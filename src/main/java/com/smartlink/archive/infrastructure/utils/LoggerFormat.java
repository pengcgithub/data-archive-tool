package com.smartlink.archive.infrastructure.utils;

import com.alibaba.fastjson2.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 日志格式器<br/>
 *
 * @author pengcheng@smartlink.com.cn
 * @date 2023/7/19 5:27 PM
 */
public class LoggerFormat {

    private final Map<String, String> logInfo = new LinkedHashMap<>();

    private LoggerFormat() {}

    public static LoggerFormat build() {
        return new LoggerFormat();
    }

    /**
     * 日志备注
     * @param remark 备注
     */
    public LoggerFormat remark(String remark) {
        logInfo.put("remark", remark);
        return this;
    }

    /**
     * 日志数据
     * @param key
     * @param value
     */
    public LoggerFormat data(String key, String value) {
        logInfo.put(key, value);
        return this;
    }

    public LoggerFormat data(String key, Object value) {
        logInfo.put(key, JSONObject.toJSONString(value));
        return this;
    }

    public String finish() {
        StringBuilder sb = new StringBuilder();
        if (!logInfo.isEmpty()) {
            Set<String> keys = logInfo.keySet();
            for (String key : keys) {
                sb.append(key).append("=[").append(logInfo.get(key)).append("] ");
            }
        }
        return sb.toString();
    }
}