package com.smartlink.archive.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "mysql")
public class DataArchiveProperties {

    /**
     * pt-Archiver地址
     */
    private String ptArchiverPath;

    /**
     * datax地址
     */
    private String dataxPath;

    /**
     * 归档账号
     */
    private String archiveUser;

    /**
     * 归档密码
     */
    private String archivePwd;

    /**
     * 归档模式 - 归档配置
     */
    private ArchiveConfig archiveConfig;

    /**
     * 归档模式 - 归档到文件配置
     */
    private ArchiveToFileConfig archiveToFileConfig;

    /**
     * 归档模式 - 只删除不归档配置
     */
    private DeleteConfig deleteConfig;

}
