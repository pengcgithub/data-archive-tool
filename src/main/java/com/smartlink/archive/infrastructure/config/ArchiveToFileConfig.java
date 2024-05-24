package com.smartlink.archive.infrastructure.config;

import lombok.Data;

/**
 *
 * Created by pengcheng on 2024/5/8.
 */
@Data
public class ArchiveToFileConfig {

    /**
     * 批量每次归档的数据数量
     * pt
     */
    private String batchSize;

    /**
     * 指定每个事务的大小（行数）
     * pt
     */
    private String txnSize;

    /**
     * 归档文件地址
     *
     * 默认为：/opt/archive
     */
    private String archiveFilePath;

    /**
     * TxtFileWriter写入前数据清理处理模式：
     *
     * truncate，写入前清理目录下一fileName前缀的所有文件。
     * append，写入前不做任何处理，DataX TxtFileWriter直接使用filename写入，并保证文件名不冲突。
     * nonConflict，如果目录下有fileName前缀的文件，直接报错。
     *
     */
    private String writeMode;

    /**
     * 读取的字段分隔符
     * 默认值：,
     */
    private String fieldDelimiter;

    /**
     * 文本压缩类型，默认不填写意味着没有压缩。支持压缩类型为zip、lzo、lzop、tgz、bzip2。
     * 必选：否
     * 默认值：无压缩
     */
    private String compress;

    /**
     * 读取文件的编码配置。
     */
    private String encoding;

    /**
     * 日期类型的数据序列化到文件中时的格式，例如 "dateFormat": "yyyy-MM-dd"
     */
    private String dateFormat;

    /**
     * 文件写出的格式
     * datax = csv、text
     * pt-archive = csv、dump
     * 默认值：csv
     */
    private String fileFormat;

}
