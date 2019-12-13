package cn.zuel.wlyw.networkalbumclient.base;

import java.util.Date;

public class Image {
    private int i_id;
    private int i_a_id;
    private String i_name;
    private String i_path;
    private Date i_addtime;
    private Date i_updatetime;

    public int getI_id() {
        return i_id;
    }

    public void setI_id(int i_id) {
        this.i_id = i_id;
    }

    public int getI_a_id() {
        return i_a_id;
    }

    public void setI_a_id(int i_a_id) {
        this.i_a_id = i_a_id;
    }

    public String getI_name() {
        return i_name;
    }

    public void setI_name(String i_name) {
        this.i_name = i_name;
    }

    public String getI_path() {
        return i_path;
    }

    public void setI_path(String i_path) {
        this.i_path = i_path;
    }

    public Date getI_addtime() {
        return i_addtime;
    }

    public void setI_addtime(Date i_addtime) {
        this.i_addtime = i_addtime;
    }

    public Date getI_updatetime() {
        return i_updatetime;
    }

    public void setI_updatetime(Date i_updatetime) {
        this.i_updatetime = i_updatetime;
    }
}
