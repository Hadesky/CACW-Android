package com.hadesky.cacw.bean;

import cn.bmob.v3.BmobObject;

/**
 * Task和User的关联表
 * Created by MicroStudent on 2016/7/5.
 */

public class TaskMember extends BmobObject {
    private UserBean mUser;
    private TaskBean mTask;
    private Boolean mIsFinish;


    public Boolean getFinish() {
        return mIsFinish;
    }

    public void setFinish(Boolean finish) {
        mIsFinish = finish;
    }

    public UserBean getUser() {
        return mUser;
    }

    public void setUser(UserBean user) {
        mUser = user;
    }

    public TaskBean getTask() {
        return mTask;
    }

    public void setTask(TaskBean task) {
        mTask = task;
    }
}
