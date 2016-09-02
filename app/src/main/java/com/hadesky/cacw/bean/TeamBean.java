package com.hadesky.cacw.bean;

import com.hadesky.cacw.config.MyApp;

import java.io.Serializable;

/**
 * 团队实体
 * Created by 45517 on 2015/10/31.
 */
public class TeamBean implements Serializable
{


    private int id;
    private String teamName;
    private String summary;//团队介绍
    private int AdminId;
    private String notice;
    private int projectCount;
    private int memberCount;
    private String avatarUrl;


    public int getProjectCount()
    {
        return projectCount;
    }

    public int getMemberCount()
    {
        return memberCount;
    }

    public int getAdminId()
    {
        return AdminId;
    }

    public void setAdminId(int adminId)
    {
        AdminId = adminId;
    }

    public String getNotice()
    {
        return notice;
    }

    public void setNotice(String notice)
    {
        this.notice = notice;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public TeamBean(String mTeamName) {
        this.teamName = mTeamName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String team_name) {
        this.teamName = team_name;
    }


    public String getTeamAvatarUrl()
    {
        return MyApp.getURL()+"/v1/images/team_"+id+"_"+avatarUrl+".jpg";
    }
}
