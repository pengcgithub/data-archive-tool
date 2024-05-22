package com.smartlink.archive.infrastructure.utils;

import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

import com.cronutils.model.Cron;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import static com.cronutils.model.CronType.*;

/**
 * cron工具类<br/>
 *
 * @author pengcheng@smartlink.com.cn
 * @date 2024/4/30 8:50 AM
 */
public class CronUtils {

    private static final CronDefinition CRON_DEFINITION = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);

    /**
     * 上一个执行时间
     * https://blog.csdn.net/Anenan/article/details/120260617
     * @param expression cron表达式
     */
    public static LocalDateTime lastExecution(String expression) {
        CronParser parser = new CronParser(CRON_DEFINITION);
        Cron quartzCron = parser.parse(expression);
        ZonedDateTime now = ZonedDateTime.now();
        ExecutionTime executionTime = ExecutionTime.forCron(quartzCron);
        Optional<ZonedDateTime> zonedDateTimeOptional = executionTime.lastExecution(now);
        if (zonedDateTimeOptional.isPresent()) {
            ZonedDateTime zonedDateTime = zonedDateTimeOptional.get();
            return zonedDateTime.toLocalDateTime();
        }
        return null;
    }

    public static LocalDateTime nextExecution(String expression) {
        CronParser parser = new CronParser(CRON_DEFINITION);
        Cron quartzCron = parser.parse(expression);
        ZonedDateTime now = ZonedDateTime.now();
        ExecutionTime executionTime = ExecutionTime.forCron(quartzCron);
        Optional<ZonedDateTime> zonedDateTimeOptional = executionTime.nextExecution(now);
        if (zonedDateTimeOptional.isPresent()) {
            ZonedDateTime zonedDateTime = zonedDateTimeOptional.get();
            return zonedDateTime.toLocalDateTime();
        }
        return null;
    }

    /**
     * 是否为cron表达式
     * @param expression 表达式
     * @return true 是cron表达式
     */
    public static Boolean isCronExpression(String expression) {
        Boolean isCron = Boolean.TRUE;
        try {
            CronParser parser = new CronParser(CRON_DEFINITION);
            Cron quartzCron = parser.parse(expression);
        } catch (Exception e) {
            isCron = Boolean.FALSE;
        }
        return isCron;
    }

    public static void main(String[] args) {
        String cronExpression = "0 0 2 1 * ? *";

        System.out.println(isCronExpression("111"));

//        System.out.println("上一个执行时间：" + lastExecution(cronExpression));
//        System.out.println("下一个执行时间：" + nextExecution(cronExpression));
    }

}
