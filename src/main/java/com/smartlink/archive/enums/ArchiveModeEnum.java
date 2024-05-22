package com.smartlink.archive.enums;

/**
 * 归档模式<br/>
 *
 * Created by pengcheng on 2024/5/6.
 */
public enum ArchiveModeEnum {

    /**
     * archive（归档）
     */
    ARCHIVE("--bulk-delete"),

    /**
     * delete(只删除不归档)
     */
    DELETE("--purge"),

    /**
     * archive-to-file(归档到文件)
     */
    ARCHIVE_TO_FILE("--file '${archiveFilePath}/%D-%t-%Y-%m-%d-%H-%i-%s'");

    private String cmd;

    ArchiveModeEnum(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }
}
