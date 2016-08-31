package com.hadesky.cacw.presenter;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.ui.view.TaskView;

/**主界面 我的任务
 * Created by dzysg on 2016/8/31 0031.
 */
public class MyTaskPresenterImpl implements MyTaskPresenter
{


    private TaskView mView;

    public MyTaskPresenterImpl(TaskView view)
    {
        mView = view;
    }

    /** 任务状态
     * @param state 0为未完成 1 为完成 2为所有
     */
    @Override
    public void LoadTasks(int state)
    {

    }

    @Override
    public void CompleteTask(TaskBean pos)
    {

    }

    @Override
    public void onDestroy()
    {

    }
}
