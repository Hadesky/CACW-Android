package com.hadesky.cacw.presenter;

/**
 * 编辑任务或新建任务
 * Created by dzysg on 2016/7/6 0006.
 */
public interface EditTaskPresenter {


    void loadTaskMember();

    void saveTask();

    void createTask();

    void loadProjects();

    void onDestroy();


}
