package com.smartlink.archive.constant;

/**
 * 常量类
 * Created by pengcheng on 2024/4/30.
 */
public interface ArchiveConfigConstants {

    /**
     * 打开
     */
    Integer TURN_ON = 1;

    /**
     * 关闭
     */
    Integer TURN_OFF = 0;


    /**
     * 批量每次归档的数据数量
     */
    String DEFAULT_BATCHSIZE = "1000";

    /**
     * 指定每个事务的大小（行数）
     */
    String DEFAULT_TXNSIZE = "1000";

    /**
     * 默认归档文件路径
     */
    String DEFAULT_ARCHIVE_FILE_PATH = "/opt/archive";

    /**
     * 默认tdengine文件路径
     */
    String DEFAULT_TDENGINE_FILE_PATH = "/opt/tdengine";

    /**
     * 周期开始时间
     */
    String SQL_VARIABLE_PERIOD_START = "periodStartTimestamp";

    /**
     * 周期结束时间
     */
    String SQL_VARIABLE_PERIOD_END = "periodEndTimestamp";

    /**
     * SQL任务变量-上个周期开始时间戳
     */
    String SQL_TASK_VARIABLE_LAST_PERIOD_START = "lastPeriodStartTimestamp";

    /**
     * SQL任务变量-上个周期结束时间戳
     */
    String SQL_TASK_VARIABLE_LAST_PERIOD_END = "lastPeriodEndTimestamp";

    /**
     * SQL任务变量-昨天开始时间戳
     */
    String SQL_TASK_VARIABLE_LAST_DAY_START = "lastDayStartTimestamp";

    /**
     * SQL任务变量-昨天结束时间戳
     */
    String SQL_TASK_VARIABLE_LAST_DAY_END = "lastDayEndTimestamp";

    /**
     * SQL任务变量-上周开始时间戳
     */
    String SQL_TASK_VARIABLE_LAST_WEEK_START = "lastWeekStartTimestamp";

    /**
     * SQL任务变量-上周结束时间戳
     */
    String SQL_TASK_VARIABLE_LAST_WEEK_END = "lastWeekEndTimestamp";

    /**
     * SQL任务变量-上月开始时间戳
     */
    String SQL_TASK_VARIABLE_LAST_MONTH_START = "lastMonthStartTimestamp";

    /**
     * SQL任务变量-上月结束时间戳
     */
    String SQL_TASK_VARIABLE_LAST_MONTH_END = "lastMonthEndTimestamp";

    /**
     * SQL任务变量-过去7天开始时间戳
     */
    String SQL_TASK_VARIABLE_LAST_7_DAY_START = "last7DaysStartTimestamp";

    /**
     * SQL任务变量-过去7天结束时间戳
     */
    String SQL_TASK_VARIABLE_LAST_7_DAY_END = "last7DaysEndTimestamp";

    /**
     * SQL任务变量-过去30天开始时间戳
     */
    String SQL_TASK_VARIABLE_LAST_30_DAY_START = "last30DaysStartTimestamp";

    /**
     * SQL任务变量-过去30天结束时间戳
     */
    String SQL_TASK_VARIABLE_LAST_30_DAY_END = "last30DaysEndTimestamp";

}
