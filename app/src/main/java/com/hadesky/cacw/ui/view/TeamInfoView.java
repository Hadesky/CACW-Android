package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.TeamMember;

import java.util.List;

/**
 *
 * Created by dzysg on 2016/7/12 0012.
 */
public interface TeamInfoView extends BaseView {

    void showMembers(List<TeamMember> list);
    void showProjectCount(int num);
    void showInfo();
}
