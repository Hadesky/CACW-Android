package com.hadesky.cacw.presenter;

import java.io.File;

/**
 *
 * Created by dzysg on 2016/7/12 0012.
 */
public interface TeamInfoPresenter {

    void getTeamMembers();
    void changeeSummary(String s);
    void saveTeamIcon(File file);
    void onDestory();
    void getProjectCount();
}
