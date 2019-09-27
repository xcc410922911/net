package com.chaochao.app.net.data.base;

/**
 * 类描述：
 * 创建时间：2019/9/27
 *
 * @author chaochao
 */
public interface BaseResponse<T> {

    public abstract int getCode();

    public abstract String getMsg();

    public abstract String getErrorMsg();

    public abstract T getData();

    public abstract int getSuccessCode();
}
