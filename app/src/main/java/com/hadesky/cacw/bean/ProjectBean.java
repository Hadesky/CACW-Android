package com.hadesky.cacw.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * project的表
 * Created by 45517 on 2015/9/18.
 */
public class ProjectBean extends BmobObject {

    private String mProjectName;//Item的文字
    private BmobFile mProjectAvatar;
    private String mTeamId;//团队所属Team
    private BmobRelation mTasks;//与之关联的Task
    private TeamBean mTeam;//所属团队


    public ProjectBean() {
    }


    public ProjectBean(String title, String teamId) {
        this.mProjectName = title;
        this.mTeamId = teamId;
    }

    public String getProjectName() {
        return mProjectName;
    }

    public void setTitle(String title) {
        this.mProjectName = title;
    }


    public BmobFile getProjectAvatar() {
        return mProjectAvatar;
    }

    public void setProjectAvatar(BmobFile mProjectAvatar) {
        this.mProjectAvatar = mProjectAvatar;
    }

    public String getTeamId() {
        return mTeamId;
    }

    public void setTeamId(String teamId) {
        this.mTeamId = teamId;
    }

}
