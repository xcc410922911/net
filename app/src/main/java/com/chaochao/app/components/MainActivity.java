package com.chaochao.app.components;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.chaochao.app.components.net.ApiService;
import com.chaochao.app.components.net.bean.TestBean;
import com.chaochao.app.net.data.HttpHelper;
import com.chaochao.app.net.data.base.BaseObserver;
import com.chaochao.app.net.data.helper.RxHelper;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                net();
            }
        },2000);

    }

    private void net() {
        HttpHelper.getInstance().getApi(ApiService.class).test()
                .compose(RxHelper.observableIO2Main())
                .subscribe(new BaseObserver<TestBean>() {

                    @Override
                    public void onSuccess(TestBean testBean) {
                        Toast.makeText(MainActivity.this, testBean.getDescribe(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
