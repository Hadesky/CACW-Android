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
    private long task_id;
    private int mTaskStatus;

    public TaskBean() {
    }

    public TaskBean(String title, long task_id) {
        this.mTitle = title;
        this.task_id = task_id;
    }

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

    public long getTaskId() {
        return task_id;
    }

    public void setTaskID(long ID) {
        this.task_id = ID;
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
