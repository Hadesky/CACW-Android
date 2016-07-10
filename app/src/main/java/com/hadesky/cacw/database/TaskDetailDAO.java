package com.hadesky.cacw.database;

import android.util.Log;

import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;

import java.util.ArrayList;
import java.util.List;

/**任务详情数据库操作层
 * Created by dzysg on 2015/11/14 0014.
 */
public class TaskDetailDAO
{


    public TaskBean getTask(long id)
    {
        TaskBean task;
        //获取任务
        DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());
        DatabaseManager.TaskCursor cursor = manager.queryTask(id);
        cursor.moveToFirst();
        task = cursor.getTaskBean();
        Log.i("tag", "taskid dao" + task.getObjectId());
        cursor.close();

        if (task!=null)
        {
            //获取任务成员
            List<UserBean> list = new ArrayList<>();
            DatabaseManager.UserCursor userCursor = manager.queryUserFromTask(id);
            while (userCursor.moveToNext()) {
                list.add(userCursor.getUserBean());
            }

            userCursor.close();

//            task.setMembers(list);
//            //获取任务所属项目
//            DatabaseManager.ProjectCursor projectCursor = manager.queryProject(task.getProjectId());
//            if (projectCursor.moveToNext()) {
//                task.setProjectName(projectCursor.getProjectBean().getProjectName());
//            }
//            projectCursor.closePage();

        }
        return task;
    }

}
