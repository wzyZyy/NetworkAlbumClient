package cn.zuel.wlyw.networkalbumclient.activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.config.MainConfig;
import cz.msebera.android.httpclient.Header;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class PreviewActivity extends BaseActivity {

    private static final String TAG = "PreviewActivity";
    private String i_path;
    private ImageView imageView;

    /**
     * 启动该活动的接口
     *
     * @param context 活动启动方
     * @param i_path    照片路径
     */
    public static void actionStart(Context context, String i_path) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra("i_path", i_path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        imageView = findViewById(R.id.previewImage);
        Intent intent = getIntent();
        i_path = intent.getStringExtra("i_path");
        // 重新显示图片
        previewImage();
    }

    /**
     * 大图预览图片
     */
    public void previewImage() {
        Glide.with(this)
                .load(MainConfig.REQUEST_URL + i_path)
                .placeholder(R.drawable.loading)
                .into(imageView);
    }
}

