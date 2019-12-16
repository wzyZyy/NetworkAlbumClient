package cn.zuel.wlyw.networkalbumclient.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.zuel.wlyw.networkalbumclient.R;

public class AlbumActivity extends BaseActivity {
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
    }
}
