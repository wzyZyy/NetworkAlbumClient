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

import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.config.MainConfig;

public class PreviewActivity extends BaseActivity {

    private static final String TAG = "PreviewActivity";
    // 待显示图片的路径
    private String i_path;
    // 显示图片
    private ImageView imageView;
    //长按后显示的 Item
    final String[] items = new String[]{"保存图片"};
    //图片转成Bitmap数组
    final Bitmap[] bitmap = new Bitmap[1];

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
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PreviewActivity.this);
                builder.setItems(new String[]{getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imageView.setDrawingCacheEnabled(true);
                        Bitmap bitmap = imageView.getDrawingCache();
                        if (bitmap != null) {
                            //这句代码时调用保存的核心
                            new SaveImageTask().execute(bitmap);
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
                FileOutputStream outStream = null;
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
}

