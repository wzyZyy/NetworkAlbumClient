package cn.zuel.wlyw.networkalbumclient.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.base.AlbumAdapter;

public class AlbumActivity extends BaseActivity {
    private NiceSpinner albumPermission;
    private NiceSpinner albumTheme;
    List<String> permissionData = new LinkedList<>(Arrays.asList("私密","共享"));
    List<String> themeData = new LinkedList<>(Arrays.asList("普通", "多人", "亲子", "旅游","情侣"));
    /**
     *
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

        albumPermission.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 :
                        Toast.makeText(AlbumActivity.this, "私密", Toast.LENGTH_SHORT).show();
                        break;
                    case 1 :
                        Toast.makeText(AlbumActivity.this, "共享", Toast.LENGTH_SHORT).show();
                        break;
                   default:
                       break;
                }
            }
        });
    }
}
