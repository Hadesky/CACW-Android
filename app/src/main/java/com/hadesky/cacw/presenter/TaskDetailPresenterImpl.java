package com.hadesky.cacw.presenter;

import com.hadesky.cacw.Model.TaskDetailModel;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.ui.View.TaskDetailView;

/**任务详情操作接口实现 {@link TaskDetailPresenter}
 * Created by dzysg on 2015/11/14 0014.
 */
public class TaskDetailPresenterImpl implements TaskDetailPresenter
{

    TaskDetailModel mModel;
    TaskDetailView mView;
    TaskDetailModel.TaskDetailCallBack mCallBack = new TaskDetailModel.TaskDetailCallBack() {
        @Override
        public void succeed(Object o)
        {
            if (o instanceof TaskBean)
            mView.showInfo((TaskBean) o);

        }

        @Override
        public void error(String msg)
        {

        }
    };


    public TaskDetailPresenterImpl(TaskDetailView view)
    {
        mModel = new TaskDetailModel(mCallBack);
        mView = view;
    }
    

    @Override
    public void LoadTask(long taskId)
    {
        mModel.getTask(taskId);

    }
}
