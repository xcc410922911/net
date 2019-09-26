package com.chaochao.app.net.data.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 类描述：
 * 创建时间：2019/9/26
 *
 * @author chaochao
 */
public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.i("HttpHelper", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        Log.i("HttpHelper", String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        return response;

        // log Response Body
//            if(BuildConfig.DEBUG) {
//                String responseBody = response.body().string();
//                Log.v("HttpHelper", String.format("Received response for %s in %.1fms%n%s%n%s",
//                        response.request().url(), (t2 - t1) / 1e6d, response.headers(), responseBody));
//                return response.newBuilder()
//                        .body(ResponseBody.create(response.body().contentType(), responseBody))
//                        .build();
//            } else {
//                Log.v("HttpHelper", String.format("Received response for %s in %.1fms%n%s",
//                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//                return response;
//            }
    }
}
