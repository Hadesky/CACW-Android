package com.hadesky.cacw.bean;

import java.io.Serializable;

/**
 * project的表
 * Created by 45517 on 2015/9/18.
 */
public class ProjectBean  implements Serializable
{

    private String mProjectName;//Item的文字
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



}
