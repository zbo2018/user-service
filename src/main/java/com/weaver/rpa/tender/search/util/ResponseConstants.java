package com.weaver.rpa.tender.search.util;

/**
 * 系统内错误码常量
 *
 * @author yyliu
 * @version 1.0.0
 * @since 2019/2/18 17:46
 **/
public enum ResponseConstants {
    SUCCESS(800, "操作成功"),
    ERROR_LOGIN(801, "登录失败"),
    ERROR_REGISTER(802, "注册失败"),
    ERROR_ADD(803, "新增失败"),
    ERROR_UPDATE(804, "更新失败"),
    ERROR_DELETE(805, "删除失败"),
    ERROR_OPERATE(806, "操作失败"),
    PARAMETER_IS_LOSE(807, "请求参数不完整"),
    PARAMETER_NO_PATTERN(808, "参数不合法"),
    REQUEST_NO_EXIST(809, "请求对象不存在"),
    REQUEST_EXIST(810, "请求对象已存在"),
    LOGIN_NULL_ACCOUNT_PW(811, "登录账号密码不能为空"),
    LOGIN_ERROR_PASSWORD(812, "登录密码不正确"),
    LOGIN_USER_NOT_EXIST(813, "登录用户不存在"),
    LOGIN_USER_INVALID(814, "当前用户已失效，无法登录"),
    USER_REST_NULL_PW(815, "新密码、旧密码不能为空"),
    USER_REST_NULL_ID(816, "参数ID为空"),
    USER_REST_ERROR_PW(817, "当前密码输入错误"),
    USER_REST_NO_EXIST(818, "重置密码失败, 该用户不存在"),
    FILE_NULL_CONTENT(819, "文件内容为空"),
    ;
    private String message;
    private Integer code;

    ResponseConstants(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
