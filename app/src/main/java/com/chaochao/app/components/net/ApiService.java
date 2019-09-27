package com.chaochao.app.components.net;

import com.chaochao.app.components.net.bean.BaseData;
import com.chaochao.app.components.net.bean.TestBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 类描述：
 * 创建时间：2019/9/26
 *
 * @author chaochao
 */
public interface ApiService {

    @GET("app/orderDetail.json")
    Observable<BaseData<TestBean>> test();
}
