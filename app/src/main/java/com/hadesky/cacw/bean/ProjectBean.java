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
    private BmobRelation mTasks;//与之关联的Task
    private TeamBean mTeam;//所属团队


    public ProjectBean() {
    }


    public ProjectBean(String title) {
        this.mProjectName = title;
    }

    public TeamBean getTeam() {
        return mTeam;
    }

    public void setTeam(TeamBean mTeam) {
        this.mTeam = mTeam;
    }

    public String getProjectName() {
        return mProjectName;
    }

    public void setProjectName(String title) {
        this.mProjectName = title;
    }


    public BmobFile getProjectAvatar() {
        return mProjectAvatar;
    }

    public void setProjectAvatar(BmobFile mProjectAvatar) {
        this.mProjectAvatar = mProjectAvatar;
    }



}
