package com.hadesky.cacw.bean;

import java.util.Date;

/**一项任务的实体对象类
 * Created by dzysg on 2015/9/14 0014.
 */


public class TaskBean {




    private String mTitle;
    private String mRemark;
    private Date mStartDate;
    private Date mEndDate;
    private int ID;
    private int mTaskStatus;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getRemark() {
        return mRemark;
    }

    public void setRemark(String remark) {
        mRemark = remark;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTaskStatus() {
        return mTaskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        mTaskStatus = taskStatus;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }
}
