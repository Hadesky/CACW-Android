package com.hadesky.cacw.database;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.config.MyApp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by dzysg on 2016/3/22 0022.
 */
public class TeamDao
{
    DatabaseManager mManager;

    public TeamDao()
    {
        mManager = DatabaseManager.getInstance(MyApp.getAppContext());
    }


    public List<TeamBean> queryTeams()
    {
        List<TeamBean> list = new ArrayList<>();
        DatabaseManager.TeamCursor cursor = mManager.queryAllTeam();
        while (cursor.moveToNext())
        {
            list.add(cursor.getTaskBean());
        }
        cursor.close();
        return list;
    }




}
