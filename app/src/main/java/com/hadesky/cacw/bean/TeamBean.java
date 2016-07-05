package com.hadesky.cacw.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 团队实体
 * Created by 45517 on 2015/10/31.
 */
public class TeamBean extends BmobObject {

    private String mTeamName;

    private String mSummary;//团队介绍

    private BmobFile mTeamAvatar;//团队头像

    private String mAdminUserId;//管理员

    private BmobRelation mProjects;

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public BmobFile getTeamAvatar() {
        return mTeamAvatar;
    }

    public void setTeamAvatar(BmobFile teamAvatar) {
        mTeamAvatar = teamAvatar;
    }

    public String getAdminUserId() {
        return mAdminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        mAdminUserId = adminUserId;
    }

    public TeamBean(String mTeamName) {
        this.mTeamName = mTeamName;
    }

    public TeamBean() {

    }


    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String team_name) {
        this.mTeamName = team_name;
    }

}
