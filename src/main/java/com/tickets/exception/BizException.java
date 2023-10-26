package com.tickets.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 自定义业务处理异常
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=true)
public class BizException extends RuntimeException {

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 描述
     */
    private String msg;
    /**
     * 其它
     */
    private Object data;


}
