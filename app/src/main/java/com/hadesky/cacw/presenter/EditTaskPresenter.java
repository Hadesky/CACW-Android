package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;

/**
 * 编辑任务或新建任务
 * Created by dzysg on 2016/7/6 0006.
 */
public interface EditTaskPresenter {

    void createNewTask(TaskBean taskBean);
    void loadTask();

    void saveTask();

    void loadProjects();

    void deleteMember(UserBean bean);

}
