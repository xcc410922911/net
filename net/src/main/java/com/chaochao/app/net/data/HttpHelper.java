package com.chaochao.app.net.data;

import android.content.Context;

import com.chaochao.app.net.data.interceptor.CacheControlInterceptor;
import com.chaochao.app.net.data.interceptor.LogInterceptor;
import com.chaochao.app.net.data.ssl.CustomTrustManager;
import com.chaochao.app.net.data.util.AppUtil;

import java.io.File;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

public class HttpHelper {
    private static final int DEFAULT_TIMEOUT = 30;
    private HashMap<String, Object> mServiceMap;
    private Context mContext;
    private static HttpHelper sHelper;
    private static Retrofit sRetrofit;
//    private Gson gson = new GsonBuilder().setLenient().create();

    public static HttpHelper getInstance() {
        if (sHelper == null) {
            synchronized (HttpHelper.class) {
                if (sHelper == null) {
                    sHelper = new HttpHelper(AppUtil.getApplicationByReflect());
                }
            }
        }
        return sHelper;
    }

    private HttpHelper(Context context) {
        //Map used to store RetrofitService
        mServiceMap = new HashMap<>();
        this.mContext = context;
    }

    public void initApi(String baseUrl) {
        if (sRetrofit != null) {
            return;
        }
        sRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getClient())
                .build();
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

    private OkHttpClient getClient() {
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
        return httpClient.build();
    }

    private static SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new CustomTrustManager()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private <S> S createApi(Class<S> serviceClass) {
        if (sRetrofit == null) {
            throw new RuntimeException("please invoke HttpHelper.getInstance().initApi(baseUrl);");
        }
        return sRetrofit.create(serviceClass);
    }

}
