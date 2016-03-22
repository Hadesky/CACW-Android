package com.hadesky.cacw.ui.View;

import com.hadesky.cacw.bean.TeamBean;

import java.util.List;

/**
 *
 * Created by dzysg on 2016/3/21 0021.
 */
public interface MyTeamView
{
    void showTeamList(List<TeamBean> list);
    void showProgressBar(boolean visibility);
    void showMessage(String msg);
}
