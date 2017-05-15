package com.imooc.imooc_res02.biz;

import com.imooc.imooc_res02.bean.User;
import com.imooc.imooc_res02.config.Config;
import com.imooc.imooc_res02.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Created by Gln on 2017/4/26.
 */
public class UserBiz {
    public void login(String username, String password, CommonCallback<User> commonCallback) {
        OkHttpUtils.post()
                .url(Config.baseUrl + "user_login")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(commonCallback);
    }

    public void register(String username, String password, CommonCallback<User> commonCallback) {
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "user_register")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(commonCallback);
    }
    public void onDestory(){
        OkHttpUtils.getInstance().cancelTag(this);

    }
}
