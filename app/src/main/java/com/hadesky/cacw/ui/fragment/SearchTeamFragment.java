package com.hadesky.cacw.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.SearchPersonOrTeamPresenter;
import com.hadesky.cacw.presenter.SearchTeamPresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchTeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchTeamFragment extends SearchFragment<TeamBean> {

    public void updateSearchKey(String searchKey) {
        if (searchKey != null && searchKey.length() != 0) {
            mPresenter.search(searchKey);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_team;
    }

    @Override
    protected SearchPersonOrTeamPresenter createPresenter() {
        return new SearchTeamPresenterImpl(this, getContext());
    }

    public void disableJoinButton(int position) {
        if (position >= 0 && position < mRecyclerView.getChildCount()) {
            View view = mRecyclerView.getChildAt(position);
            Button button = (Button) view.findViewById(R.id.bt_join);
            if (button != null) {
                button.setSelected(true);
                button.setEnabled(false);
            }
        }
    }
}
