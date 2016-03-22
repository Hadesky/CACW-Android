package com.hadesky.cacw.presenter;

import com.hadesky.cacw.Model.MyTeamModel;
import com.hadesky.cacw.ui.View.MyTeamView;

/**
 *
 * Created by dzysg on 2016/3/22 0022.
 */
public class MyTeamPresenterImpl implements MyteamPresenter
{


    MyTeamModel mModel;
    MyTeamView mTeamView;



    public MyTeamPresenterImpl(MyTeamView TeamView)
    {
        mModel = new MyTeamModel();
        mTeamView = TeamView;
    }

    @Override
    public void LoadAllTeams()
    {

    }
}
