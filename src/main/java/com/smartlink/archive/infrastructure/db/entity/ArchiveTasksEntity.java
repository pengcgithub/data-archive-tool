package com.smartlink.archive.infrastructure.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 归档任务表<br/>
 *
 * Created by pengcheng on 2024/5/11.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "archive_tasks")
public class ArchiveTasksEntity implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 归档配置编号
     */
    private Integer archiveConfigId;

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
     * 归档模式：archive（归档），archive-slow(慢模式，兼容性高),delete(只删除不归档)，archive-to-file(归档到文件)
     */
    private String archiveMode;

    /**
     * 优化级，数值越高，在执行时间窗口的有多个任务时，优先执行
     */
    private Byte priority;

    /**
     * 运行的状态，initial:初始状态，running:执行中，check failed:检查不通过，wait timeout:等待超时，done:已执行
     */
    private String execStatus;

    /**
     * 归档开始时间
     */
    private Date execStart;

    /**
     * 归档结束时间
     */
    private Date execEnd;

    /**
     * 执行时间（秒）
     */
    private Integer execSeconds;

    /**
     * 归档命令
     */
    private String archiveCmd;

    /**
     * 模版字符串 json串
     */
    private String templateStr;

    /**
     * 执行日志
     */
    private String execLog;

    /**
     * 修改时间
     */
    private Date sysUtime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}