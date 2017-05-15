package com.imooc.imooc_res02;

import android.app.Application;

import com.imooc.imooc_res02.utils.SPUtils;
import com.imooc.imooc_res02.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import okhttp3.OkHttpClient;

import java.text.CollationKey;
import java.util.concurrent.TimeUnit;


/**
 * Created by Gln on 2017/4/28.
 */
public class ResApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        T.init(this);
        SPUtils.init(this, "sp_user.pref");

        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, java.util.concurrent.TimeUnit.MILLISECONDS)
                .readTimeout(10000L, java.util.concurrent.TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .build();
        OkHttpUtils.initClient(okHttpClient);

    }
}
