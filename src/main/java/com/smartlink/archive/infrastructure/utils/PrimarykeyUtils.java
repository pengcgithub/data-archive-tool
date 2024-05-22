package com.smartlink.archive.infrastructure.utils;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by pengcheng on 2024/5/11.
 */
public class PrimarykeyUtils {

    private static final String PREFIX = "10000";

    public static Long generatePrimarykey(Long primarykey) {
        return NumberUtils.toLong(PREFIX + primarykey);
    }

}
