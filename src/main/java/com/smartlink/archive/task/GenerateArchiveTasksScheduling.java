package com.smartlink.archive.task;

import com.smartlink.archive.task.generate.GenerateArchiveTasksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 生成归档任务的，定时任务<br/>
 *
 * Created by pengcheng on 2024/5/6.
 */
@Component
@Slf4j
public class GenerateArchiveTasksScheduling {

    @Autowired
    private GenerateArchiveTasksService generateArchiveTasksService;

    /**
     * 每分钟执行一次，扫描归档配置，并且生成任务
     */
    @Scheduled(cron = "00 * * * * ? ")
    public void running() {
        log.info("[BIZ] >>>>>>>>>>>>>>>> 开始生成归档任务");
        generateArchiveTasksService.generateArchiveTasks();
    }

}
