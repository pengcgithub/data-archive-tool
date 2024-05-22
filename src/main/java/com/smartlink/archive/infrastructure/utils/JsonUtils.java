package com.smartlink.archive.infrastructure.utils;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartlink.archive.infrastructure.config.TdengineArchiveProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * json工具类<br/>
 *
 * Created by pengcheng on 2024/5/11.
 */
public class JsonUtils {

    /**
     * 字符串转json object对象
     * @param properties 字符串 dataxPath=/Users/pengcheng/Downloads/datax,archiveUser=root,tdengineConfig.batchSize=100
     * @return json对象
     */
    public static JSONObject toJson(String properties) {
        String[] pairs = properties.split(",");

        // 创建一个JSONObject来存储键值对
        JSONObject jsonObject = new JSONObject();

        // 遍历参数数组，将每个参数分割成键和值，并添加到JSONObject中
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                // 如果键包含点号（.），则可能是一个嵌套属性的键
                if (key.contains(".")) {
                    String[] nestedKeys = key.split("\\.");
                    JSONObject nestedObject = jsonObject;
                    for (int i = 0; i < nestedKeys.length - 1; i++) {
                        String nestedKey = nestedKeys[i];
                        if (!nestedObject.containsKey(nestedKey)) {
                            nestedObject.put(nestedKey, new JSONObject());
                        }
                        nestedObject = (JSONObject) nestedObject.get(nestedKey);
                    }
                    // 设置最后一个嵌套属性的值
                    nestedObject.put(nestedKeys[nestedKeys.length - 1], value);
                } else {
                    // 普通的键值对
                    jsonObject.put(key, value);
                }
            }
        }

        return jsonObject;
    }

    public static void main(String[] args) {
        Map<String, Object> param = new HashMap<String, Object>(){{
            put("dataxPath", "1234");
            put("archiveUser", "root");
            put("archivePwd", "1234");
        }};
        String properties = "dataxPath=/Users/pengcheng/Downloads/datax,archiveUser=root,tdengineConfig.batchSize=100";
        JSONObject jsonObject = toJson(properties);
        TdengineArchiveProperties dataArchive = jsonObject.toJavaObject(TdengineArchiveProperties.class);
        Map<String, Object> extensionParam = BeanUtil.beanToMap(dataArchive, false, true);
        System.out.println(JSON.toJSONString(extensionParam));

    }

}
