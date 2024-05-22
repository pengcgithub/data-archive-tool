/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.3.15
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : 192.168.3.15:3306
 Source Schema         : mysql_archiver

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 26/08/2022 09:52:50
*/

CREATE DATABASE IF NOT EXISTS `mysql_archiver` DEFAULT CHARACTER SET utf8 collate utf8_unicode_ci;

USE `mysql_archiver`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for archive_config
-- ----------------------------
DROP TABLE IF EXISTS `archive_config`;
create table archive_config
(
    id                    int auto_increment comment 'id'
        primary key,
    source_host           varchar(64)                            not null comment '源服务器',
    source_port           int                                    not null comment '源服务器端口',
    source_db             varchar(64)                            not null comment '源数据库schema',
    source_table          varchar(128)                           not null comment '源数据库表',
    source_table_column   varchar(512)                           null comment '源数据表字段，多个用英文逗号隔开',
    dest_host             varchar(64)                            not null comment '目标服务器',
    dest_port             int          default 3306              not null comment '目标服务器端口',
    dest_db               varchar(64)  default ''                not null comment '目标数据库schema',
    dest_table            varchar(128) default ''                not null comment '目标数据库表',
    dest_table_column     varchar(512)                           null comment '目标数据表字段，多个用英文逗号隔开',
    archive_mode          varchar(20)  default 'ARCHIVE'         not null comment '归档模式：ARCHIVE（归档）,DELETE(只删除不归档)，ARCHIVE_TO_FILE(归档到文件)',
    charset               varchar(20)  default 'UTF8'            not null comment '字符集',
    archive_condition     varchar(255) default ''                not null comment '归档条件',
    exec_time_window_cron varchar(30)  default ''                not null comment '执行时间窗口（默认空则需要手动触发构建），如：0 0 2 1 * ? *,表示在每月的1日的凌晨2点执行任务。',
    extension_cmd         varchar(255) default ''                null comment '归档扩展命令',
    priority              tinyint      default 1                 null comment '优化级，数值越高，在执行时间窗口的有多个任务时，优先执行',
    sys_ctime             datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    sys_utime             datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    is_enable             tinyint      default 0                 null comment '是否开启：1:开启，0:关闭',
    remark                varchar(200)                           null comment '备注信息',
    archive_type          varchar(20)  default 'mysql'           null comment '归档类型',
    begin_date_time       varchar(32)                            null comment '归档开始时间',
    end_date_time         varchar(32)                            null comment '归档结束时间',
    extension_properties  varchar(2048)                          null comment '归档扩展配置，针对application.yml中配置做增强',
    query_sql             varchar(2048)                          null comment '查询sql（datax, 配置后会忽略 table, column, where, beginDateTime, endDateTime这些配置）'
)
    comment '归档配置表' charset = utf8;

create index idx_source_db_source_table
    on archive_config (source_db, source_table);

-- ----------------------------
-- Table structure for archive_tasks
-- ----------------------------
DROP TABLE IF EXISTS `archive_tasks`;
create table archive_tasks
(
    id                int auto_increment comment 'id' primary key,
    source_host       varchar(64)                            not null comment '源服务器',
    source_port       int                                    not null comment '源服务器端口',
    source_db         varchar(64)                            not null comment '源数据库schema',
    source_table      varchar(128)                           not null comment '源数据库表',
    dest_host         varchar(64)                            not null comment '目标服务器',
    dest_port         int                                    not null comment '目标服务器端口',
    dest_db           varchar(64)                            null comment '目标数据库schema',
    dest_table        varchar(128)                           null comment '目标数据库表',
    archive_mode      varchar(20)  default 'archive'         null comment '归档模式：ARCHIVE（归档）,DELETE(只删除不归档)，ARCHIVE_TO_FILE(归档到文件)',
    priority          tinyint      default 1                 null comment '优化级，数值越高，在执行时间窗口的有多个任务时，优先执行',
    exec_status       varchar(100) default 'INITIAL'         null comment '运行的状态，INITIAL:初始状态，RUNNING:执行中，ERROR:执行失败，WAIT_TIMEOUT:等待超时，SUCCESS:执行成功',
    exec_start        datetime                               null comment '归档开始时间',
    exec_end          datetime                               null comment '归档结束时间',
    exec_seconds      int                                    null comment '执行时间（秒）',
    archive_cmd       varchar(2000)                          null comment '归档命令',
    exec_log          longtext                               null comment '执行日志',
    sys_utime         datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    archive_type      varchar(20)  default 'mysql'           null comment '归档类型',
    template_str      text                                   null comment '模版字符串',
    archive_config_id int          default 0                 not null comment '归档配置编号'
)
    comment '归档任务表' charset = utf8;

create index idx_source_db_table
    on archive_tasks (source_db, source_table, exec_start);

create index idx_source_host_port
    on archive_tasks (source_host, source_port, exec_start);

SET FOREIGN_KEY_CHECKS = 1;
