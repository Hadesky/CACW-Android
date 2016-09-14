package com.hadesky.cacw.ui.view;

import android.support.v7.widget.RecyclerView;

/**
 * Created by 45517 on 2016/7/22.
 */
public interface SearchPersonOrTeamView extends BaseView {

    void setAdapter(RecyclerView.Adapter adapter);

    void hide();

    void show();
}
