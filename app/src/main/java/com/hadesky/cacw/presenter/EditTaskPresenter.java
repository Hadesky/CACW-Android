package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskMember;

import java.util.List;

/**
 * 编辑任务或新建任务
 * Created by dzysg on 2016/7/6 0006.
 */
public interface EditTaskPresenter {


    void loadTaskMember();

    void saveTask(List<TaskMember> Members);

    void loadProjects();

    void onDestroy();


}
