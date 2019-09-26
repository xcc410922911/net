package com.chaochao.app.components.api;

import retrofit2.http.GET;
import rx.Observable;

/**
 * 类描述：
 * 创建时间：2019/9/26
 *
 * @author chaochao
 */
public interface ApiService {

    @GET("app/orderDetail.json")
    Observable<String> test();
}
