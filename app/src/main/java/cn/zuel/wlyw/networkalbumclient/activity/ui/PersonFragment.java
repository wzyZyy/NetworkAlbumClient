package cn.zuel.wlyw.networkalbumclient.activity.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.activity.IndexActivity;
import cn.zuel.wlyw.networkalbumclient.base.User;


public class PersonFragment extends Fragment {
    private IndexActivity indexActivity;

    private EditText userNickname;
    private EditText userPhone;
    private EditText userGender;
    private EditText userQq;

    private User user;

    public PersonFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        // 与碎片关联的活动
        indexActivity = (IndexActivity) getActivity();

        userNickname = view.findViewById(R.id.display_user_nickname);
        userPhone = view.findViewById(R.id.display_user_phone);
        userGender = view.findViewById(R.id.display_user_gender);
        userQq = view.findViewById(R.id.display_user_qq);

        // 修改信息按钮设置监听事件
        view.findViewById(R.id.modify_user_info_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(indexActivity, "修改个人信息", Toast.LENGTH_SHORT).show();
                indexActivity.modifyUserInfo(userNickname.getText().toString(), userPhone.getText().toString(),
                        userGender.getText().toString(), userQq.getText().toString());
            }
        });
        // 显示用户信息
        userNickname.setText(user.getU_nickname());
        userPhone.setText(user.getU_phone());
        userGender.setText(user.getU_gender());
        userQq.setText(user.getU_qq());
        return view;
    }

    /**
     * @param user 用户信息
     */
    public void setUser(User user) {
        this.user = user;
    }
}
