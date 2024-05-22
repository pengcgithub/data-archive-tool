package com.smartlink.archive.api.dto;

import com.smartlink.archive.enums.ArchiveModeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


/**
 * 归档配置<br/>
 *
 * Created by pengcheng on 2024/4/30.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveConfigDTO implements Serializable {

    private Integer id;

    /**
     * 归档类型
     * mysql、tdengine
     */
    @NotEmpty
    private String archiveType;

    /**
     * 源服务器
     */
    @NotEmpty
    private String sourceHost;

    /**
     * 源服务器端口
     */
    @NotEmpty
    private Integer sourcePort;

    /**
     * 源数据库schema
     */
    @NotEmpty
    private String sourceDb;

    /**
     * 源数据库表
     */
    @NotEmpty
    private String sourceTable;

    /**
     * 源数据表字段，多个用英文逗号隔开
     */
    private String sourceTableColumn;

    /**
     * 目标服务器
     */
    private String destHost;

    /**
     * 目标服务器端口
     */
    private Integer destPort;

    /**
     * 目标数据库schema
     */
    private String destDb;

    /**
     * 目标数据库表
     */
    private String destTable;

    /**
     * 目标数据表字段，多个用英文逗号隔开
     */
    private String destTableColumn;

    /**
     * 归档模式
     * @see ArchiveModeEnum
     */
    @NotEmpty
    private String archiveMode;

    /**
     * 字符集
     */
    private String charset;

    /**
     * 归档条件
     */
    private String archiveCondition;

    /**
     * 执行时间窗口，如：0 0 2 1 * ? *
     *
     * 表示在每月的1日的凌晨2点执行任务
     */
    private String execTimeWindowCron;

    /**
     * 优化级，数值越高，在执行时间窗口的有多个任务时，优先执行
     */
    private Byte priority;

    /**
     * 开始时间
     * 默认：tdengine
     */
    private String beginDateTime;

    /**
     * 结束时间
     * 默认tdengine
     */
    private String endDateTime;

    /**
     * 是否开启：1:开启，0:关闭
     */
    @NotEmpty
    private Integer isEnable;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * mysql=扩展命令
     * tdengine=查询条件
     */
    private String extensionCmd;

    /**
     * 查询sql（datax, 配置后会忽略 table, column, where, beginDateTime, endDateTime这些配置）
     */
    private String querySql;

}