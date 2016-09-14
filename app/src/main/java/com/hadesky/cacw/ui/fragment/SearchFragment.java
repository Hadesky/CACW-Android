package com.hadesky.cacw.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.presenter.SearchPresenter;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

/**
 *
 * Created by 45517 on 2016/7/23.
 */
public abstract class SearchFragment extends Fragment implements SearchPersonOrTeamView{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    protected static final String ARG_SEARCH_KEY = "search_key";

    protected OnFragmentLoadingListener mListener;

    protected RecyclerView.Adapter mAdapter;

    protected SearchPresenter mPresenter;

    protected RecyclerView mRecyclerView;

    public SearchFragment() {
    }

    /**
     * @param searchKey 需要查找的Key.
     * @return A new instance of fragment SearchPersonFragment，可能为null
     */
    @Nullable
    public static <T extends SearchFragment> T newInstance(Class<T> subClass, String searchKey) {
        T fragment = null;
        try {
            fragment = subClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_KEY, searchKey);
        if (fragment != null) {
            fragment.setArguments(args);
        }

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (getArguments() != null) {
            String searchKey = getArguments().getString(ARG_SEARCH_KEY);
            updateSearchKey(searchKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        if (mAdapter != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    protected abstract int getLayoutId();


    protected abstract SearchPresenter createPresenter();

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showProgress() {
        mListener.onFragmentLoadingStart();
    }

    @Override
    public void hideProgress() {
        mListener.onFragmentLoadingEnd();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentLoadingListener) {
            mListener = (OnFragmentLoadingListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentLoadingListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    public void show() {
        getFragmentManager().beginTransaction().show(this).commit();
    }

    @Override
    public void hide() {
        getFragmentManager().beginTransaction().hide(this).commit();
    }

    @Override
    public void showMsg(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }


    public interface OnFragmentLoadingListener {
        void onFragmentLoadingStart();

        void onFragmentLoadingEnd();
    }

    public abstract void updateSearchKey(String searchKey);
}
