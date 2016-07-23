package com.hadesky.cacw.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.presenter.SearchPersonOrTeamPresenter;
import com.hadesky.cacw.presenter.SearchTeamPresenterImpl;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

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
        return new SearchTeamPresenterImpl(this);
    }
}
