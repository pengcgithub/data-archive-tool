package com.smartlink.archive.task.generate;

import cn.hutool.cron.pattern.CronPattern;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.smartlink.archive.constant.ArchiveConfigConstants;
import com.smartlink.archive.enums.ArchiveTypeEnum;
import com.smartlink.archive.enums.ExecStatusEnum;
import com.smartlink.archive.infrastructure.config.DataArchiveProperties;
import com.smartlink.archive.infrastructure.config.TdengineArchiveProperties;
import com.smartlink.archive.infrastructure.db.entity.ArchiveConfigEntity;
import com.smartlink.archive.infrastructure.db.entity.ArchiveTasksEntity;
import com.smartlink.archive.infrastructure.db.service.ArchiveConfigService;
import com.smartlink.archive.infrastructure.db.service.ArchiveTasksService;
import com.smartlink.archive.infrastructure.utils.LambdaUtil;
import com.smartlink.archive.infrastructure.utils.LoggerFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by pengcheng on 2024/4/30.
 */
@Component
@Slf4j
public class GenerateArchiveTasksService {

    @Autowired
    private ArchiveConfigService archiveConfigService;
    @Autowired
    private ArchiveTasksService archiveTasksService;
    @Autowired
    private DataArchiveProperties dataArchiveProperties;
    @Autowired
    private TdengineArchiveProperties tdengineArchiveProperties;

    @Transactional(rollbackFor = Exception.class)
    public List<Integer> generateArchiveTasks() {

        // 获取当前需要生成归档任务的归档配置列表
        List<ArchiveConfigEntity> archiveConfigs = archiveConfigService.list(Wrappers
                .lambdaQuery(ArchiveConfigEntity.class)
                .eq(ArchiveConfigEntity::getIsEnable, ArchiveConfigConstants.TURN_ON)
        );
        archiveConfigs = LambdaUtil.filterToList(archiveConfigs, archiveConfigEntity -> {
            if (StringUtils.isBlank(archiveConfigEntity.getExecTimeWindowCron())) {
                return false;
            }
            CronPattern cronPattern = new CronPattern(archiveConfigEntity.getExecTimeWindowCron());
            return cronPattern.match(LocalDateTime.now(), false);
        });

        return this.doBuildArchiveTasks(archiveConfigs);
    }

    private List<Integer> doBuildArchiveTasks(List<ArchiveConfigEntity> archiveConfigs) {
        List<ArchiveTasksEntity> archiveTasksEntities = LambdaUtil.listMapper(archiveConfigs, archiveConfig ->
                buildArchiveTasksEntity(dataArchiveProperties, tdengineArchiveProperties, archiveConfig));
        if (CollectionUtils.isNotEmpty(archiveTasksEntities)) {
            archiveTasksService.saveBatch(archiveTasksEntities);
            List<Integer> archiveTaskIds = archiveTasksEntities.stream().map(ArchiveTasksEntity::getId).collect(Collectors.toList());
            log.info(LoggerFormat.build().remark("成功生成归档任务")
                    .data("total", archiveTasksEntities.size())
                    .data("ids", archiveTaskIds)
                    .finish());
            return archiveTaskIds;
        } else {
            log.error("[BIZ] >>>>>>>>>>>>>>>> 当前没有归档任务需要生成");
        }

        return Collections.emptyList();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Integer> generateArchiveTasks(List<Integer> ids) {

        // 获取当前需要生成归档任务的归档配置列表
        List<ArchiveConfigEntity> archiveConfigs = archiveConfigService.list(Wrappers
                .lambdaQuery(ArchiveConfigEntity.class)
                .in(ArchiveConfigEntity::getId, ids)
                .eq(ArchiveConfigEntity::getIsEnable, ArchiveConfigConstants.TURN_ON)
        );

        return this.doBuildArchiveTasks(archiveConfigs);
    }

    private ArchiveTasksEntity buildArchiveTasksEntity(DataArchiveProperties dataArchiveProperties, TdengineArchiveProperties tdengineArchiveProperties, ArchiveConfigEntity archiveConfig) {

        ArchiveTasksEntity archiveTasksEntity = ArchiveTasksEntity
                .builder()
                .archiveConfigId(archiveConfig.getId())
                .archiveType(archiveConfig.getArchiveType())

                .sourceHost(archiveConfig.getSourceHost())
                .sourcePort(archiveConfig.getSourcePort())
                .sourceDb(archiveConfig.getSourceDb())
                .sourceTable(archiveConfig.getSourceTable())

                .destHost(archiveConfig.getDestHost())
                .destPort(archiveConfig.getDestPort())
                .destDb(archiveConfig.getDestDb())
                .destTable(archiveConfig.getDestTable())

                .archiveMode(archiveConfig.getArchiveMode())
                .execStatus(ExecStatusEnum.INITIAL.name())

                // 优先级
                .priority(archiveConfig.getPriority())

                // 设置为当前时间
                .execStart(new Date())

                .build();

        if (StringUtils.equals(archiveConfig.getArchiveType(), ArchiveTypeEnum.TDENGINE.name().toLowerCase(Locale.ROOT))) {
            // 命令
            String cmd = GenerateTdArchiverTemplate.generateCmd(tdengineArchiveProperties, archiveConfig);
            archiveTasksEntity.setArchiveCmd(cmd);

            // 所需模版
            String templateStr = GenerateTdArchiverTemplate.generateTemplate(tdengineArchiveProperties, archiveConfig);
            archiveTasksEntity.setTemplateStr(templateStr);
        } else {
            String cmd = GeneratePtArchiverCmd.generateCmd(dataArchiveProperties, archiveConfig);
            archiveTasksEntity.setArchiveCmd(cmd);

            String templateStr = GeneratePtArchiverCmd.generateTemplate(dataArchiveProperties, archiveConfig);
            archiveTasksEntity.setTemplateStr(templateStr);
        }

        return archiveTasksEntity;
    }

}
