package com.smartlink.archive.infrastructure.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smartlink.archive.enums.ArchiveModeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 归档配置表<br/>
 *
 * Created by pengcheng on 2024/4/30.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "archive_config")
public class ArchiveConfigEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 归档类型
     * mysql、tdengine
     */
    private String archiveType;

    /**
     * 源服务器
     */
    private String sourceHost;

    /**
     * 源服务器端口
     */
    private Integer sourcePort;

    /**
     * 源数据库schema
     */
    private String sourceDb;

    /**
     * 源数据库表
     */
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
     *
     * @see ArchiveModeEnum
     */
    private String archiveMode;

    /**
     * 字符集
     */
    private String charset;

    /**
     * 归档条件
     * tdengine/mysql=查询条件
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
     * 创建时间
     */
    private Date sysCtime;

    /**
     * 修改时间
     */
    private Date sysUtime;

    /**
     * 是否开启：1:开启，0:关闭
     */
    private Integer isEnable;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * mysql=扩展命令
     */
    private String extensionCmd;

    /**
     * 扩展配置
     * @see com.smartlink.archive.infrastructure.config.DataArchiveProperties
     * @see com.smartlink.archive.infrastructure.config.TdengineArchiveProperties
     */
    private String extensionProperties;

    /**
     * 查询sql（datax, 配置后会忽略 table, column, where, beginDateTime, endDateTime这些配置）
     */
    private String querySql;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}