package com.hadesky.cacw.bean;

import com.hadesky.cacw.R;

/**
 * Created by 45517 on 2015/9/18.
 */
public class ProjectBean {
    private long project_id;
    private String project_name;//Item的文字
    private int avatar_resId = R.drawable.default_user_image;
    private long team_id;//团队所属Team

    public ProjectBean() {
    }


    public ProjectBean(String title,long project_id,long teamId) {
        this.project_name = title;
        this.project_id = project_id;
        this.team_id = teamId;
    }


    public String getProjectName() {
        return project_name;
    }



    public void setTitle(String title) {
        this.project_name = title;
    }

    public long getProjectId() {
        return project_id;
    }

    public void setProjectId(long project_id) {
        this.project_id = project_id;
    }

    public int getAvatarResId() {
        return avatar_resId;
    }

    public void setAvatarResId(int avatarResId) {
        this.avatar_resId = avatarResId;
    }

    public long getTeamId() {
        return team_id;
    }

    public void setTeamId(long teamId) {
        this.team_id = teamId;
    }

    @Override
    public String toString() {
        return "project_id = " + project_id + " ,project_name = " + project_name;
    }
}
