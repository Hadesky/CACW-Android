package com.hadesky.cacw.presenter;

import cn.bmob.v3.BmobQuery;

/**
 *
 * Created by dzysg on 2016/3/22 0022.
 */
public interface MyTeamPresenter
{
    void LoadAllTeams(BmobQuery.CachePolicy policy);
    void onDestroy();
}