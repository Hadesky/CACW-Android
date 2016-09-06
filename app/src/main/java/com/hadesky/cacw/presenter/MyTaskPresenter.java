package com.hadesky.cacw.presenter;


import com.hadesky.cacw.bean.TaskBean;

/**控制任务页面数据加载逻辑接口
 * Created by dzysg on 2015/10/29 0029.
 */
public interface MyTaskPresenter
{
    //0为未完成 1 为完成 2为所有
    void LoadTasks(int state,int projectid);
    void CompleteTask(TaskBean pos);
    void onDestroy();

}
