package com.hadesky.cacw.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 团队实体
 * Created by 45517 on 2015/10/31.
 */
public class TeamBean extends BmobObject {

    private Long mTeamId;

    private String mTeamName;

    private String mSummary;//团队介绍

    private BmobFile mTeamAvatar;//团队头像

    private UserBean mAdminUser;//管理员

    private BmobRelation mProjects;

    public TeamBean() {

    }

    public BmobRelation getProjects() {
        return mProjects;
    }

    public void setProjects(BmobRelation mProjects) {
        this.mProjects = mProjects;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof TeamBean) {
            if (((TeamBean) o).getObjectId().equals(getObjectId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (getObjectId() != null) {
            result = 31 * result + getObjectId().hashCode();
        }
        return result;
    }
}
