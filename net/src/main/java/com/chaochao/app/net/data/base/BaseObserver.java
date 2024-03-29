package com.chaochao.app.net.data.base;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.chaochao.app.net.data.util.RxExceptionUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 类描述：
 * 创建时间：2019/9/27
 *
 * @author chaochao
 */
public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private static final String TAG = "BaseObserver";

    @Override
    public void onSubscribe(Disposable d) {
        Log.e(TAG, "onSubscribe: ");
    }

    @Override
    public void onNext(BaseResponse<T> response) {
        //在这边对 基础数据 进行统一处理  举个例子：
        if (response.getCode() == response.getSuccessCode()) {
            onSuccess(response.getData());
        } else {
            onFailure(null, response.getErrorMsg());
        }
        Log.e(TAG, "onNext: " + JSON.toJSONString(response));
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "Throwable: " + e.getMessage());
        onFailure(e, RxExceptionUtil.exceptionHandler(e));
    }

    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete: ");
    }

    protected abstract void onSuccess(T t);

    protected void onFailure(Throwable e, String errorMsg){}
}
