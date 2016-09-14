package com.hadesky.cacw.test;

import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.model.DB.DatabaseManager;

/**
 *
 * Created by dzysg on 2015/10/29 0029.
 */
public class testData
{
    public static void createTestData()
    {
        //模拟数据
        DatabaseManager manager = DatabaseManager.getInstance(MyApp.getAppContext());

//        for (int i = 0; i < SimData.team_list.length; i++) {
//            manager.insertTeam(new TeamBean(SimData.team_list[i], i));
//        }
//
//        for (int i = 0; i < SimData.user_list.length; i++) {
//            manager.insertUser(new UserBean(SimData.user_list[i], i));
//        }
//
//        //1 team的project
//        for (int i = 0; i < SimData.project_list.length / 2; i++) {
//            manager.insertProject(new ProjectBean(SimData.project_list[i], i, 0));
//        }
//
//        //2 team的project
//        for (int i = SimData.project_list.length / 2; i < SimData.project_list.length; i++) {
//            manager.insertProject(new ProjectBean(SimData.project_list[i], i, 1));
//        }
//
//        for (int i = 0; i < SimData.task_list.length; i++) {
//            TaskBean bean = new TaskBean(SimData.task_list[i], i,1);
//            bean.setContent(SimData.task_content[i]);
//            bean.setLocation(SimData.task_location[i]);
//            manager.insertTask(bean);
//        }
//        //把用户插进Task
//
//        for (int i = 0, j = 0; i < SimData.task_list.length; i++) {
//            for (int k = 0; k < 10 && j < SimData.user_list.length; j++, k++) {
//                manager.putUserIntoTask(j, i);
//            }
//        }
//        //把用户插进project
//        for (int i = 0, j = 0; i < SimData.project_list.length; i++) {
//            for (int k = 0; k < 15 && j < SimData.user_list.length; j++, k++) {
//                manager.putUserIntoProject(j, i);
//            }
//        }
//        //手动把id为0的USER放进project1和2
//        manager.putUserIntoProject(0, 1);
//        manager.putUserIntoProject(0, 2);
    }
}
