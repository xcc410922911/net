package com.chaochao.app.components;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.chaochao.app.components.api.ApiService;
import com.chaochao.app.components.api.BaseObserver;
import com.chaochao.app.components.api.bean.TestBean;
import com.chaochao.app.net.data.HttpHelper;
import com.chaochao.app.net.data.helper.RxHelper;
import com.trello.rxlifecycle2.components.RxActivity;


public class MainActivity extends RxActivity {

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
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver<TestBean>() {

                    @Override
                    public void onSuccess(TestBean t) {
                        Toast.makeText(MainActivity.this, t.getDescribe(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }
}
