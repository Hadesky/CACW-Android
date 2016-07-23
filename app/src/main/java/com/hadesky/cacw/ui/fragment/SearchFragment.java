package com.hadesky.cacw.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hadesky.cacw.R;
import com.hadesky.cacw.presenter.SearchPersonOrTeamPresenter;
import com.hadesky.cacw.ui.view.SearchPersonOrTeamView;

/**
 * Created by 45517 on 2016/7/23.
 */
public abstract class SearchFragment extends Fragment implements SearchPersonOrTeamView{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SEARCH_KEY = "search_key";

    private OnFragmentLoadingListener mListener;

    private RecyclerView.Adapter mAdapter;

    private SearchPersonOrTeamPresenter mPresenter;

    public SearchFragment() {
    }

    /**
     * @param searchKey 需要查找的Key.
     * @return A new instance of fragment SearchPersonFragment.
     */
    public abstract SearchFragment newInstance(String searchKey);


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
