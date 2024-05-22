package com.smartlink.archive.infrastructure.exception;

/**
 * 自定义业务异常类<br/>
 *
 * Created by pengcheng on 2024/4/30.
 */
public class ArchiveBizException extends BaseBizException {

    public ArchiveBizException(String errorMsg) {
        super(errorMsg);
    }

    public ArchiveBizException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public ArchiveBizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum);
    }

    public ArchiveBizException(String errorCode, String errorMsg, Object... arguments) {
        super(errorCode, errorMsg, arguments);
    }

    public ArchiveBizException(BaseErrorCodeEnum baseErrorCodeEnum, Object... arguments) {
        super(baseErrorCodeEnum, arguments);
    }
}