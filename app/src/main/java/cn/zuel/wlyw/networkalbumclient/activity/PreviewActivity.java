package cn.zuel.wlyw.networkalbumclient.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.config.MainConfig;

public class PreviewActivity extends BaseActivity {

    private static final String TAG = "PreviewActivity";
    // 待显示图片的路径
    private String i_path;
    // 显示图片视图
    private ImageView imageView;

    /**
     * 启动该活动的接口
     *
     * @param context 活动启动方
     * @param i_path  照片路径
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
        // 获取显示图片的图片视图
        imageView = findViewById(R.id.previewImage);
        // 获取图片路径
        Intent intent = getIntent();
        i_path = intent.getStringExtra("i_path");
        // 预览图片
        previewImage();
        // 长按响应事件
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PreviewActivity.this);
                builder.setItems(new String[]{getResources().getString(R.string.save_picture), getResources().getString(R.string.share_picture)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:// 下载图片到本地相册
                                imageView.setDrawingCacheEnabled(true);
                                Bitmap bitmap = imageView.getDrawingCache();
                                if (bitmap != null) {
                                    //这句代码时调用保存的核心
                                    new SaveImageTask().execute(bitmap);
                                }
                                break;
                            case 1:// 分享图片到社区
                                showShare();
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    //点击长按保存图片
    class SaveImageTask extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected String doInBackground(Bitmap... params) {
            String result = getResources().getString(R.string.save_picture_failed);
            try {
                String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File(sdcard + "/Pictures");
                if (!file.exists()) {
                    file.mkdirs();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File imageFile = new File(file.getAbsolutePath(), fileName);
                FileOutputStream outStream;
                outStream = new FileOutputStream(imageFile);
                Bitmap image = params[0];
                image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
                //扫描保存的图片到系统图库
                MediaScannerConnection.scanFile(PreviewActivity.this, new String[]{imageFile.getAbsolutePath()}, null, null);
                result = getResources().getString(R.string.save_picture_success, file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(PreviewActivity.this, result, Toast.LENGTH_LONG).show();
            imageView.setDrawingCacheEnabled(false);
        }
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

    /**
     * 分享图片到社区
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.share));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("分享来自网络相册的图片");
        // imagePath是图片的本地路径，确保SDcard下面存在此张图片
//        oks.setImagePath("/sdcard/test.jpg");
        oks.setImagePath(MainConfig.REQUEST_URL + i_path);
        // url在微信、Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }
}

