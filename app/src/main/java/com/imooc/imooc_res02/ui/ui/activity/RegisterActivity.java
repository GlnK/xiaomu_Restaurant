package com.imooc.imooc_res02.ui.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.imooc.imooc_res02.R;
import com.imooc.imooc_res02.bean.User;
import com.imooc.imooc_res02.biz.UserBiz;
import com.imooc.imooc_res02.net.CommonCallback;
import com.imooc.imooc_res02.utils.T;

/**
 * Created by Gln on 2017/4/26.
 */
public class RegisterActivity extends BaseActivity {
    private EditText mEtUsername, mEtPassword, mEtRePassword;
    private Button mBtnRegister;
    private UserBiz mUserBiz = new UserBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUpToolbar();
        initView();
        initEvent();
        setTitle("注册");
    }


    private void initEvent() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();
                final String repassword = mEtRePassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    T.showToast(" 账号或者密码不能为空");
                    return;
                }
                if (!password.equals(repassword)) {
                    T.showToast(" 两次输入的密码不一致");
                    return;
                }

                startLoadingProgress();

                mUserBiz.register(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response) {
                        stopLoadingProgress();
                        T.showToast("注册成功,用户名为： " + response.getUsername());
                        LoginActivity.launch(RegisterActivity.this, response.getUsername(), response.getPassword());
                        finish();
                    }
                });
            }
        });
    }

    private void initView() {
        mBtnRegister = (Button) findViewById(R.id.id_btn_register);
        mEtPassword = (EditText) findViewById(R.id.id_et_password);
        mEtRePassword = (EditText) findViewById(R.id.id_et_repassword);
        mEtUsername = (EditText) findViewById(R.id.id_et_username);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestory();
    }
}
