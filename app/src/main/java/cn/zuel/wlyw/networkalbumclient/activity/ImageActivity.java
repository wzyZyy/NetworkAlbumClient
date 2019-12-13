package cn.zuel.wlyw.networkalbumclient.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.base.BaseActivity;
import cn.zuel.wlyw.networkalbumclient.base.Image;
import cn.zuel.wlyw.networkalbumclient.base.ImageAdapter;
import cn.zuel.wlyw.networkalbumclient.config.MainConfig;
import cz.msebera.android.httpclient.Header;

public class ImageActivity extends BaseActivity {

    private static final String TAG = "ImageActivity";
    // 请求码
    private static final int TAKE_PHOTO_REQUEST_CODE = 120;
    private static final int PICTURE_REQUEST_CODE = 911;
    private Uri currentTakePhotoUri;

    private List<Image> imageList = new ArrayList<>();    // 用于保存收到的图片的信息
    private int a_id;    // 保存当前图片所属的相册

    public static void actionStart(Context context, int a_id) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("a_id", a_id);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        a_id = intent.getIntExtra("a_id", 0);
        // 获取图片
        getImages(a_id);
    }

    public void getImages(int a_id) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("a_id", a_id);
        Log.d(TAG, "getImages: " + MainConfig.GET_IMAGES_URL);
        client.post(MainConfig.GET_IMAGES_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ImageActivity.this, "网络错误，查询图片失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: net error view image fail");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "onSuccess: view label success");
                Log.d(TAG, "onSuccess: " + responseString);

                String data = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    data = jsonObject.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imageList = JSON.parseArray(data, Image.class);
                Log.d(TAG, "onSuccess: " + imageList);
                setRecycleView();
            }
        });
    }

    /**
     * 取得列表后加载RecycleView
     */
    private void setRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recycle_view_image);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ImageActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        ImageAdapter imageAdapter = new ImageAdapter(imageList, ImageActivity.this);
        recyclerView.setAdapter(imageAdapter);
    }
}
