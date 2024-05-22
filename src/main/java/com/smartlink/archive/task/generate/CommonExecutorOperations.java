package com.smartlink.archive.task.generate;

import cn.hutool.core.date.DateUtil;
import com.smartlink.archive.constant.ArchiveConfigConstants;
import com.smartlink.archive.infrastructure.db.entity.ArchiveConfigEntity;
import com.smartlink.archive.infrastructure.utils.CommonUtils;
import com.smartlink.archive.infrastructure.utils.CronUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 公共的执行操作逻辑<br/>
 *
 * @author pengcheng@smartlink.com.cn
 * @date 2024/4/30 10:37 AM
 */
public class CommonExecutorOperations {

    public static StrSubstitutor getSub(ArchiveConfigEntity archiveConfigEntity) {
        Map<String, String> variableMap = new HashMap<>();
        addPeriodVariableMap(archiveConfigEntity.getExecTimeWindowCron(), variableMap);
        addTimeVariableMap(variableMap);
        return new StrSubstitutor(variableMap);
    }

    private static void addPeriodVariableMap(String execTimeWindowCron, Map<String, String> variableMap) {
        if (!CronUtils.isCronExpression(execTimeWindowCron)) {
            return;
        }

        LocalDateTime lastTime = CronUtils.lastExecution(execTimeWindowCron);
        variableMap.put(ArchiveConfigConstants.SQL_VARIABLE_PERIOD_START, DateUtil.formatLocalDateTime(lastTime));
        variableMap.put(ArchiveConfigConstants.SQL_VARIABLE_PERIOD_END, DateUtil.formatLocalDateTime(LocalDateTime.now().withSecond(0)));
    }

    private static void addTimeVariableMap(Map<String, String> variableMap) {
        long[] lastDayTimeStamps = CommonUtils.getStandardTimeStampRangeOnLastDay();
        variableMap.put(ArchiveConfigConstants.SQL_TASK_VARIABLE_LAST_DAY_START, CommonUtils.timeStampsFormat(lastDayTimeStamps[0]));
        variableMap.put(ArchiveConfigConstants.SQL_TASK_VARIABLE_LAST_DAY_END, CommonUtils.timeStampsFormat(lastDayTimeStamps[1]));

        long[] last7DaysTimeStamps = CommonUtils.getStandardTimeStampRangeOnLast7Days();
        variableMap.put(ArchiveConfigConstants.SQL_TASK_VARIABLE_LAST_7_DAY_START, CommonUtils.timeStampsFormat(last7DaysTimeStamps[0]));
        variableMap.put(ArchiveConfigConstants.SQL_TASK_VARIABLE_LAST_7_DAY_END, CommonUtils.timeStampsFormat(last7DaysTimeStamps[1]));

        long[] last30DaysTimeStamps = CommonUtils.getStandardTimeStampRangeOnLast30Days();
        variableMap.put(ArchiveConfigConstants.SQL_TASK_VARIABLE_LAST_30_DAY_START, CommonUtils.timeStampsFormat(last30DaysTimeStamps[0]));
        variableMap.put(ArchiveConfigConstants.SQL_TASK_VARIABLE_LAST_30_DAY_END, CommonUtils.timeStampsFormat(last30DaysTimeStamps[1]));

        long[] lastWeekTimeStamps = CommonUtils.getStandardTimeStampRangeOnLastWeek();
        variableMap.put(ArchiveConfigConstants.SQL_TASK_VARIABLE_LAST_WEEK_START, CommonUtils.timeStampsFormat(lastWeekTimeStamps[0]));
        variableMap.put(ArchiveConfigConstants.SQL_TASK_VARIABLE_LAST_WEEK_END, CommonUtils.timeStampsFormat(lastWeekTimeStamps[1]));

        long[] lastMonthTimeStamps = CommonUtils.getStandardTimeStampRangeOnLastMonth();
        variableMap.put(ArchiveConfigConstants.SQL_TASK_VARIABLE_LAST_MONTH_START, CommonUtils.timeStampsFormat(lastMonthTimeStamps[0]));
        variableMap.put(ArchiveConfigConstants.SQL_TASK_VARIABLE_LAST_MONTH_END, CommonUtils.timeStampsFormat(lastMonthTimeStamps[1]));
    }

}
