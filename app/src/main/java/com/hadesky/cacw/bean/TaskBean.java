package com.hadesky.cacw.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 一项任务的实体对象类
 * Created by dzysg on 2015/9/14 0014.
 */


public class TaskBean
{


    private String mTitle = "";
    private String mRemark = "";
    private Date mStartDate;
    private Date mEndDate;
    private long mTaskId = -1;
    private int mTaskStatus = 1;//完成为0,没完成为1
    private List<UserBean> mMembers = new ArrayList<>();
    private int mProjectId = -1;
    private String mContent="";
    private String mlocation ="";


    public String getLocation()
    {
        return mlocation;
    }

    public void setLocation(String mlocation)
    {
        this.mlocation = mlocation;
    }



    public String getContent()
    {
        return mContent;
    }

    public void setContent(String content)
    {
        mContent = content;
    }



    public String getProjectName()
    {
        return mProjectName;
    }

    public void setProjectName(String projectName)
    {
        mProjectName = projectName;
    }

    private String mProjectName ="";

    public List<UserBean> getMembers()
    {
        return mMembers;
    }

    public void setMembers(List<UserBean> members)
    {
        mMembers = members;
    }


    public void addMember(UserBean bean)
    {
        mMembers.add(bean);
    }

    public void delMember(int userid)
    {
        for (int i = 0; i < mMembers.size(); i++) {
            if (mMembers.get(i).getUserId() == userid) {
                mMembers.remove(i);
                break;
            }
        }
    }

    public int getProjectId()
    {
        return mProjectId;
    }

    public void setProjectId(int projectId)
    {
        mProjectId = projectId;
    }




    public TaskBean()
    {
    }

    public TaskBean(String title, long task_id, int projectId)
    {
        this.mTitle = title;
        this.mTaskId = task_id;
        this.mProjectId = projectId;
    }

    public TaskBean(String title, long task_id)
    {
        this.mTitle = title;
        this.mTaskId = task_id;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public String getRemark()
    {
        return mRemark;
    }

    public void setRemark(String remark)
    {
        mRemark = remark;
    }

    public Date getStartDate()
    {
        return mStartDate;
    }

    public void setStartDate(Date startDate)
    {
        mStartDate = startDate;
    }

    public long getTaskId()
    {
        return mTaskId;
    }

    public void setTaskId(long ID)
    {
        this.mTaskId = ID;
    }

    public int getTaskStatus()
    {
        return mTaskStatus;
    }

    public void setTaskStatus(int taskStatus)
    {
        mTaskStatus = taskStatus;
    }

    public Date getEndDate()
    {
        return mEndDate;
    }

    public void setEndDate(Date endDate)
    {
        mEndDate = endDate;
    }
}
