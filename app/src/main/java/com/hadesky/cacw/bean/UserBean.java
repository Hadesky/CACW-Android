package com.hadesky.cacw.bean;

/**
 *
 * Created by 45517 on 2015/10/17.
 */
public class UserBean {

    private String username;

    private long user_id;

    private int avatarResid;


    public UserBean() {
    }

    public UserBean(String username, int avatarResid) {
        this.username = username;
        this.avatarResid = avatarResid;
    }

    public UserBean(String username, int avatarResid, int type) {
        this.username = username;
        this.avatarResid = avatarResid;
    }

    public String getUsername() {
        return username;
    }

    public int getAvatarResid() {
        return avatarResid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarResid(int avatarResid) {
        this.avatarResid = avatarResid;
    }

    public long getUserid() {
        return user_id;
    }

    public void setUserid(long user_id) {
        this.user_id = user_id;
    }
}
