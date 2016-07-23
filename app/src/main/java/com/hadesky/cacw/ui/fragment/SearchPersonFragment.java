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
public class SearchPersonFragment extends Fragment implements SearchPersonOrTeamView<UserBean> {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SEARCH_KEY = "search_key";

    private SearchPersonOrTeamPresenter mPresenter;

    private RecyclerView.Adapter mAdapter;

    private OnFragmentLoadingListener mListener;

    public SearchPersonFragment() {
        // Required empty public constructor
    }

    /**
     * @param searchKey 需要查找的Key.
     * @return A new instance of fragment SearchPersonFragment.
     */
    public static SearchPersonFragment newInstance(String searchKey) {
        SearchPersonFragment fragment = new SearchPersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_KEY, searchKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SearchPersonPresenterImpl(this);
        if (getArguments() != null) {
            String searchKey = getArguments().getString(ARG_SEARCH_KEY);
            updateSearchKey(searchKey);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_person, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        if (mAdapter != null) {
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    public void updateSearchKey(String searchKey) {
        if (searchKey != null && searchKey.length() != 0) {
            mPresenter.search(searchKey);
        }
    }

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
    public void showMsg(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
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
}
