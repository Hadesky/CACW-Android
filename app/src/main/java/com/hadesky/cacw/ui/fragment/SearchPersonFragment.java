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
import com.hadesky.cacw.adapter.SearchPersonAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.SearchPersonPresenter;
import com.hadesky.cacw.presenter.SearchPersonPresenterImpl;
import com.hadesky.cacw.ui.view.SearchPersonView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchPersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchPersonFragment extends Fragment implements SearchPersonView {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SEARCH_KEY = "search_key";

    private RecyclerView mRecyclerView;

    private SearchPersonAdapter mAdapter;

    private SearchPersonPresenter mPresenter;

//    private OnFragmentInteractionListener mListener;

    public SearchPersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_person, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);

        mAdapter = new SearchPersonAdapter(null, R.layout.item_person_in_search, new BaseViewHolder.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                showProgress();
                mPresenter.showNextResults();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public void updateSearchKey(String searchKey) {
        if (searchKey != null && searchKey.length() != 0) {
            mPresenter.search(searchKey);
        }
    }

    @Override
    public void setData(List<UserBean> data, boolean isFinal) {
        if (mAdapter != null) {
            mAdapter.setData(data, isFinal);
        }
    }

    @Override
    public void addData(List<UserBean> data,boolean isFinal) {
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
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
