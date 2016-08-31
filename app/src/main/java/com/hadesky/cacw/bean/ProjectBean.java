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
    private int id;
    private int AdminId;
    private int isPrivate=0;
    private int isFile = 0;

    public ProjectBean() {

    }

    public int getIsFile()
    {
        return isFile;
    }

    public void setIsFile(int isFile)
    {
        this.isFile = isFile;
    }

    public int getIsPrivate()
    {
        return isPrivate;
    }

    public void setIsPrivate(int isPrivate)
    {
        this.isPrivate = isPrivate;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getAdminId()
    {
        return AdminId;
    }

    public void setAdminId(int adminId)
    {
        AdminId = adminId;
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
