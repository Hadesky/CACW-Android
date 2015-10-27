package com.hadesky.cacw.bean;

/**
 * Created by 45517 on 2015/10/17.
 */
public class MemberBean {

    private String username;
    private int avatarResid;

    public MemberBean() {
    }

    public MemberBean(String username, int  avatarResid) {
        this.username = username;
        this.avatarResid = avatarResid;
    }

    public MemberBean(String username, int avatarResid, int type) {
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
}
