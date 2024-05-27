# Percona Toolkit 安装及使用

### 简介
mysql归档工具

### 安装Percona Toolkit

#### （1）下载Percona Toolkit:

[Percona-Toolkit-3.4.0.rpm](https://downloads.percona.com/downloads/percona-toolkit/3.4.0/binary/redhat/7/x86_64/percona-toolkit-3.4.0-3.el7.x86_64.rpm)

#### （2）安装前置依赖环境：

```
yum install -y perl-DBI perl-DBD-MySQL perl-Digest-MD5 perl-IO-Socket-SSL perl-TermReadKey
```

#### （3）配置SSL.pm策略（如果使用默认的SSL_VERIFY_NONE策略，会导致同步失败）

- 找到perl5的SSL.pm

```
cd /usr/share/perl5/vendor_perl/IO/Socket
```

- 编辑SSL.pm将 **SSL_VERIFY_NONE** 改成 **SSL_VERIFY_PEER**

```
vim SSL.pm

# global defaults
my %DEFAULT_SSL_ARGS = (
    。。。。。。
    SSL_verify_mode => SSL_VERIFY_PEER,
);
```

#### （4）安装Percona Toolkit:

- 安装Percona Toolkit
```
rpm -ivh percona-toolkit-3.4.0-3.el7.x86_64.rpm
```

- 检查pt-archiver是否安装成功
```
pt-archiver --version
```

- 查看pt-archiver安装目录
```
rpm -ql percona-toolkit
```

### 使用Percona Toolkit

```shell
pt-archiver \
--source h=127.0.0.1,u=root,p=xxx,P=3306,D=smart_transport,t=goods,A=utf8mb4 \
--progress 20000 --where "create_time >= '2024-02-01 00:00:00' and create_time <= '2024-05-01 00:00:00'"  --statistics \
--output-format=csv \
--limit=20000 --txn-size 500 --file '/opt/%D-%t-%Y-%m-%d-%H-%i-%s.csv' --no-delete --ask-pass
```

### 参考资料
- [https://docs.percona.com/percona-toolkit/pt-archiver.html](https://docs.percona.com/percona-toolkit/pt-archiver.html)
- [pt-archiver参数](https://www.yisu.com/jc/30741.html)