package com.hadesky.cacw.bean;

/**
 * Created by 45517 on 2015/10/17.
 */
public class MemberBean {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_ADD = 1;
    public static final int TYPE_REDUCE = 2;
    private String username;
    private int avatarResid;
    private int type = TYPE_NORMAL;

    public MemberBean(String username, int  avatarResid) {
        this.username = username;
        this.avatarResid = avatarResid;
    }

    public MemberBean(String username, int avatarResid, int type) {
        this.username = username;
        this.avatarResid = avatarResid;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public int getAvatarResid() {
        return avatarResid;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
