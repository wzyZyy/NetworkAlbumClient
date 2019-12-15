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
import cn.zuel.wlyw.networkalbumclient.config.MainConfig;
import cn.zuel.wlyw.networkalbumclient.kit.ImageKit;
import cz.msebera.android.httpclient.Header;

public class ImageActivity extends BaseActivity {
    private static final String TAG = "ImageActivity";
    // 请求码
    private static final int TAKE_PHOTO_REQUEST_CODE = 120;
    private static final int PICTURE_REQUEST_CODE = 911;
    // 拍照上传图片临时路径
    private String currentImageCachePath;
    // 从相册选择图片上传
    private Uri currentTakePhotoUri;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 添加上传图片的菜单项
        getMenuInflater().inflate(R.menu.upload_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 响应菜单项的选择结果
        switch (item.getItemId()) {
            case R.id.addImageFromDCIM:
                Toast.makeText(this, "从相册选择照片", Toast.LENGTH_SHORT).show();
                chooseImageFromDCIM();
                break;
            case R.id.addImageFromCamera:
                Toast.makeText(this, "选择拍照上传", Toast.LENGTH_SHORT).show();
                chooseImageFromCamera();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 从相册选取图片
     */
    private void chooseImageFromDCIM() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICTURE_REQUEST_CODE);
    }

    /**
     * 从相机选择拍照上传
     */
    private void chooseImageFromCamera() {
        Log.d(TAG, "chooseImageFromCamera: 方法进入");
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(getExternalCacheDir(),
                "output_image.jpg");
        Log.d(TAG, "chooseImageFromCamera: 路径是：----->" + outputImage.getAbsolutePath());
        currentImageCachePath = outputImage.getAbsolutePath();
        Log.d(TAG, "chooseImageFromCamera: 创建File对象开始");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "chooseImageFromCamera: 创建File完成");
        if (Build.VERSION.SDK_INT >= 24) {
            currentTakePhotoUri = FileProvider.getUriForFile(ImageActivity.this,
                    "cn.zuel.wlyw.networkalbumclient.fileprovider", outputImage);
        } else {
            currentTakePhotoUri = Uri.fromFile(outputImage);
        }
        Log.d(TAG, "chooseImageFromCamera: 启动相机");
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentTakePhotoUri);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICTURE_REQUEST_CODE) {
                // 处理从相册选择上传的图片
                assert data != null;
                try {
                    openConfirmDialog(data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
                Log.d(TAG, "onActivityResult: 拍照成功");
                // 如果拍照成功，加载图片并识别
                try {
                    openConfirmDialog(currentTakePhotoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 用户确定与否对话框
     *
     * @param uri
     * @throws IOException
     */
    public void openConfirmDialog(final Uri uri) throws IOException {
        Log.d(TAG, "上传照片，打开用户交互对话框：" + uri);
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
                // 图片的绝对路径
                String path = ImageKit.getRealPathFromUri(ImageActivity.this, uri);
                if (path == null) {
                    // 拍照上传
                    path = currentImageCachePath;
                }
                Log.d(TAG, "用户确认上传图片: " + path);
                File file = new File(path);
                try {
                    // 上传图片到指定相册
                    ImageActivity.this.uploadImage(a_id, file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "用户取消上传图片");
            }
        });
        builder.show();
    }
    public void previewImage(String i_path) {
        // 启动活动PreviewActivity
       PreviewActivity.actionStart(ImageActivity.this, i_path);
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
                Toast.makeText(ImageActivity.this, "网络错误，获取图片失败", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ImageActivity.this, resultDesc, Toast.LENGTH_SHORT).show();
                if (resultCode.equals("6021")) {
                    imageList = JSON.parseArray(data, Image.class);
                    Log.d(TAG, "onSuccess: " + imageList);
                    setRecycleView();
                }
            }
        });
    }

    /**
     * 上传图片文件到指定相册
     *
     * @param a_id      相册id
     * @param imageFile 上传的图片文件
     * @throws FileNotFoundException
     */
    public void uploadImage(final int a_id, File imageFile) throws FileNotFoundException {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("a_id", a_id);
        params.put("file", imageFile, "Content-Type");
        client.post(MainConfig.UPLOAD_IMAGE_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ImageActivity.this, "网络错误，上传图片失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "上传图片失败，与服务器连接错误");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "成功上传图片，服务器响应结果：" + responseString);
                String resultCode = "";
                String resultDesc = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    resultCode = jsonObject.getString("resultCode");
                    resultDesc = jsonObject.getString("resultDesc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(ImageActivity.this, resultDesc, Toast.LENGTH_SHORT).show();
                if (resultCode.equals("6016")) {
                    Toast.makeText(ImageActivity.this, "上传图片成功", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "上传图片成功");
                    // 刷新
                    getImages(a_id);
                    setRecycleView();
                }
            }
        });
    }
}
