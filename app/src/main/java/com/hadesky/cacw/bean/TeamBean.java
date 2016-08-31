package com.hadesky.cacw.bean;

import java.io.Serializable;

/**
 * 团队实体
 * Created by 45517 on 2015/10/31.
 */
public class TeamBean implements Serializable
{

    private Long mTeamId;

    private String mTeamName;

    private String mSummary;//团队介绍

    private UserBean mAdminUser;//管理员


    public TeamBean() {

    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public TeamBean(String mTeamName) {
        this.mTeamName = mTeamName;
    }

    public Long getTeamId() {
        return mTeamId;
    }

    public void setTeamId(Long teamId) {
        mTeamId = teamId;
    }

    public UserBean getAdminUser() {
        return mAdminUser;
    }

    public void setAdminUser(UserBean adminUser) {
        mAdminUser = adminUser;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String team_name) {
        this.mTeamName = team_name;
    }


    public String getTeamAvatarUrl()
    {
        return "";
    }
}
