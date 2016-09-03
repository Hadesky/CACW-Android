package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/**
 * 编辑任务或新建任务
 * Created by dzysg on 2016/7/6 0006.
 */
public interface EditTaskPresenter {


    void loadTaskMember();

    void saveTask();

    void createTask(List<UserBean> member);
    void addMember(List<UserBean> member);
    void deleteMember(List<UserBean> member);

    void loadProjects();

    void onDestroy();

}
