package com.imooc.imooc_res02.ui.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imooc.imooc_res02.R;
import com.imooc.imooc_res02.UserInfoHolder;
import com.imooc.imooc_res02.bean.User;
import com.imooc.imooc_res02.biz.UserBiz;
import com.imooc.imooc_res02.net.CommonCallback;
import com.imooc.imooc_res02.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

/**
 * Created by Gln on 2017/4/26.
 */
public class LoginActivity extends BaseActivity {

    private UserBiz mUserBiz = new UserBiz();

    private EditText mEtUsername, mEtPassword;
    private Button mBtnLogin;
    private TextView mTvRegister;

    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";

    @Override
    protected void onResume() {
        super.onResume();
        CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils
                .getInstance().getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
        initIntent(getIntent());
    }

    private void initEvent() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    T.showToast(" 账号或者密码不能为空");
                    return;
                }

                startLoadingProgress();

                mUserBiz.login(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response) {
                        stopLoadingProgress();
                        T.showToast("登陆成功");
                        //保存用户登录信息
                        UserInfoHolder.getInstance().setUser(response);
                        toOrderActivity();
                    }
                });
            }
        });
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegisterActivity();
            }
        });
    }


    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void toOrderActivity() {
        Intent intent = new Intent(LoginActivity.this, OrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void initView() {
        mBtnLogin = (Button) findViewById(R.id.id_btn_login);
        mTvRegister = (TextView) findViewById(R.id.id_tv_register);
        mEtPassword = (EditText) findViewById(R.id.id_et_password);
        mEtUsername = (EditText) findViewById(R.id.id_et_username);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        String username = intent.getStringExtra(KEY_USERNAME);
        String password = intent.getStringExtra(KEY_PASSWORD);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        }
        mEtUsername.setText(username);
        mEtPassword.setText(password);
    }

    public static void launch(Context context, String username, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KEY_USERNAME, username);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();mUserBiz.onDestory();
    }
}
