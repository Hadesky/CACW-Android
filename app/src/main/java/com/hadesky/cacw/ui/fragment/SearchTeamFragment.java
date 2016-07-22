package com.hadesky.cacw.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SearchTeamAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.SearchPersonOrTeamPresenter;
import com.hadesky.cacw.presenter.SearchTeamPresenterImpl;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchTeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchTeamFragment extends Fragment implements SearchPersonOrTeamView<TeamBean>{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SEARCH_KEY = "search_key";

    private SearchPersonOrTeamPresenter mPresenter;

    private SearchTeamAdapter mAdapter;

    public SearchTeamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param key 搜索的关键字
     * @return A new instance of fragment SearchTeamFragment.
     */
    public static SearchTeamFragment newInstance(String key) {
        SearchTeamFragment fragment = new SearchTeamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SearchTeamPresenterImpl(this);
        if (getArguments() != null) {
            String searchKey = getArguments().getString(ARG_SEARCH_KEY);
            updateSearchKey(searchKey);
        }
    }

    public void updateSearchKey(String searchKey) {
        if (searchKey != null && searchKey.length() != 0) {
            mPresenter.search(searchKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_team, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        mAdapter = new SearchTeamAdapter(null, R.layout.item_team_in_search, new BaseViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                showProgress();
                mPresenter.showNextResults();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void setData(List<TeamBean> data, boolean isFinal) {
        if (mAdapter != null) {
            mAdapter.setData(data, isFinal);
        }
    }

    @Override
    public void addData(List<TeamBean> data,boolean isFinal) {
        if (mAdapter != null) {
            mAdapter.addData(data, isFinal);
        }
    }

    @Override
    public void showProgress() {
        if (mAdapter != null) {
            mAdapter.showProgress();
        }
    }

    @Override
    public void hideProgress() {
        if (mAdapter != null) {
            mAdapter.hideProgress();
        }
    }

    @Override
    public void showMsg(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
