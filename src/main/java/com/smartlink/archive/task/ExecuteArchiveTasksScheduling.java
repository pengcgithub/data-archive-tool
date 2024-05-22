package com.smartlink.archive.task;

import com.smartlink.archive.task.execute.ExecuteArchiveTasksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 执行归档任务的定时任务<br/>
 *
 * Created by pengcheng on 2024/5/6.
 */
@Component
@Slf4j
public class ExecuteArchiveTasksScheduling {

    @Autowired
    private ExecuteArchiveTasksService executeArchiveTasksService;

    /**
     * 每隔5分钟执行一次归档任务
     */
    @Scheduled(cron = "00 0/5 * * * ? ")
    public void running() {
        log.info("[BIZ] >>>>>>>>>>>>>>>> 开始执行归档任务");
        executeArchiveTasksService.executeArchiveTasks();
        log.info("[BIZ] >>>>>>>>>>>>>>>> 执行归档任务完成");
    }

}
