package com.hadesky.cacw.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 一项任务的实体对象类
 * Created by dzysg on 2015/9/14 0014.
 */


public class TaskBean implements Serializable
{


    private int id;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("content")
    private String mContent;

    @SerializedName("location")
    private String mLocation;

    @SerializedName("project")
    private ProjectBean mProject;
    private int AdminId;
    private String startDate;
    private String endDate;
    private int finish;


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public ProjectBean getProject() {
        return mProject;
    }

    public void setProject(ProjectBean project) {
        mProject = project;
    }


    public boolean isFinish()
    {
        return finish==1;
    }

    public void setFinish(int finish)
    {
        this.finish = finish;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String mstartDate)
    {
        this.startDate = mstartDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String mendDate)
    {
        this.endDate = mendDate;
    }

    public int getAdminId() {
        return AdminId;
    }

    public void setAdminId(int adaminUserId) {
        AdminId = adaminUserId;
    }

    public String getLocation()
    {
        return mLocation;
    }

    public void setLocation(String mlocation)
    {
        this.mLocation = mlocation;
    }
    public String getContent()
    {
        return mContent;
    }

    public void setContent(String content)
    {
        mContent = content;
    }


    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }


}
