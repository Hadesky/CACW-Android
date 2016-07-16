package com.hadesky.cacw.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * 一项任务的实体对象类
 * Created by dzysg on 2015/9/14 0014.
 */


public class TaskBean extends BmobObject
{
    private String mTitle = "";
    private BmobDate mStartDate;
    private BmobDate mEndDate;

    private String mContent="";
    private String mLocation = "";
    private ProjectBean mProjectBean;
    private String mAdaminUserId = "";

    public ProjectBean getProjectBean() {
        return mProjectBean;
    }

    public void setProjectBean(ProjectBean projectBean) {
        mProjectBean = projectBean;
    }

    public TaskBean() {

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

    public BmobDate getStartDate() {
        return mStartDate;
    }

    public void setStartDate(BmobDate startDate) {
        mStartDate = startDate;
    }

    public BmobDate getEndDate() {
        return mEndDate;
    }

    public void setEndDate(BmobDate endDate) {
        mEndDate = endDate;
    }

}
