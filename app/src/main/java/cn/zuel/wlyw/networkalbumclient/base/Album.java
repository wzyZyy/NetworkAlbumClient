package cn.zuel.wlyw.networkalbumclient.base;

import java.util.Date;

public class Album {
    private int a_id;         // 相册id
    private int a_u_id;       // 相册拥有者id
    private int a_t_id;       // 相册主题id
    private String a_name;    // 相册名称
    private String a_desc;    // 相册描述
    private int a_auth;       // 相册权限
    private Date a_addtime;    // 添加时间
    private Date a_updatetime; // 修改时间

    public int getA_id() {
        return a_id;
    }

    public void setA_id(int a_id) {
        this.a_id = a_id;
    }

    public int getA_u_id() {
        return a_u_id;
    }

    public void setA_u_id(int a_u_id) {
        this.a_u_id = a_u_id;
    }

    public int getA_t_id() {
        return a_t_id;
    }

    public void setA_t_id(int a_t_id) {
        this.a_t_id = a_t_id;
    }

    public String getA_desc() {
        return a_desc;
    }

    public void setA_desc(String a_desc) {
        this.a_desc = a_desc;
    }

    public int getA_auth() {
        return a_auth;
    }

    public void setA_auth(int a_auth) {
        this.a_auth = a_auth;
    }

    public Date getA_addtime() {
        return a_addtime;
    }

    public String getA_name() {
        return a_name;
    }

    public void setA_name(String a_name) {
        this.a_name = a_name;
    }

    public void setA_addtime(Date a_addtime) {
        this.a_addtime = a_addtime;
    }

    public Date getA_updatetime() {
        return a_updatetime;
    }

    public void setA_updatetime(Date a_updatetime) {
        this.a_updatetime = a_updatetime;
    }

}
