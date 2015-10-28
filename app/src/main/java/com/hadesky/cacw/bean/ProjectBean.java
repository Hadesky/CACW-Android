package com.hadesky.cacw.bean;

/**
 * Created by 45517 on 2015/9/18.
 */
public class ProjectBean {
    private long project_id;
    private String project_name;//Item的文字
    private int avatar_resId;

    public ProjectBean() {
    }

    public ProjectBean(long projectId, String projectName, int avatarResId) {
        this.project_id = projectId;
        this.project_name = projectName;
        this.avatar_resId = avatarResId;
    }

    public ProjectBean(String title,long project_id) {
        this.project_name = title;
        this.project_id = project_id;
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

    @Override
    public String toString() {
        return "project_id = " + project_id + " ,project_name = " + project_name;
    }
}
