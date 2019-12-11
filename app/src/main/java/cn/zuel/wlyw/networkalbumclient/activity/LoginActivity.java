package cn.zuel.wlyw.networkalbumclient.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 用户登录账号
        EditText userAccountInput = (EditText) findViewById(R.id.login_input_user_account);
        // 用户登录密码
        EditText userPasswordInput = (EditText) findViewById(R.id.login_input_user_password);
        // 登录按钮
        Button loginBtn = (Button) findViewById(R.id.login_btn);
    }
}
