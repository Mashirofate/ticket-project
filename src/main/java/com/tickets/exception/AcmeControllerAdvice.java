package com.tickets.exception;

import com.tickets.dto.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class AcmeControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * 处理自定义异常
     *
     * @param b
     * @return
     */
    @ExceptionHandler(BizException.class)
    public ResponseResult handleBizException(BizException b) {
        return new ResponseResult(b.getCode(), b.getMsg());
    }


    /**
     * 其他未知异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseResult handleException(Exception ex) {
        ex.printStackTrace();
        return new ResponseResult().setMsg(ex.getMessage());
    }
}
