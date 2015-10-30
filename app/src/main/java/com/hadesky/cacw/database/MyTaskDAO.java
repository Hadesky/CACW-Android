package com.hadesky.cacw.database;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.config.MyApp;

import java.util.ArrayList;
import java.util.List;

/**
 * myTask的本地操作接口,后期应定义为接口
 * Created by dzysg on 2015/10/29 0029.
 */
public class MyTaskDAO
{


    /**
     * 查询任务
     *
     * @param complete 1为没完成,0为完成,2为所有
     * @return
     */
    public List<TaskBean> getTask(int complete)
    {
        List<TaskBean> list = new ArrayList<>();
        DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());

        DatabaseManager.TaskCursor cursor;
        if (complete == 1)
            cursor = manager.queryUncompleteTask();
        else if (complete == 0)
            cursor = manager.queryCompleteTask();
        else
            cursor = manager.queryAllcompleteTask();


        while (cursor.moveToNext()) {
            list.add(cursor.getTaskBean());
        }

        cursor.close();
        return list;
    }

    public void deleteTask(int id)
    {
        DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());
        manager.deleteTask(id);
    }

    public void CompleteTask(int id)
    {
        DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());
        manager.updateTaskComplete(id, 0);
    }
}
