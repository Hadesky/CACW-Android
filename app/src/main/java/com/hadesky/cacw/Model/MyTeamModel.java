package com.hadesky.cacw.model;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.database.TeamDao;

import java.util.List;

/**
 * 我的团队model
 * Created by dzysg on 2016/3/22 0022.
 */
public class MyTeamModel
{

    TeamDao mDao;
    public MyTeamModel()
    {
        mDao = new TeamDao();
    }


    public List<TeamBean> getAllTeams()
    {
       return mDao.queryTeams();
    }



}
