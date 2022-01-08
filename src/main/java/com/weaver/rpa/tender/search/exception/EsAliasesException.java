package com.weaver.rpa.tender.search.exception;

/**
 * es获取别名异常
 */
public class EsAliasesException extends Exception{

    private Integer code;
    private String message;

    public EsAliasesException(Integer code,String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
