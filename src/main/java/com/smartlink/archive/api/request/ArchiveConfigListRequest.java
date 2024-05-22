package com.smartlink.archive.api.request;

import lombok.Data;


/**
 * 列表查询<br/>
 *
 * Created by pengcheng on 2024/4/30.
 */
@Data
public class ArchiveConfigListRequest {

    /**
     * 归档类型
     * mysql、tdengine
     */
    private String archiveType;

    /**
     * 是否开启：1:开启，0:关闭
     */
    private Integer isEnable;

}
