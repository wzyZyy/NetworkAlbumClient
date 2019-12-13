package cn.zuel.wlyw.networkalbumclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.base.BaseActivity;
import cn.zuel.wlyw.networkalbumclient.config.MainConfig;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 用户登录账号
        final EditText userAccountInput = findViewById(R.id.login_input_user_account);
        // 用户登录密码
        final EditText userPasswordInput = findViewById(R.id.login_input_user_password);
        // 登录按钮
        Button loginBtn = findViewById(R.id.login_btn);
        // 去注册跳转链接
        EditText login_link_to_register = findViewById(R.id.login_link_to_register);

        // 为登录按钮设置监听事件
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "登录验证", Toast.LENGTH_SHORT).show();
                login(userAccountInput.getText().toString(), userPasswordInput.getText().toString());
            }
        });

        // 为跳转链接设置监听事件
        login_link_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "去注册", Toast.LENGTH_SHORT).show();
                // 启动活动 RegisterActivity
                RegisterActivity.actionStart(LoginActivity.this);
            }
        });
    }



    /**
     * 用户登录
     *
     * @param userAccount 用户手机号
     * @param userPassword 用户密码
     */
    public void login(String userAccount, String userPassword) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.add("u_phone", userAccount);
        requestParams.add("u_pwd", userPassword);

        asyncHttpClient.post(MainConfig.USER_LOGIN_URL, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("onFailure", responseString);
                Toast.makeText(LoginActivity.this, "连接服务器出错", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("onSuccess:", responseString);

                // 获取返回的状态码
                String resultCode = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    resultCode = jsonObject.getString("resultCode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (resultCode.equals("4000")) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    // 启动活动 IndexActivity
                    IndexActivity.actionStart(LoginActivity.this);
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败，账号或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
