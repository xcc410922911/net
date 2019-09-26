package com.chaochao.app.components;

import android.app.Application;

import com.chaochao.app.net.data.HttpHelper;

/**
 * 类描述：
 * 创建时间：2019/9/26
 *
 * @author chaochao
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpHelper.getInstance().initApi("http://10.20.1.0:8848");
    }
}
