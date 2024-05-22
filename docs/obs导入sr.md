# 华为云obs csv文件导入starrocks数据库

### 安装Broker Load

[部署Broker节点](https://docs.mirrorship.cn/zh/docs/2.5/deployment/deploy_broker/)

### 通过Broker Load导入到 starrocks

#### csv格式要求
```csv
974,1,1614892735175622658,330000,330300,330381,普洛斯瑞安物流园,2,120.579347,27.769324,30,0,\N,2024-04-01 13:42:36
```
- 空数据不能直接展示null或则空字符，需要用 `\N` 字符表示
- 如果使用`,`作为列分隔符，那么字段串中则不能包含`,`字符，否则会影响数据导入
- 字符串不能使用单引号或则双引号包裹，否则会被认为是一个字符
  - 3.0后的版本可以通过`enclose = "\""`过滤掉某些字符来避免（验证版本3.2.6）。具体参考[BROKER_LOAD](https://docs.starrocks.io/zh/docs/sql-reference/sql-statements/data-manipulation/BROKER_LOAD/)

#### 从本地导入到starrocks
```sql
LOAD LABEL smart_transport_warehouse.label_local
(
    DATA INFILE("file:///Users/pengcheng/Downloads/datax/bin/business_address_bak.cvs")
    INTO TABLE business_address_bak
    COLUMNS TERMINATED BY ","
    FORMAT AS CSV
    (
    enclose = "\""
    )
    (id,business_type,business_id,province,city,district,address,address_type,lon,lat,sort,delete_flag,null_work,create_time)

)
WITH BROKER "broker_name_example"
PROPERTIES
(
    "timeout" = "3600"
);
```

#### 从obs导入到starrocks
注意：官网说明2.5版本之后不需要手动指定<broker_name>，但是根据验证还是需要指定broker_name才能生效。WITH BROKER <broker_name>
```sql
LOAD LABEL smart_transport_warehouse.label_brokerloadtest_601
(
    DATA INFILE("obs://data-archive-18262632337/input/business_address_bak.cvs")
    INTO TABLE business_address_bak
    COLUMNS TERMINATED BY ","
    (id,business_type,business_id,province,city,district,address,address_type,lon,lat,sort,delete_flag,null_work,create_time)
)
WITH BROKER "broker_name_example"
(
    "fs.obs.access.key" = "",
    "fs.obs.secret.key" = "",
    "fs.obs.endpoint" = "obs.cn-east-3.myhuaweicloud.com"
)
PROPERTIES
(
    "timeout" = "3600"
);
```

### 部署sr-3.2.6单机版本(测试)

#### 准备文件
```
wget https://releases.starrocks.io/starrocks/StarRocks-3.2.6.tar.gz
tar -zxvf StarRocks-3.2.6
mv ./StarRocks-3.2.6 /opt/module/starrocks
```

#### 部署fe实例
mkdir /opt/meta
vim /opt/module/starrocks/fe/conf/fe.conf
```shell
meta_dir = /opt/meta
priority_networks = 172.29.30.145 # 可以忽略
```

/opt/module/starrocks/fe/bin/start_fe.sh --daemon
ps -ef | grep StarRocksFE

mysql -h172.29.30.145 -P9030 -uroot
`show frontends\G` 命令查看fe状态，`Alive: true`则表示节点状态正常。

#### 部署be实例
mkdir /opt/storage
vim /opt/module/starrocks/be/conf/be.conf
```shell
storage_root_path = /opt/storage
priority_networks = 172.29.30.145 # 可以忽略
```

/opt/module/starrocks/be/bin/start_be.sh --daemon
ps -ef | grep starrocks_be

mysql -h172.29.30.145 -P9030 -uroot
`show backends\G` 命令查看be状态，`Alive: true`则表示节点状态正常。

需要将be节点加入到集群，否则`show backends\G`命令查询不到节点。
```sql
mysql> alter system add backend '172.29.30.145:9050'; -- 添加be节点
mysql> alter system dropp backend '172.29.30.145:9050'; -- 移除be节点
```

#### 部署broker load实例
检查Broker 节点的 HDFS Thrift RPC 端口（`broker_ipc_port`，默认8000）被占用，如果占用需要修改可用端口。</br>
vim /opt/module/starrocks/apache_hdfs_broker/conf/apache_hdfs_broker.conf

/opt/module/starrocks/apache_hdfs_broker/bin/start_broker.sh --daemon

mysql -h172.29.30.145 -P9030 -uroot
`SHOW PROC "/brokers"\G`命令查看broker状态，`Alive: true`则表示节点状态正常。
```sql
mysql> ALTER SYSTEM ADD BROKER broker_name_example "172.29.30.145:8000";
```

### 参考资料
- [sr文件导入-broker load](https://blog.csdn.net/vv5559999/article/details/138312972)

