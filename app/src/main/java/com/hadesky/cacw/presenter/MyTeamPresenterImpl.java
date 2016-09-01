package com.hadesky.cacw.presenter;

import com.hadesky.cacw.ui.view.MyTeamView;

/**
 *
 * Created by dzysg on 2016/9/1 0001.
 */
public class MyTeamPresenterImpl implements MyTeamPresenter
{
    MyTeamView mView;

    public MyTeamPresenterImpl(MyTeamView view)
    {
        mView = view;
    }

    @Override
    public void LoadAllTeams()
    {

    }

    @Override
    public void onDestroy()
    {

    }
}
