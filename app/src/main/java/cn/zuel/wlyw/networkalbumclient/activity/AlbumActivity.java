package cn.zuel.wlyw.networkalbumclient.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.angmarch.views.NiceSpinner;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.config.MainConfig;
import cz.msebera.android.httpclient.Header;

public class AlbumActivity extends BaseActivity {
    private static final String TAG = "AlbumActivity";
    // 用户id
    private int a_u_id;
    // 相册权限
    private int a_auth;
    // 相册主题
    private int a_t_id;
    // 相册名称
    private String a_name;
    // 相册描述
    private String a_desc;

    private NiceSpinner albumPermission;
    private NiceSpinner albumTheme;
    private Button create_album_btn;
    private EditText albumName;
    private EditText albumDesc;
    List<String> permissionData = new LinkedList<>(Arrays.asList("私密", "共享"));
    List<String> themeData = new LinkedList<>(Arrays.asList("普通", "多人", "亲子", "旅游", "情侣"));

    /**
     * @param context
     * @param u_id
     */
    public static void actionStart(Context context, int u_id) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("u_id", u_id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        albumName = findViewById(R.id.album_name);
        albumDesc = findViewById(R.id.album_desc);

        albumPermission = findViewById(R.id.album_permission);
        albumPermission.attachDataSource(permissionData);
        albumPermission.setBackgroundResource(R.drawable.textview_round_border);
        albumPermission.setTextColor(Color.WHITE);
        albumPermission.setTextSize(13);

        albumTheme = findViewById(R.id.album_theme);
        albumTheme.attachDataSource(themeData);
        albumTheme.setBackgroundResource(R.drawable.textview_round_border);
        albumTheme.setTextColor(Color.WHITE);
        albumTheme.setTextSize(13);
        // 确认创建按钮
        create_album_btn = findViewById(R.id.create_album_btn);

        albumPermission.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(AlbumActivity.this, "私密", Toast.LENGTH_SHORT).show();
                        a_auth = 0;
                        break;
                    case 1:
                        Toast.makeText(AlbumActivity.this, "共享", Toast.LENGTH_SHORT).show();
                        a_auth = 1;
                        break;
                    default:
                        break;
                }
            }
        });
        albumTheme.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Toast.makeText(AlbumActivity.this, "普通", Toast.LENGTH_SHORT).show();
                        a_t_id = 5;
                        break;
                    case 1:
                        Toast.makeText(AlbumActivity.this, "多人", Toast.LENGTH_SHORT).show();
                        a_t_id = 6;
                        break;
                    case 2:
                        Toast.makeText(AlbumActivity.this, "亲子", Toast.LENGTH_SHORT).show();
                        a_t_id = 7;
                        break;
                    case 3:
                        Toast.makeText(AlbumActivity.this, "旅游", Toast.LENGTH_SHORT).show();
                        a_t_id = 8;
                        break;
                    case 4:
                        Toast.makeText(AlbumActivity.this, "情侣", Toast.LENGTH_SHORT).show();
                        a_t_id = 9;
                        break;
                    default:
                        break;
                }
            }
        });
        // 获取用户的id
        Intent intent = getIntent();
        a_u_id = intent.getIntExtra("u_id", 0);

        // 为按钮设置监听事件
        create_album_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a_name = albumName.getText().toString();
                a_desc = albumDesc.getText().toString();
                createAlbum();
            }
        });
    }
    public void createAlbum() {
        AsyncHttpClient client = new AsyncHttpClient();
        // 请求的参数
        RequestParams requestParams = new RequestParams();
        requestParams.put("a_u_id", a_u_id);
        requestParams.put("a_name", a_name);
        requestParams.put("a_auth", a_auth);
        requestParams.put("a_t_id", a_t_id);
        requestParams.put("a_desc", a_desc);

        Log.d(TAG, "createAlbum: a_t_id" + a_t_id);
        client.post(MainConfig.ALBUM_CREATE_URL, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(AlbumActivity.this, "网络错误，新建相册失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "新建相册失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "成功新建相册，服务器响应结果：" + responseString);
                String resultCode = "";
                String resultDesc = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    resultCode = jsonObject.getString("resultCode");
                    resultDesc = jsonObject.getString("resultDesc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(AlbumActivity.this, resultDesc, Toast.LENGTH_SHORT).show();
                if (resultCode.equals("5006")) {
                    // 创建相册成功
                    finish();
                }
            }
        });
    }
}
