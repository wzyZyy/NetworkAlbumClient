package cn.zuel.wlyw.networkalbumclient.activity.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import cn.zuel.wlyw.networkalbumclient.R;
import cn.zuel.wlyw.networkalbumclient.activity.IndexActivity;
import cn.zuel.wlyw.networkalbumclient.base.User;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class PersonFragment extends Fragment {

    private static final String TAG = "PersonFragment";

    private IndexActivity indexActivity;

    private EditText userNickname;
    private EditText userPhone;
    private EditText userGender;
    private EditText userQq;
    private Button modifyUserInfoBtn;

    private User user;

    public PersonFragment(User user) {
        this.user = user;
    }

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

        userNickname.setText(user.getU_nickname());
        userPhone.setText(user.getU_phone());
        userGender.setText(user.getU_gender());
        userQq.setText(user.getU_qq());

        modifyUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(indexActivity, "点击了修改按钮", Toast.LENGTH_SHORT).show();
                indexActivity.modifyUserInfo(userNickname.getText().toString(), userPhone.getText().toString(),
                        userGender.getText().toString(), userQq.getText().toString());
            }
        });
        return view;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //    /**
//     * 设置内容显示组件的内容
//     *
//     * @param u_nickname
//     * @param u_phone
//     * @param u_gender
//     * @param u_qq
//     */
//    public void refresh(String u_nickname, String u_phone, String u_gender, String u_qq) {
//        Log.d(TAG, "refresh: " + "refresh方法come--------------------------------->");
//        Log.d(TAG, "refresh: " + u_nickname);
//        Log.d(TAG, "refresh: " + u_phone);
//        Log.d(TAG, "refresh: " + u_gender);
//        Log.d(TAG, "refresh: " + u_qq);
////        userNickname.setText(u_nickname);
////        userPhone.setText(u_phone);
////        userGender.setText(u_gender);
////        userQq.setText(u_qq);
//    }
//    public void refresh(User user) {
//        Log.d(TAG, "refresh: " + "refresh方法come--------------------------------->");
////        Log.d(TAG, "refresh: " + u_nickname);
////        Log.d(TAG, "refresh: " + u_phone);
////        Log.d(TAG, "refresh: " + u_gender);
////        Log.d(TAG, "refresh: " + u_qq);
////        userNickname.setText(u_nickname);
////        userPhone.setText(u_phone);
////        userGender.setText(u_gender);
////        userQq.setText(u_qq);
//        this.user = user;
//    }
}
