package cn.zuel.wlyw.networkalbumclient.base;

import java.util.Date;

public class User {
    private int u_id;         // 用户id
    private String u_nickname;     // 用户昵称
    private String u_phone;      // 用户电话
    private String u_pwd;    // 用户密码
    private String u_gender;    // 用户性别
    private String u_qq;       // 用户QQ
    private Date u_addtime;    // 添加时间
    private Date u_updatetime; // 修改时间

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getU_nickname() {
        return u_nickname;
    }

    public void setU_nickname(String u_nickname) {
        this.u_nickname = u_nickname;
    }

    public String getU_phone() {
        return u_phone;
    }

    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
    }

    public String getU_pwd() {
        return u_pwd;
    }

    public void setU_pwd(String u_pwd) {
        this.u_pwd = u_pwd;
    }

    public String getU_gender() {
        return u_gender;
    }

    public void setU_gender(String u_gender) {
        this.u_gender = u_gender;
    }

    public String getU_qq() {
        return u_qq;
    }

    public void setU_qq(String u_qq) {
        this.u_qq = u_qq;
    }

    public Date getU_addtime() {
        return u_addtime;
    }

    public void setU_addtime(Date u_addtime) {
        this.u_addtime = u_addtime;
    }

    public Date getU_updatetime() {
        return u_updatetime;
    }

    public void setU_updatetime(Date u_updatetime) {
        this.u_updatetime = u_updatetime;
    }
}
