package com.hadesky.cacw.ui.view;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/**
 *
 * Created by dzysg on 2016/7/12 0012.
 */
public interface TeamInfoView extends BaseView {

    void showMembers(List<UserBean> list);
    void showInfo(TeamBean bean);
    void close();
}
