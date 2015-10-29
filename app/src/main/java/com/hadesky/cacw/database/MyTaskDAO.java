package com.hadesky.cacw.database;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.config.MyApp;

import java.util.ArrayList;
import java.util.List;

/**myTask的本地操作接口,后期应定义为接口
 * Created by dzysg on 2015/10/29 0029.
 */
public class MyTaskDAO
{

    public List<TaskBean> getUnCompleteTask()
    {
        List<TaskBean> list = new ArrayList<>();
        DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());
        DatabaseManager.TaskCursor cursor = manager.queryUncompleteTask();
        cursor.moveToFirst();
        int len = cursor.getCount();
        for (int i=0;i<len;i++) {
            list.add(cursor.getTaskBean());
            cursor.moveToNext();
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
