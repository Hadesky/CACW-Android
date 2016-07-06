package com.hadesky.cacw.database;

import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TaskBean;
import com.hadesky.cacw.config.MyApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 45517 on 2015/10/30.
 */
public class ProjectDAO {
    /**
     * 获取当前用户所有的Project
     * @return 包含ProjectBean的List
     */
    public List<ProjectBean> getAllMyProject() {


        //DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());
        //DatabaseManager.ProjectCursor projectCursor = manager.queryProjectByUserId(MyApp.getSession().getUserDetails().getObjectId());
        List<ProjectBean> list = new ArrayList<>();

        //while (projectCursor.moveToNext()) {
        //    list.add(projectCursor.getProjectBean());
        //}
        //projectCursor.closeActivity();
        return list;
    }

    /**
     * 退出一个团队
     * @param user_id
     * @param project_id
     */
    public void quitProject(String user_id, long project_id) {
        DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());
        manager.deleteUserFromProject(user_id, project_id);
    }

    public List<TaskBean> getUncompletedTaskFromProject(long project_id) {
        DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());
        DatabaseManager.TaskCursor taskCursor = manager.queryUnCompletedTaskFromPj(project_id);
        List<TaskBean> list = new ArrayList<>();
        while (taskCursor.moveToNext()) {
            list.add(taskCursor.getTaskBean());
        }
        taskCursor.close();
        return list;
    }

    public List<TaskBean> getAllTaskFromProject(long project_id) {
        DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());
        DatabaseManager.TaskCursor taskCursor = manager.queryAllTaskFromPj(project_id);
        List<TaskBean> list = new ArrayList<>();
        while (taskCursor.moveToNext()) {
            list.add(taskCursor.getTaskBean());
        }
        taskCursor.close();
        return list;
    }


    public List<TaskBean> getCompletedTaskFromProject(long project_id) {
        DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());
        DatabaseManager.TaskCursor taskCursor = manager.queryCompletedTaskFromPj(project_id);
        List<TaskBean> list = new ArrayList<>();
        while (taskCursor.moveToNext()) {
            list.add(taskCursor.getTaskBean());
        }
        taskCursor.close();
        return list;
    }
}
