package cn.zuel.wlyw.networkalbumclient.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.activity.ui.HomeFragment;
import cn.zuel.wlyw.networkalbumclient.activity.ui.PersonFragment;
import cn.zuel.wlyw.networkalbumclient.activity.ui.PicFragment;
import cn.zuel.wlyw.networkalbumclient.base.Album;
import cn.zuel.wlyw.networkalbumclient.base.AlbumAdapter;
import cn.zuel.wlyw.networkalbumclient.base.Bar;
import cn.zuel.wlyw.networkalbumclient.base.BottomTabBar;
import cn.zuel.wlyw.networkalbumclient.base.ShareAlbumAdapter;
import cn.zuel.wlyw.networkalbumclient.base.User;
import cn.zuel.wlyw.networkalbumclient.config.ServerConstantConfig;
import cz.msebera.android.httpclient.Header;

public class IndexActivity extends BaseActivity implements BottomTabBar.OnSelectListener {

    private static final String TAG = "IndexActivity";
    // 用户id
    private int u_id;
    private User user;

    private BottomTabBar tb;
    // 底部的三个碎片
    private HomeFragment homeFragment;
    private PicFragment picFragment;
    private PersonFragment personFragment;
    // 用于保存相册信息
    private List<Album> albumList = new ArrayList<>();
    private List<Album> sharedAlbumList = new ArrayList<>();

    private boolean refreshFlag = false;


    /**
     * 启动该活动的接口
     *
     * @param context 活动启动方
     */
    public static void actionStart(Context context, int u_id) {
        Intent intent = new Intent(context, IndexActivity.class);
        intent.putExtra("u_id", u_id);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Intent intent = getIntent();
        u_id = intent.getIntExtra("u_id", 0);
        getUserInfo();
        initView();
    }

    // 活动重启，刷新页面，查看自己的相册
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (refreshFlag) {
            getAlbums();
        }
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        tb = findViewById(R.id.tb);
        List<Bar> bars = new ArrayList<>();
        bars.add(new Bar("我的图库", R.drawable.pic_select, R.drawable.pic_unselect));
        bars.add(new Bar("发现", R.drawable.home_select, R.drawable.home_unselect));
        bars.add(new Bar("个人", R.drawable.person_select, R.drawable.person_unselect));
        tb.setManager(manager).setOnSelectListener(this).setBars(bars);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 添加新建相册的菜单项
        getMenuInflater().inflate(R.menu.create_album, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 响应菜单项的选择结果
        switch (item.getItemId()) {
            case R.id.createAlbum:
                Toast.makeText(this, "新建相册", Toast.LENGTH_SHORT).show();
                // 启动活动AlbumActivity，新建相册
                AlbumActivity.actionStart(IndexActivity.this, u_id);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelect(int position) {
        switch (position) {
            case 0:
                refreshFlag = true;
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                tb.switchContent(homeFragment);
                // 获取属于该用户的所有相册
                getAlbums();
                break;
            case 1:
                refreshFlag = false;
                if (picFragment == null) {
                    picFragment = new PicFragment();
                }
                tb.switchContent(picFragment);
                // 获取其它用户共享的相册
                getSharedAlbums();
                break;
            case 2:
                refreshFlag = false;
                if (personFragment == null) {
                    personFragment = new PersonFragment(user);
                }
                tb.switchContent(personFragment);
                break;
            default:
                break;
        }
    }

    /**
     * 用户自己相册使用RecycleView
     */
    private void setRecycleView() {
        // 获取RecycleView的实例
        RecyclerView recyclerView = findViewById(R.id.recycle_view_album);
        // 指定Recycle的布局方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(IndexActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        // 创建适配器的实例（并传入数据）
        AlbumAdapter albumAdapter = new AlbumAdapter(albumList, IndexActivity.this);
        // 设置适配器
        recyclerView.setAdapter(albumAdapter);
    }

    /**
     * 浏览其他人共享的网络相册
     */
    private void setSharedRecycleView() {
        // 获取RecycleView的实例
        RecyclerView recyclerView = findViewById(R.id.recycle_view_share_album);
        // 指定Recycle的布局方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(IndexActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        // 创建适配器的实例（并传入数据）
        ShareAlbumAdapter shareAlbumAdapter = new ShareAlbumAdapter(sharedAlbumList, IndexActivity.this);
        // 设置适配器
        recyclerView.setAdapter(shareAlbumAdapter);
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.put("u_id", u_id);

        client.post(ServerConstantConfig.GET_USER_URL, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(IndexActivity.this, "网络错误，获取用户信息失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "点击底部tab,获取用户信息失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "成功获取用户信息，服务器响应结果：" + responseString);
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
                Toast.makeText(IndexActivity.this, resultDesc, Toast.LENGTH_SHORT).show();
                if (resultCode.equals("7000")) {
                    // 获取用户信息成功
                    user = JSON.parseObject(data, User.class);
                }
            }
        });
    }

    /**
     * 获取用户所有相册
     */
    private void getAlbums() {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.put("u_id", u_id);
        client.post(ServerConstantConfig.ALBUM_GET_URL, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(IndexActivity.this, "网络错误，获取相册失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "点击底部tab,获取相册失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "成功获取相册，服务器响应结果：" + responseString);
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
                Toast.makeText(IndexActivity.this, resultDesc, Toast.LENGTH_SHORT).show();
                if (resultCode.equals("6023")) {
                    // 获取相册成功
                    albumList = JSON.parseArray(data, Album.class);
                    setRecycleView();
                }
            }
        });
    }

    /**
     * 获取其它用户共享的所有相册
     */
    private void getSharedAlbums() {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.put("u_id", u_id);
        client.post(ServerConstantConfig.ALBUM_GET_SHARE_URL, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(IndexActivity.this, "网络错误，获取相册失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "点击底部tab,获取共享相册失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "成功获取共享相册，服务器响应结果：" + responseString);
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
                Toast.makeText(IndexActivity.this, resultDesc, Toast.LENGTH_SHORT).show();
                if (resultCode.equals("6023")) {
                    // 获取相册成功
                    sharedAlbumList = JSON.parseArray(data, Album.class);
                    setSharedRecycleView();
                }
            }
        });
    }

    /**
     * 响应AlbumAdapter的点击相册事件
     * 点击相册，获取相册中的照片，启动ImageActivity
     *
     * @param a_id 相册id
     */
    public void viewImageByAlbum(int a_id) {
        // 启动活动ImageActivity
        ImageActivity.actionStart(IndexActivity.this, a_id);
    }

    /**
     * @param a_id 相册id
     */
    public void viewShareImageByAlbum(int a_id) {
        // 启动活动ShareImageActivity
        ShareImageActivity.actionStart(IndexActivity.this, a_id);
    }

    /**
     * 删除相册
     *
     * @param a_id 相册id
     */
    public void deleteAlbum(int a_id) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("a_id", a_id);
        client.post(ServerConstantConfig.ALBUM_DELETE_URL, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(IndexActivity.this, "网络错误，删除相册失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "删除相册失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "成功获取相册，服务器响应结果：" + responseString);
                String resultCode = "";
                String resultDesc = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    resultCode = jsonObject.getString("resultCode");
                    resultDesc = jsonObject.getString("resultDesc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(IndexActivity.this, resultDesc, Toast.LENGTH_SHORT).show();
                if (resultCode.equals("6025")) {
                    // 删除相册成功，刷新
                    getAlbums();
                    setRecycleView();
                }
            }
        });
    }

    /**
     * 修改用户信息
     */
    public void modifyUserInfo(String u_nickname, String u_phone, String u_gender, String u_qq) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.put("u_id", u_id);
        requestParams.put("u_nickname", u_nickname);
        requestParams.put("u_phone", u_phone);
        requestParams.put("u_gender", u_gender);
        requestParams.put("u_qq", u_qq);

        client.post(ServerConstantConfig.MODIFY_USER_INFO_URL, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(IndexActivity.this, "网络错误，修改用户信息失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "修改用户信息失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(TAG, "成功修改用户信息，服务器响应结果：" + responseString);
                String resultCode = "";
                String resultDesc = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    resultCode = jsonObject.getString("resultCode");
                    resultDesc = jsonObject.getString("resultDesc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(IndexActivity.this, resultDesc, Toast.LENGTH_SHORT).show();
                if (resultCode.equals("7002")) {
                    // 修改用户信息成功
                    getUserInfo();
                    personFragment.setUser(user);
                }
            }
        });
    }
}
