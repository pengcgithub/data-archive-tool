package com.smartlink.archive.infrastructure.exception;

import java.text.MessageFormat;

/**
 * 基础自定义业务异常<br/>
 *
 * Created by pengcheng on 2024/4/30.
 */
public class BaseBizException extends RuntimeException {

    /**
     * 默认错误码
     */
    private static final String DEFAULT_ERROR_CODE = "-1";

    private String errorCode;

    private String errorMsg;

    public BaseBizException(String errorMsg) {
        super(errorMsg);
        this.errorCode = DEFAULT_ERROR_CODE;
        this.errorMsg = errorMsg;
    }

    public BaseBizException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseBizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum.getErrorMsg());
        this.errorCode = baseErrorCodeEnum.getErrorCode();
        this.errorMsg = baseErrorCodeEnum.getErrorMsg();
    }

    public BaseBizException(String errorCode, String errorMsg, Object... arguments) {
        super(MessageFormat.format(errorMsg, arguments));
        this.errorCode = errorCode;
        this.errorMsg = MessageFormat.format(errorMsg, arguments);
    }

    public BaseBizException(BaseErrorCodeEnum baseErrorCodeEnum, Object... arguments) {
        super(MessageFormat.format(baseErrorCodeEnum.getErrorMsg(), arguments));
        this.errorCode = baseErrorCodeEnum.getErrorCode();
        this.errorMsg = MessageFormat.format(baseErrorCodeEnum.getErrorMsg(), arguments);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}