package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.ui.view.EditTaskView;

/** 编辑任务或新建任务presenter
 * Created by dzysg on 2016/9/2 0002.
 */
public class EditTaskPresenterImpl implements EditTaskPresenter
{
    private EditTaskView mView;
    private TaskBean mTask;

    public EditTaskPresenterImpl(EditTaskView view,TaskBean t)
    {
        mView = view;
        mTask = t;
    }

    @Override
    public void loadTaskMember()
    {

    }

    @Override
    public void saveTask()
    {

    }

    @Override
    public void createTask()
    {

    }

    @Override
    public void loadProjects()
    {

    }

    @Override
    public void onDestroy()
    {

    }
}
