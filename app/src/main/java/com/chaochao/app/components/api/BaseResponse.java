package com.chaochao.app.components.api;

/**
 * 类描述：
 * 创建时间：2019/9/27
 *
 * @author chaochao
 */
public class BaseResponse<T> {

    public static final int SUCCESS_CODE = 0;

    public int code;
    public String message;
    public String errorMsg;
    public T bizContent;
}
