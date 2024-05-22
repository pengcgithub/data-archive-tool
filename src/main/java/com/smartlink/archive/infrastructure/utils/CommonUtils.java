package com.smartlink.archive.infrastructure.utils;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.*;

import java.time.temporal.TemporalAdjusters;

/**
 * 公共工具类<br/>
 *
 * Created by pengcheng on 2024/4/30.
 */
@Slf4j
public final class CommonUtils {

    /**
     * 一天的毫秒数
     */
    private static final int MILLS_IN_DAY = 24 * 60 * 60 * 1000;

    private CommonUtils() {
    }

    /**
     * 获取昨天的起止标准时间
     *
     * @return 昨天的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnLastDay() {
        return getStandardTimeStampRangeByDay(LocalDate.now().minusDays(1));
    }

    /**
     * 获取最近7天的起止标准时间
     *
     * @return 最近7天的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnLast7Days() {
        long[] startTimestamps = getStandardTimeStampRangeByDay(LocalDate.now().minusDays(7));
        long[] endTimestamps = getStandardTimeStampRangeByDay(LocalDate.now().minusDays(1));
        return new long[]{startTimestamps[0], endTimestamps[1]};
    }

    /**
     * 获取最近30天的起止标准时间
     *
     * @return 最近30天的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnLast30Days() {
        long[] startTimestamps = getStandardTimeStampRangeByDay(LocalDate.now().minusDays(30));
        long[] endTimestamps = getStandardTimeStampRangeByDay(LocalDate.now().minusDays(1));
        return new long[]{startTimestamps[0], endTimestamps[1]};
    }

    /**
     * 获取上星期的起止标准时间
     *
     * @return 上星期的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnLastWeek() {
        return getStandardTimeStampRangeByWeek(LocalDate.now().minusWeeks(1));
    }

    /**
     * 获取上月的起止标准时间
     *
     * @return 上个月的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnLastMonth() {
        return getStandardTimeStampRangeByMonth(LocalDate.now().minusMonths(1));
    }

    /**
     * 获取基准时间的昨天的起止标准时间
     *
     * @param baseTime 基准时间
     * @return 基准时间的昨天的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnLastDay(LocalDateTime baseTime) {
        return getStandardTimeStampRangeByDay(baseTime.toLocalDate().minusDays(1));
    }

    /**
     * 获取基准时间的最近7天的起止标准时间
     *
     * @param baseTime 基准时间
     * @return 基准时间的最近7天的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnLast7Days(LocalDateTime baseTime) {
        long[] startTimestamps = getStandardTimeStampRangeByDay(baseTime.toLocalDate().minusDays(7));
        long[] endTimestamps = getStandardTimeStampRangeByDay(baseTime.toLocalDate().minusDays(1));
        return new long[]{startTimestamps[0], endTimestamps[1]};
    }

    /**
     * 获取基准时间的最近30天的起止标准时间
     *
     * @param baseTime 基准时间
     * @return 基准时间的最近30天的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnLast30Days(LocalDateTime baseTime) {
        long[] startTimestamps = getStandardTimeStampRangeByDay(baseTime.toLocalDate().minusDays(30));
        long[] endTimestamps = getStandardTimeStampRangeByDay(baseTime.toLocalDate().minusDays(1));
        return new long[]{startTimestamps[0], endTimestamps[1]};
    }

    /**
     * 获取基准时间的上星期的起止标准时间
     *
     * @param baseTime 基准时间
     * @return 基准时间的上星期的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnLastWeek(LocalDateTime baseTime) {
        return getStandardTimeStampRangeByWeek(baseTime.toLocalDate().minusWeeks(1));
    }

    /**
     * 获取基准时间的上月的起止标准时间
     *
     * @param baseTime 基准时间
     * @return 基准时间的上个月的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnLastMonth(LocalDateTime baseTime) {
        return getStandardTimeStampRangeByMonth(baseTime.toLocalDate().minusMonths(1));
    }

    /**
     * 获取今天的起止标准时间
     *
     * @return 今天的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnCurrentDay() {
        long startStamp = getTimestamp(LocalDate.now().atStartOfDay());
        return new long[]{startStamp, System.currentTimeMillis()};
    }

    /**
     * 获取本星期的起止标准时间
     *
     * @return 本星期的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnCurrentWeek() {
        return getStandardTimeStampRangeByWeek(LocalDate.now());
    }

    /**
     * 获取本月的起止标准时间
     *
     * @return 本月的起止标准时间
     */
    public static long[] getStandardTimeStampRangeOnCurrentMonth() {
        return getStandardTimeStampRangeByMonth(LocalDate.now());
    }

    /**
     * 获取一星期的起止标准时间
     *
     * @param date 日期
     * @return 起止标准时间
     */
    public static long[] getStandardTimeStampRangeByWeek(LocalDate date) {
        LocalDate startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        long startStamp = getTimestamp(startDate.atStartOfDay());
        long endStamp = getTimestamp(endDate.atStartOfDay()) + MILLS_IN_DAY;

        return new long[]{startStamp, endStamp};
    }

    /**
     * 获取一月的起止标准时间
     *
     * @param date 日期
     * @return 起止标准时间
     */
    public static long[] getStandardTimeStampRangeByMonth(LocalDate date) {
        LocalDate startDate = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endDate = date.with(TemporalAdjusters.lastDayOfMonth());
        long startStamp = getTimestamp(startDate.atStartOfDay());
        long endStamp = getTimestamp(endDate.atStartOfDay()) + MILLS_IN_DAY;

        return new long[]{startStamp, endStamp};
    }

    /**
     * 获取一天的起止标准时间
     *
     * @param date 日期
     * @return 起止标准时间
     */
    public static long[] getStandardTimeStampRangeByDay(LocalDate date) {
        long startStamp = getTimestamp(date.atStartOfDay());
        long endStamp = startStamp + MILLS_IN_DAY;

        return new long[]{startStamp, endStamp};
    }

    /**
     * 获取LocalDatetime 的毫秒标签
     *
     * @param dateTime dateTime
     * @return 毫秒值
     */
    public static long getTimestamp(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        return dateTime.atZone(zoneId).toInstant().toEpochMilli();
    }

    /**
     * 时间戳格式化
     * @param timeStamps 毫秒级时间戳
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String timeStampsFormat(long timeStamps) {
        return DateUtil.formatDateTime(DateUtil.date(timeStamps));
    }


}
