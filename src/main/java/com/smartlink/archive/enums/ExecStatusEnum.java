package com.smartlink.archive.enums;

/**
 * 运行的状态<br/>
 *
 * Created by pengcheng on 2024/5/6.
 */
public enum ExecStatusEnum {

    /**
     * 初始状态
     */
    INITIAL,

    /**
     * 执行中
     */
    RUNNING,

    /**
     * 执行失败
     */
    ERROR,

    /**
     * 执行成功
     */
    SUCCESS,

    /**
     * 等待超时
     */
    WAIT_TIMEOUT,
    ;

}
