package com.chaochao.app.components.api;

import com.chaochao.app.components.api.bean.TestBean;

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
    Observable<BaseResponse<TestBean>> test();
}
