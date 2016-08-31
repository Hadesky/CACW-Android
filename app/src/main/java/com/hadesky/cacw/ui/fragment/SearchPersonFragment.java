package com.hadesky.cacw.ui.fragment;

import android.support.v4.app.Fragment;

import com.hadesky.cacw.R;
import com.hadesky.cacw.presenter.SearchPresenter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchPersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchPersonFragment extends SearchFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_person;
    }

    @Override
    protected SearchPresenter createPresenter() {
        // TODO: 2016/8/31 0031 ps
        return null;//new SearchPersonPresenterImpl(this,getContext());
    }

    public void updateSearchKey(String searchKey) {
        if (searchKey != null && searchKey.length() != 0) {
            mPresenter.search(searchKey);
        }
    }
}
