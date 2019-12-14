package cn.zuel.wlyw.networkalbumclient.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.base.Image;
import cn.zuel.wlyw.networkalbumclient.base.ImageAdapter;
import cn.zuel.wlyw.networkalbumclient.config.MainConfig;
import cn.zuel.wlyw.networkalbumclient.kit.ImageKit;
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
        Log.d(TAG, "onCreate: end");
    }

    /**
     * 在右上角添加按钮
     *
     * @param menu 自定义的图形
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addImageFromDCIM:
                Toast.makeText(this, "从相册选择照片", Toast.LENGTH_SHORT).show();
                chooseImageFromDCIM();
                break;
            case R.id.addImageFromCamera:
                Toast.makeText(this, "选择拍照上传", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseImageFromDCIM() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICTURE_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICTURE_REQUEST_CODE) {
                // 处理选择的图片
                assert data != null;
//                handleInputPhoto(data.getData());
                try {
                    openConfirmDialog(data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
                // 如果拍照成功，加载图片并识别
//                handleInputPhoto(currentTakePhotoUri);
                try {
                    openConfirmDialog(currentTakePhotoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void openConfirmDialog(final Uri uri) throws IOException {
        Log.d(TAG, "openConfirmDialog: " + uri);

        // 新建ImageView，在Dialog中预览要上传的图片
        final ImageView imageView = new ImageView(this);
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        imageView.setImageBitmap(bitmap);

        // Dialog对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认上传");
        builder.setView(imageView);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ImageActivity.this, "你点了确认", Toast.LENGTH_SHORT).show();
                String path = ImageKit.getRealPathFromUri(ImageActivity.this, uri);
                Log.d(TAG, "openConfirmDialog: " + path);
                File file = new File(path);
                try {
                    ImageActivity.this.addImage(a_id, file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ImageActivity.this, "你点了取消", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
    /**
     * 查看相册中的照片
     *
     * @param a_id
     */
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
                // 打印结果
                Toast.makeText(ImageActivity.this, resultDesc, Toast.LENGTH_SHORT).show();
                if (resultCode.equals("6021")) {
                    imageList = JSON.parseArray(data, Image.class);
                    Log.d(TAG, "onSuccess: " + imageList);
                }
                setRecycleView();

            }
        });
    }

    public void addImage(final int a_id, File imageFile) throws FileNotFoundException {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("files", imageFile, "Content-Type");
        params.put("a_id", a_id);
        Log.d(TAG, "addImage: " + MainConfig.UPLOAD_IMAGE_URL);
        client.post(MainConfig.UPLOAD_IMAGE_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ImageActivity.this, "网络错误，添加图片失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: net error add image fail");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (responseString.equals("success")) {
                    Toast.makeText(ImageActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: add image success");
                    getImages(a_id);
                } else {
                    Toast.makeText(ImageActivity.this, "未知错误，添加图片失败", Toast.LENGTH_SHORT).show();
                }
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
