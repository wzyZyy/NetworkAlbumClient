package cn.zuel.wlyw.networkalbumclient.activity.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import cn.zuel.wlyw.networkalbumclient.R;

/**
 * Created by WZH on 2016/11/5.
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        return view;
    }
}
