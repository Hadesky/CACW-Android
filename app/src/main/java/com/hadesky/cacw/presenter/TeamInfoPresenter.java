package com.hadesky.cacw.presenter;

import java.io.File;

/**
 *
 * Created by dzysg on 2016/7/12 0012.
 */
public interface TeamInfoPresenter {
    void getTeamInfo();
    void getTeamMembers();
    void modifySummary(String s);
    void modifyNotice(String s);

    void saveTeamIcon(File file);
    void onDestroy();
    void getProjectCount();

    void delCurrentTeam();

    void exitTeam();
}
