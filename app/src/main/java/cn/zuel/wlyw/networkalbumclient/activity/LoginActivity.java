package cn.zuel.wlyw.networkalbumclient.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.base.BaseActivity;
import cn.zuel.wlyw.networkalbumclient.request.HttpRequest;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 用户登录账号
        final EditText userAccountInput = (EditText) findViewById(R.id.login_input_user_account);
        // 用户登录密码
        final EditText userPasswordInput = (EditText) findViewById(R.id.login_input_user_password);
        // 登录按钮
        Button loginBtn = (Button) findViewById(R.id.login_btn);

        // 为登录按钮设置监听事件
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "登录验证", Toast.LENGTH_SHORT).show();
                HttpRequest.login(LoginActivity.this, userAccountInput.getText().toString(), userPasswordInput.getText().toString());
            }
        });
    }
}
