package cn.zuel.wlyw.networkalbumclient.activity;

import android.content.Context;
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

import androidx.annotation.Nullable;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.config.ServerConstantConfig;
import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends BaseActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
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

        // 为注册按钮设置监听事件
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "进行注册", Toast.LENGTH_SHORT).show();
                register(userAccountInput.getText().toString(), userPasswordInput.getText().toString());
            }
        });

    }

    /**
     * 用户注册
     *
     * @param userAccount
     * @param userPassword
     */
    public void register(String userAccount, String userPassword) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.add("u_phone", userAccount);
        requestParams.add("u_pwd", userPassword);

        asyncHttpClient.post(ServerConstantConfig.USER_REGISTER_URL, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("onFailure", responseString);
                Toast.makeText(RegisterActivity.this, "网络错误，连接服务器出错", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("onSuccess:", responseString);

                // 获取返回的状态码
                String resultCode = "";
                String resultDesc = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    resultCode = jsonObject.getString("resultCode");
                    resultDesc = jsonObject.getString("resultDesc");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegisterActivity.this, resultDesc, Toast.LENGTH_SHORT).show();
                if (resultCode.equals("5001")) {// 注册成功
                    LoginActivity.actionStart(RegisterActivity.this);
                }
            }
        });
    }
}
