package com.hadesky.cacw.ui.fragment;

import android.support.v4.app.Fragment;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.SearchPresenter;
import com.hadesky.cacw.presenter.SearchPersonPresenterImpl;

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
        return new SearchPersonPresenterImpl(this,getContext());
    }

    public void updateSearchKey(String searchKey) {
        if (searchKey != null && searchKey.length() != 0) {
            mPresenter.search(searchKey);
        }
    }
}
