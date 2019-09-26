package com.chaochao.app.net.data;

import android.content.Context;

import com.chaochao.app.net.data.interceptor.CacheControlInterceptor;
import com.chaochao.app.net.data.interceptor.LogInterceptor;
import com.chaochao.app.net.data.ssl.CustomTrustManager;

import java.io.File;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpHelper {
    private static final int DEFAULT_TIMEOUT = 30;
    private HashMap<String, Object> mServiceMap;
    private Context mContext;
//    private Gson gson = new GsonBuilder().setLenient().create();


    public HttpHelper(Context context) {
        //Map used to store RetrofitService
        mServiceMap = new HashMap<>();
        this.mContext = context;
    }


    @SuppressWarnings("unchecked")
    public <S> S getApi(Class<S> serviceClass) {
        if (mServiceMap.containsKey(serviceClass.getName())) {
            return (S) mServiceMap.get(serviceClass.getName());
        } else {
            Object obj = createApi(serviceClass);
            mServiceMap.put(serviceClass.getName(), obj);
            return (S) obj;
        }
    }

    @SuppressWarnings("unchecked")
    public <S> S getApi(Class<S> serviceClass, OkHttpClient client) {
        if (mServiceMap.containsKey(serviceClass.getName())) {
            return (S) mServiceMap.get(serviceClass.getName());
        } else {
            Object obj = createApi(serviceClass, client);
            mServiceMap.put(serviceClass.getName(), obj);
            return (S) obj;
        }
    }

    private <S> S createApi(Class<S> serviceClass) {
//        private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        //custom OkHttp
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //time our
        httpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //cache
        File httpCacheDirectory = new File(mContext.getCacheDir(), "OkHttpCache");
        httpClient.cache(new Cache(httpCacheDirectory, 50 * 1024 * 1024));
        //Interceptor
        httpClient.addNetworkInterceptor(new LogInterceptor());
        httpClient.addInterceptor(new CacheControlInterceptor());
        //retry when fail
        httpClient.retryOnConnectionFailure(true);

        //todo SSL证书
        httpClient.sslSocketFactory(getSSLSocketFactory());
        httpClient.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        return createApi(serviceClass, httpClient.build());
    }

    public static SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new CustomTrustManager()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    private <S> S createApi(Class<S> serviceClass, OkHttpClient client) {
        String end_point = "";
        try {
            Field field1 = serviceClass.getField("end_point");
            end_point = (String) field1.get(serviceClass);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.getMessage();
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(end_point)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit.create(serviceClass);
    }

}
