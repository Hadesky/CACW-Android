package com.hadesky.cacw.presenter;

/**任务详情页面presenter接口
 * Created by dzysg on 2015/11/14 0014.
 */
public  interface  TaskDetailPresenter
{
    void LoadTaskMember();
    void onDestroy();

    void loadTaskInfo();
    void onDeleteTask();
}
