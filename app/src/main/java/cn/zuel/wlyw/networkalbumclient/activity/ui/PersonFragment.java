package cn.zuel.wlyw.networkalbumclient.activity.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.activity.IndexActivity;


public class PersonFragment extends Fragment {

    IndexActivity indexActivity;

    EditText userNickname;
    EditText userPhone;
    EditText userGender;
    EditText userQq;
    Button modifyUserInfoBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        indexActivity = (IndexActivity) getActivity();
        userNickname = view.findViewById(R.id.display_user_nickname);
        userPhone = view.findViewById(R.id.display_user_phone);
        userGender = view.findViewById(R.id.display_user_gender);
        userQq = view.findViewById(R.id.display_user_qq);
        modifyUserInfoBtn = view.findViewById(R.id.modify_user_info_btn);

        modifyUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexActivity.modifyUserInfo(userNickname.getText().toString(), userPhone.getText().toString(),
                        userGender.getText().toString(), userQq.getText().toString());
            }
        });
        return view;
    }

    /**
     * 设置内容显示组件的内容
     *
     * @param u_nickname
     * @param u_phone
     * @param u_gender
     * @param u_qq
     */
    public void refresh(String u_nickname, String u_phone, String u_gender, String u_qq) {
        userNickname.setText(u_nickname);
        userPhone.setText(u_phone);
        userGender.setText(u_gender);
        userQq.setText(u_qq);
    }
}
