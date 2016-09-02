package com.hadesky.cacw.ui.view;

import android.support.v7.widget.RecyclerView;

/**
 *
 * Created by MicroStudent on 2016/7/16.
 */

public interface TeamMemberView extends BaseView {

    void setAdapter(RecyclerView.Adapter adapter);

    void addItemDecoration(RecyclerView.ItemDecoration decoration);

    void setData(Object[] data);
}
