package cn.zuel.wlyw.networkalbumclient.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.activity.ui.HomeFragment;
import cn.zuel.wlyw.networkalbumclient.activity.ui.PersonFragment;
import cn.zuel.wlyw.networkalbumclient.activity.ui.PicFragment;
import cn.zuel.wlyw.networkalbumclient.base.Album;
import cn.zuel.wlyw.networkalbumclient.base.AlbumAdapter;
import cn.zuel.wlyw.networkalbumclient.base.BarEntity;
import cn.zuel.wlyw.networkalbumclient.base.BottomTabBar;
import cn.zuel.wlyw.networkalbumclient.config.MainConfig;
import cz.msebera.android.httpclient.Header;

public class IndexActivity extends FragmentActivity implements BottomTabBar.OnSelectListener {
    private BottomTabBar tb ;
    private List<BarEntity> bars ;
    private HomeFragment homeFragment ;
    private PicFragment picFragment ;
    private PersonFragment personFragment ;
    private FragmentManager manager ;

    private static final String TAG = "IndexActivity";
    // 用于保存相册信息
    private List<Album> albumList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
    }

    private void initView() {
        manager = getSupportFragmentManager();
        tb = findViewById(R.id.tb);
        bars = new ArrayList<>();
        bars.add(new BarEntity("首页",R.drawable.home_select,R.drawable.home_unselect));
        bars.add(new BarEntity("图库",R.drawable.pic_select,R.drawable.pic_unselect));
        bars.add(new BarEntity("个人",R.drawable.person_select,R.drawable.person_unselect));
        tb.setManager(manager).setOnSelectListener(this).setBars(bars);
    }
    @Override
    public void onSelect(int position) {
        switch (position){
            case 0:
                if (homeFragment==null){
                    homeFragment = new HomeFragment();
                }
                tb.switchContent(homeFragment);
                getAlbums();
                break;
            case 1:
                if (picFragment==null){
                    picFragment = new PicFragment();
                }
                tb.switchContent(picFragment);
                break;
            case 2:
                if (personFragment==null){
                    personFragment = new PersonFragment();
                }
                tb.switchContent(personFragment);
                break;
            default:
                break;
        }
    }

    /**
     * 获取相册照片
     *
     * @param a_id
     */
    public void viewImageByAlbum(int a_id) {

    }

    public void deleteAlbum(int a_id) {

    }

    private void getAlbums() {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d(TAG, "getAlbums: " + MainConfig.ALBUM_GET_URL);
        client.post(MainConfig.ALBUM_GET_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(IndexActivity.this, "网络错误，查询标签失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: net error view label fail");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "onSuccess: get albums success");
                Log.d(TAG, "onSuccess: " + responseString);

                String data = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    data = jsonObject.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                albumList = JSON.parseArray(data, Album.class);
                Log.d(TAG, "onSuccess: " + albumList);
                setRecycleView();
            }
        });
    }

    private void setRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recycle_view_album);
        LinearLayoutManager layoutManager = new LinearLayoutManager(IndexActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        AlbumAdapter labelAdapter = new AlbumAdapter(albumList, IndexActivity.this);
        recyclerView.setAdapter(labelAdapter);
    }
}
