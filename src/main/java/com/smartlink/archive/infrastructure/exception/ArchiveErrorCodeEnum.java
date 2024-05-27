package com.smartlink.archive.infrastructure.exception;

/**
 * 异常业务枚举错误码
 * 前2位:微服务code、中2位:功能模块code、末2位:递增具体异常<br/>
 *
 * Created by pengcheng on 2024/4/30.
 */
public enum ArchiveErrorCodeEnum implements BaseErrorCodeEnum {

    ERROR_POST_PARAM("400", "请求参数异常"),
    PATH_NOT_FOUND("404", "页面没有找到"),
    ERROR_POST_METHOD("405", "请求方法异常"),
    QPS_LIMIT_ERROR("406", "限流异常"),
    FAILED("500", "系统异常"),

    /**
     * +--------+---------+-------+
     * | first  | middle  | last  |
     * +========+=========+=======+
     * | 10     | 01      | 00    |
     * +--------+---------+-------+
     */
    ARCHIVE_CONFIG_NOT_EXIST_ERROR("100100", "归档配置不存在"),

    ;

    private String errorCode;

    private String errorMsg;

    ArchiveErrorCodeEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}