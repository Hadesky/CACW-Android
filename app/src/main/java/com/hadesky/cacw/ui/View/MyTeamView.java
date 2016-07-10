package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.TeamMember;

import java.util.List;

/**
 *
 * Created by dzysg on 2016/3/21 0021.
 */
public interface MyTeamView
{
    void showTeamList(List<TeamMember> list);
    void showProgressBar(boolean visibility);
    void showMessage(String msg);

}
