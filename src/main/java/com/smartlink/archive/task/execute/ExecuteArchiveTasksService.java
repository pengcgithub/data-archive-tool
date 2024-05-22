package com.smartlink.archive.task.execute;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.smartlink.archive.enums.ArchiveTypeEnum;
import com.smartlink.archive.enums.ExecStatusEnum;
import com.smartlink.archive.infrastructure.db.entity.ArchiveTasksEntity;
import com.smartlink.archive.infrastructure.db.service.ArchiveTasksService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 执行归档任务服务<br/>
 *
 * Created by pengcheng on 2024/5/6.
 */
@Component
@Slf4j
public class ExecuteArchiveTasksService {

    @Autowired
    private ArchiveTasksService archiveTasksService;

    public void executeArchiveTasks() {

        // 获取任务状态为初始化状态的归档任务列表
        List<ArchiveTasksEntity> archiveTasksEntities = archiveTasksService.list(Wrappers
                .lambdaQuery(ArchiveTasksEntity.class)
                .eq(ArchiveTasksEntity::getExecStatus, ExecStatusEnum.INITIAL.name())
                .orderByAsc(ArchiveTasksEntity::getPriority));

        if (CollectionUtils.isEmpty(archiveTasksEntities)) {
            log.info("[BIZ] 当前没有需要执行的归档任务");
            return;
        }

        log.info("[BIZ] 扫描到{}条，需要执行的归档任务", archiveTasksEntities.size());

        // 批量更新为运行状态和归档开始时间
        Date now = new Date();
        archiveTasksEntities.forEach(archiveTasksEntity -> {
            archiveTasksEntity.setExecStart(now);
            archiveTasksEntity.setSysUtime(now);
            archiveTasksEntity.setExecStatus(ExecStatusEnum.RUNNING.name());
        });
        archiveTasksService.saveOrUpdateBatch(archiveTasksEntities);

        // 开始批量执行归档任务
        List<CompletableFuture<String>> completableFutures = Lists.newArrayList();
        for (ArchiveTasksEntity dataEntityActuator : archiveTasksEntities) {
            CompletableFuture<String> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                return executeShellCmd(dataEntityActuator);
            });
            completableFutures.add(voidCompletableFuture);
        }

        completableFutures.forEach(a -> {
            try {
                a.get();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            } catch (ExecutionException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private String executeShellCmd(ArchiveTasksEntity dataEntityActuator) {

        StopWatch sw = new StopWatch();
        sw.start();

        ShellCommandActuator.ShellFileSource templateFile = null;
        try {
            String archiveCmd = dataEntityActuator.getArchiveCmd();
            if (StringUtils.equals(dataEntityActuator.getArchiveType(), ArchiveTypeEnum.TDENGINE.name().toLowerCase(Locale.ROOT)) ||
                    StringUtils.equals(dataEntityActuator.getArchiveType(), ArchiveTypeEnum.MYSQL_DATAX.name().toLowerCase(Locale.ROOT))) {
                // datax 需要处理json template的问题
                templateFile = ShellCommandActuator.createTemplateFile(dataEntityActuator.getTemplateStr());
                archiveCmd = MessageFormat.format(archiveCmd, templateFile.getFilePath());
            }

            String execLog = ShellCommandActuator.exec(archiveCmd);
            dataEntityActuator.setExecLog(execLog);
            dataEntityActuator.setExecStatus(ExecStatusEnum.SUCCESS.name());

        } catch (Exception ex) {

            log.error(ex.getMessage(), ex);
            dataEntityActuator.setExecLog(ex.getMessage());
            dataEntityActuator.setExecStatus(ExecStatusEnum.ERROR.name());

        } finally {
            // 记录执行持续时间
            sw.stop();
            double totalTimeSeconds = sw.getTotalTimeSeconds();
            dataEntityActuator.setExecSeconds((int) totalTimeSeconds);
            dataEntityActuator.setExecEnd(new Date());
            dataEntityActuator.setSysUtime(new Date());
            archiveTasksService.updateById(dataEntityActuator);

            if (ObjectUtil.isNotNull(templateFile)) {
                // 模版执行完成后，删除模版
                FileUtil.del(templateFile.getFile());
            }

            log.info("[BIZ] 任务ID{},已经执行完成，执行结果：{}，持续时间：{}，执行日志：\n{}", dataEntityActuator.getId(), dataEntityActuator.getExecStatus(), totalTimeSeconds, dataEntityActuator.getExecLog());
        }

        return "OK";
    }

}
