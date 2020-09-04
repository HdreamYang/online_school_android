package com.etiantian.onlineschoolandroid.modules.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.etiantian.lib_network.request.RequestParams;
import com.etiantian.lib_network.response_handler.NormalResponseCallBack;
import com.etiantian.onlineschoolandroid.R;
import com.etiantian.onlineschoolandroid.api.NetworkManager;
import com.etiantian.onlineschoolandroid.entrance.TabBarNavigationActivity;
import com.etiantian.onlineschoolandroid.model.LoginModel;
import com.etiantian.onlineschoolandroid.entrance.BaseActivity;
import com.etiantian.onlineschoolandroid.tools.SharedPreferencesManager;

public class LoginActivity extends BaseActivity implements CompoundButton.OnClickListener {

    private Button loginButton;
    private String canBack;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    ///
    /// @description 初始化View
    /// @param 
    /// @return 
    /// @author waitwalker
    /// @time 2020/9/2 2:11 PM
    ///
    private void initView() {
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        textView = findViewById(R.id.textView);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String back = intent.getStringExtra("back");
            if (back != null) {
                canBack = back;
            }
        }
    }

    ///
    /// @description 监听点击事件
    /// @param 
    /// @return 
    /// @author waitwalker
    /// @time 2020/9/2 2:11 PM
    ///
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                loginAction();
                break;
        }
    }

    /// 登录按钮点击事件
    private void loginAction() {
        RequestParams map =new RequestParams();
        map.put("username","18600000001");
        map.put("password","a11111");
        NetworkManager.login((RequestParams) map, new NormalResponseCallBack() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.d("1","响应成功");
                LoginModel loginModel = (LoginModel) responseObj;

                // 缓存数据
                SharedPreferencesManager.instance().putString("token", loginModel.getAccess_token());
                SharedPreferencesManager.instance().putLong("expiration", loginModel.getExpiration());

                // 跳转到首页
                navigateTo(TabBarNavigationActivity.class);
            }

            @Override
            public void onFailure(Object responseObj) {
                Log.d("1","响应失败");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    ///
    /// @description 处理安卓物理返回键
    /// @param
    /// @return
    /// @author waitwalker
    /// @time 2020/9/2 2:06 PM
    ///
    @Override
    public void onBackPressed() {
        if (canBack != null && canBack.equals("yes")) {
            canBack = null;
            super.onBackPressed();
        }
        return;
    }
}