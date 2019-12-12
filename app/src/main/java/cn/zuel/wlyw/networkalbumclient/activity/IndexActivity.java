package cn.zuel.wlyw.networkalbumclient.activity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.activity.ui.HomeFragment;
import cn.zuel.wlyw.networkalbumclient.activity.ui.PersonFragment;
import cn.zuel.wlyw.networkalbumclient.activity.ui.PicFragment;
import cn.zuel.wlyw.networkalbumclient.base.BarEntity;
import cn.zuel.wlyw.networkalbumclient.base.BottomTabBar;

public class IndexActivity extends FragmentActivity implements BottomTabBar.OnSelectListener {
    private BottomTabBar tb ;
    private List<BarEntity> bars ;
    private HomeFragment homeFragment ;
    private PicFragment picFragment ;
    private PersonFragment personFragment ;
    private FragmentManager manager ;

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
}
