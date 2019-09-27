package com.chaochao.app.components.net.bean;

import com.chaochao.app.net.data.base.BaseResponse;

/**
 * 类描述：
 * 创建时间：2019/9/27
 *
 * @author chaochao
 */
public class BaseData<T> implements BaseResponse<T> {

    private int code;
    private String message;
    private String errorMessage;
    private T bizContent;

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getBizContent() {
        return bizContent;
    }

    public void setBizContent(T bizContent) {
        this.bizContent = bizContent;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return message;
    }

    @Override
    public String getErrorMsg() {
        return errorMessage;
    }

    @Override
    public T getData() {
        return bizContent;
    }

    @Override
    public int getSuccessCode() {
        return 0;
    }
}
