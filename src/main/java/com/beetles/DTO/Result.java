package com.beetles.DTO;

import lombok.Data;

@Data
public class Result<T> {
    private static final Integer HTTP_OK = 200;
    private static final Integer HTTP_ERROR = 500;
    private static final Integer HTTP_UNAUTH = 401;



    private Integer code;
    private String msg;
    private T data;

    // 链式编程：方法返回的数据类型时当前类本身
    public Result<T> success(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }
    public Result<T> success(String msg) {
        this.success(HTTP_OK, msg);
        return this;
    }
    public Result<T> success() {
        this.success("操作成功");
        return this;
    }

    public Result<T> error(String msg) {
        this.success(HTTP_ERROR, msg);
        return this;
    }
    public Result<T> error() {
        this.error("操作失败");
        return this;
    }

    public Result<T> unAuth(String msg) {
        this.success(HTTP_UNAUTH, msg);
        return this;
    }
    public Result<T> unAuth() {
        this.error("操作失败");
        return this;
    }

    public Result<T> put(T data) {
        this.data = data;
        return this;
    }
}
