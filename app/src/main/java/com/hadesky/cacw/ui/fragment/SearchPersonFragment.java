package com.hadesky.cacw.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.SearchPersonOrTeamPresenter;
import com.hadesky.cacw.presenter.SearchPersonPresenterImpl;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchPersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchPersonFragment extends SearchFragment<UserBean> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_person;
    }

    @Override
    protected SearchPersonOrTeamPresenter createPresenter() {
        return new SearchPersonPresenterImpl(this);
    }

    public void updateSearchKey(String searchKey) {
        if (searchKey != null && searchKey.length() != 0) {
            mPresenter.search(searchKey);
        }
    }
}
