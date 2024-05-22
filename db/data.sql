-- ----------------------------
-- Records of archive_config
-- ----------------------------
BEGIN;
-- mysql
-- --no-delete --ask-pass 归档但是不删除
-- create_time >= '${lastMonthStartTimestamp}' and create_time <= '${lastMonthEndTimestamp}' 使用时间增加逻辑
INSERT INTO smart_transport.archive_config (id, source_host, source_port, source_db, source_table, dest_host, dest_port, dest_db, dest_table, archive_mode, charset, archive_condition, exec_time_window_cron, extension_cmd, priority, sys_ctime, sys_utime, is_enable, remark, archive_type, source_table_column, dest_table_column, extension_properties, query_sql) VALUES (1, '10.128.29.232', 3306, 'smart_transport', 'goods', '10.128.29.232', 3306, 'smart_transport', 'goods', 'ARCHIVE_TO_FILE', 'none', 'create_time >= ''${lastMonthStartTimestamp}'' and create_time <= ''${lastMonthEndTimestamp}''', '0 0 2 1 * ? *', '--no-delete --ask-pass', 1, '2024-04-26 07:15:31', '2024-04-30 14:24:26', 1, null, 'mysql', null, null, null, null);

-- tdengine
INSERT INTO smart_transport.archive_config (id, source_host, source_port, source_db, source_table, dest_host, dest_port, dest_db, dest_table, archive_mode, charset, archive_condition, exec_time_window_cron, extension_cmd, priority, sys_ctime, sys_utime, is_enable, remark, archive_type, source_table_column, dest_table_column, extension_properties, query_sql) VALUES (2, '172.29.30.145', 6041, 'safety_center', 'safety_alarm', '172.29.30.145', 6041, 'safety_center', 'safety_alarm', 'ARCHIVE_TO_FILE', 'none', 'create_time >= ''${lastMonthStartTimestamp}'' and create_time <= ''${lastMonthEndTimestamp}''', '0 0 2 1 * ? *', '', 1, '2024-04-26 07:15:31', '2024-05-22 19:05:04', 1, null, 'tdengine', 'create_time,terminal_id,alarm_id,driver_name', 'create_time,terminal_id,alarm_id,driver_name', null, null);

-- mysql
-- archiveUser=root,archiveConfig.batchSize=100,archiveConfig.txnSize=99 扩展yaml配置，数据库配置作为第一优先级
INSERT INTO smart_transport.archive_config (id, source_host, source_port, source_db, source_table, dest_host, dest_port, dest_db, dest_table, archive_mode, charset, archive_condition, exec_time_window_cron, extension_cmd, priority, sys_ctime, sys_utime, is_enable, remark, archive_type, source_table_column, dest_table_column, extension_properties, query_sql) VALUES (3, '10.128.29.232', 3306, 'smart_transport', 'goods', '10.128.29.232', 3306, 'smart_transport', 'goods', 'ARCHIVE_TO_FILE', 'none', 'create_time >= ''${lastMonthStartTimestamp}'' and create_time <= ''${lastMonthEndTimestamp}''', '0 0 2 1 * ? *', '--no-delete --ask-pass', 1, '2024-04-30 17:01:17', '2024-05-06 17:23:09', 1, null, 'mysql', null, null, 'archiveUser=root,archiveConfig.batchSize=100,archiveConfig.txnSize=99', null);

-- mysql datax
-- query_sql 自定义sql语句导出，会忽略source_table、source_table_column、archive_condition字段
INSERT INTO smart_transport.archive_config (id, source_host, source_port, source_db, source_table, dest_host, dest_port, dest_db, dest_table, archive_mode, charset, archive_condition, exec_time_window_cron, extension_cmd, priority, sys_ctime, sys_utime, is_enable, remark, archive_type, source_table_column, dest_table_column, extension_properties, query_sql) VALUES (4, '10.128.29.232', 3306, 'smart_transport', '', '10.128.29.232', 3306, 'smart_transport', '', 'ARCHIVE_TO_FILE', 'none', 'create_time >= ''${lastMonthStartTimestamp}'' and create_time <= ''${lastMonthEndTimestamp}''', '0 0 2 1 * ? *', '', 1, '2024-05-08 14:54:28', '2024-05-08 14:54:28', 1, null, 'mysql_datax', null, null, null, 'select id, business_type, business_id, province, city, district, replace(address, '','', ''，''), address_type, lon, lat, sort, delete_flag, null_work from business_address_bak where id <= 1000;');








COMMIT;