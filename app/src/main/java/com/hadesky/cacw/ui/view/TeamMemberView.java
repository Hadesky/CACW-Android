package com.hadesky.cacw.ui.view;

import android.support.v7.widget.RecyclerView;

import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/**
 * Created by MicroStudent on 2016/7/16.
 */

public interface TeamMemberView extends BaseView {
    TeamBean getTeamBean();

    void setAdapter(RecyclerView.Adapter adapter);

    void addItemDecoration(RecyclerView.ItemDecoration decoration);

    void setData(Object[] data);
}
