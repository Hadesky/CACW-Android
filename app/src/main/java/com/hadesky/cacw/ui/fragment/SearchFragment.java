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
import com.hadesky.cacw.presenter.SearchPersonOrTeamPresenter;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

/**
 *
 * Created by 45517 on 2016/7/23.
 */
public abstract class SearchFragment<Bean> extends Fragment implements SearchPersonOrTeamView<Bean>{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    protected static final String ARG_SEARCH_KEY = "search_key";

    protected OnFragmentLoadingListener mListener;

    protected RecyclerView.Adapter mAdapter;

    protected SearchPersonOrTeamPresenter mPresenter;

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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        if (mAdapter != null) {
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    protected abstract int getLayoutId();


    protected abstract SearchPersonOrTeamPresenter createPresenter();

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
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
    public void showMsg(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentLoadingListener {
        void onFragmentLoadingStart();

        void onFragmentLoadingEnd();
    }

    public abstract void updateSearchKey(String searchKey);
}
