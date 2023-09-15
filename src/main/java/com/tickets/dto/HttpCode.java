package com.tickets.dto;

/**
 * 响应状态码
 */
public enum HttpCode {
    SUCCESS(2000,"ok"),
    ERROR(4005,"orror"),
    TOKEN_ERROR(4015,"sigin error"),
    /**
     * 暂无权限
     */
    NOT_AUTHORIZED(4025,"not authorized")
    ;

    private Integer code;
    private String msg;

    HttpCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
