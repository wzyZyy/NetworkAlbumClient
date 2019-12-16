package cn.zuel.wlyw.networkalbumclient.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bytedance.sdk.account.open.aweme.share.Share;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.base.Image;
import cn.zuel.wlyw.networkalbumclient.base.ImageAdapter;
import cn.zuel.wlyw.networkalbumclient.base.ShareImageAdapter;
import cn.zuel.wlyw.networkalbumclient.config.MainConfig;
import cn.zuel.wlyw.networkalbumclient.kit.ImageKit;
import cz.msebera.android.httpclient.Header;

public class ShareImageActivity extends BaseActivity {
    private static final String TAG = "ShareImageActivity";
    // 保存收到的图片的信息
    private List<Image> imageList = new ArrayList<>();
    // 当前图片所属的相册id
    private int a_id;

    /**
     * 启动该活动的接口
     *
     * @param context 活动启动方
     * @param a_id    相册id
     */
    public static void actionStart(Context context, int a_id) {
        Intent intent = new Intent(context, ShareImageActivity.class);
        intent.putExtra("a_id", a_id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image);

        Intent intent = getIntent();
        a_id = intent.getIntExtra("a_id", 0);
        // 获取图片
        getImages(a_id);
    }

    /**
     * 取得列表后加载RecycleView
     */
    private void setRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recycle_view_image2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShareImageActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        ShareImageAdapter imageAdapter = new ShareImageAdapter(imageList, ShareImageActivity.this);
        recyclerView.setAdapter(imageAdapter);
    }

    public void previewImage(String i_path) {
        // 启动活动PreviewActivity
        PreviewActivity.actionStart(ShareImageActivity.this, i_path);
    }
    /**
     * 查看相册中的照片
     *
     * @param a_id 相册id
     */
    public void getImages(int a_id) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("a_id", a_id);
        client.post(MainConfig.GET_IMAGES_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ShareImageActivity.this, "网络错误，获取图片失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "获取相册图片失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "成功获取相册中的图片，服务器的响应结果: " + responseString);

                String data = "";
                String resultCode = "";
                String resultDesc = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    data = jsonObject.getString("data");
                    resultCode = jsonObject.getString("resultCode");
                    resultDesc = jsonObject.getString("resultDesc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(ShareImageActivity.this, resultDesc, Toast.LENGTH_SHORT).show();
                if (resultCode.equals("6021")) {
                    imageList = JSON.parseArray(data, Image.class);
                    Log.d(TAG, "onSuccess: " + imageList);
                    setRecycleView();
                }
            }
        });
    }
}
