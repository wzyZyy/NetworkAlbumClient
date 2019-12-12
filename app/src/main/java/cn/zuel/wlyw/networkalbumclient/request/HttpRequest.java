package cn.zuel.wlyw.networkalbumclient.request;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cn.zuel.wlyw.networkalbumclient.config.MainConfig;
import cz.msebera.android.httpclient.Header;

public class HttpRequest {
    public static void login(final Context loginActivity, String userAccount, String userPassword) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.add("u_phone", userAccount);
        requestParams.add("u_pwd", userPassword);

        asyncHttpClient.post(MainConfig.USER_LOGIN_URL, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("onFailure", responseString);
                Toast.makeText(loginActivity, "连接服务器出错", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(loginActivity, "登录成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(loginActivity, "登录失败，账号或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
