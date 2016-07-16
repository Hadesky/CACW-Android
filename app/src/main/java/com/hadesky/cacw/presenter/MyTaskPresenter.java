package com.hadesky.cacw.presenter;

/**控制任务页面数据加载逻辑接口
 * Created by dzysg on 2015/10/29 0029.
 */
public interface MyTaskPresenter
{
     void LoadTasks();
     void CompleteTask(int pos);
     void DeleteTask(int pos);
    void onDestroy();

}
