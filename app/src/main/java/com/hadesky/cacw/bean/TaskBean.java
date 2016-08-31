package com.hadesky.cacw.bean;

import java.io.Serializable;

/**
 * 一项任务的实体对象类
 * Created by dzysg on 2015/9/14 0014.
 */


public class TaskBean implements Serializable
{
    private String mTitle = "";
    private String mContent="";
    private String mLocation = "";
    private ProjectBean mProjectBean;
    private String AdaminId = "";
    private String startDate;
    private String endDate;
    private int isFinish;

    public ProjectBean getProjectBean() {
        return mProjectBean;
    }

    public void setProjectBean(ProjectBean projectBean) {
        mProjectBean = projectBean;
    }

    public TaskBean() {

    }

    public int getIsFinish()
    {
        return isFinish;
    }

    public void setIsFinish(int isFinish)
    {
        this.isFinish = isFinish;
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

    public String getAdaminId() {
        return AdaminId;
    }

    public void setAdaminId(String adaminUserId) {
        AdaminId = adaminUserId;
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
