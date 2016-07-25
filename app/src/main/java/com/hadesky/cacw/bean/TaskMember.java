package com.hadesky.cacw.bean;

import cn.bmob.v3.BmobObject;

/**
 * Task和User的关联表
 * Created by MicroStudent on 2016/7/5.
 */

public class TaskMember extends BmobObject {
    private UserBean mUser;
    private TaskBean mTask;
    private Byte mIsFinish = 1;//1 为未完成  2 为完成



    public void setFinish(boolean b)
    {
        if (b)
            mIsFinish = 2;
        else
            mIsFinish = 1;
    }
    public boolean isFinish()
    {
        return mIsFinish ==2;
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


    @Override
    public boolean equals(Object o)
    {
        if (o!=null&&o instanceof TaskMember)
        {
            return mUser.getObjectId().equals(((TaskMember) o).getUser().getObjectId());
        }
        return false;
    }
}
