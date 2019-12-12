package cn.zuel.wlyw.networkalbumclient.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.base.BaseActivity;
import cn.zuel.wlyw.networkalbumclient.request.HttpRequest;

public class RegisterActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 用户注册账号
        final EditText userAccountInput = (EditText) findViewById(R.id.register_input_user_account);
        // 用户注册密码
        final EditText userPasswordInput = (EditText) findViewById(R.id.register_input_user_password);
        // 注册按钮
        Button registerBtn = (Button) findViewById(R.id.register_btn);

        // 为登录按钮设置监听事件
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "进行注册", Toast.LENGTH_SHORT).show();
                HttpRequest.register(RegisterActivity.this, userAccountInput.getText().toString(), userPasswordInput.getText().toString());
            }
        });

    }
}
