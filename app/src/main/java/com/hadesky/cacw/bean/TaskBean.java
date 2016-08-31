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
    private String mAdaminUserId = "";
    private String mstartDate;
    private String mendDate;

    public ProjectBean getProjectBean() {
        return mProjectBean;
    }

    public void setProjectBean(ProjectBean projectBean) {
        mProjectBean = projectBean;
    }

    public TaskBean() {

    }

    public String getMstartDate()
    {
        return mstartDate;
    }

    public void setMstartDate(String mstartDate)
    {
        this.mstartDate = mstartDate;
    }

    public String getMendDate()
    {
        return mendDate;
    }

    public void setMendDate(String mendDate)
    {
        this.mendDate = mendDate;
    }

    public String getAdaminUserId() {
        return mAdaminUserId;
    }

    public void setAdaminUserId(String adaminUserId) {
        mAdaminUserId = adaminUserId;
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
