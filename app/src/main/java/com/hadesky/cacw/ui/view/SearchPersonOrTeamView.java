package com.hadesky.cacw.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.hadesky.cacw.bean.UserBean;

import java.util.List;

/**
 * Created by 45517 on 2016/7/22.
 */
public interface SearchPersonOrTeamView<Bean> extends BaseView {

    void setAdapter(RecyclerView.Adapter adapter);

    void hide();

    void show();
}