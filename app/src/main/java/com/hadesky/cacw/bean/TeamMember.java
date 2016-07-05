package com.hadesky.cacw.bean;

import cn.bmob.v3.BmobObject;

/**
 * Team 和User的关联表
 * Created by MicroStudent on 2016/7/5.
 */

public class TeamMember extends BmobObject {

    private TeamBean mTeam;

    private UserBean mUser;

    public UserBean getUser() {
        return mUser;
    }

    public void setUser(UserBean user) {
        mUser = user;
    }

    public TeamBean getTeam() {
        return mTeam;
    }

    public void setTeam(TeamBean team) {
        mTeam = team;
    }
}
