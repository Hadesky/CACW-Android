package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.ui.view.TaskDetailView;

/**任务详情操作接口实现 {@link TaskDetailPresenter}
 * Created by dzysg on 2015/11/14 0014.
 */
public class TaskDetailPresenterImpl implements TaskDetailPresenter
{


    TaskDetailView mView;
    TaskBean mTask;



    public TaskDetailPresenterImpl(TaskDetailView view,TaskBean task)
    {
        mTask = task;
        mView = view;
    }
    

    @Override
    public void LoadTask()
    {

    }

    @Override
    public void onDestroy() {

    }
}
