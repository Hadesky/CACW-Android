package com.hadesky.cacw.bean;

import java.io.Serializable;

/**
 * project的表
 * Created by 45517 on 2015/9/18.
 */
public class ProjectBean  implements Serializable
{

    private String name;//Item的文字
    private TeamBean team;//所属团队
    private int id;
    private int AdminId;
    private int isPrivate=0;
    private int isFile = 0;
    private int taskCount;//任务总数
    private int unfinishTaskCount;//没完成的任务数


    public ProjectBean() {

    }

    public int getTaskCount()
    {
        return taskCount;
    }

    public void setTaskCount(int taskCount)
    {
        this.taskCount = taskCount;
    }

    public int getUnfinishTaskCount()
    {
        return unfinishTaskCount;
    }

    public void setUnfinishTaskCount(int unfinishTaskCount)
    {
        this.unfinishTaskCount = unfinishTaskCount;
    }

    public int getIsFile()
    {
        return isFile;
    }

    public void setIsFile(int isFile)
    {
        this.isFile = isFile;
    }

    public boolean isPrivate()
    {
        return isPrivate>0;
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
        this.name = title;
    }

    public TeamBean getTeam() {
        return team;
    }

    public void setTeam(TeamBean mTeam) {
        this.team = mTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }


    @Override
    public boolean equals(Object obj)
    {
        return obj != null && obj instanceof ProjectBean && ((ProjectBean) obj).id == id;

    }
}
