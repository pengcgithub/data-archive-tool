package com.smartlink.archive.infrastructure.config;

import lombok.Data;

@Data
public class TdengineConfig {

    /**
     * 每batchSize条record为一个batch进行写入
     * 默认值：1
     */
    private String batchSize;

    /**
     * 当table为TDengine中的一张子表，table具有tag值。如果数据的tag值与table的tag值不想等，数据不写入到table中。
     * 默认值：false
     */
    private String ignoreTagsUnmatched;

}
