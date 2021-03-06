package com.hadesky.cacw.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.SearchTaskAdapter;
import com.hadesky.cacw.adapter.ViewPagerAdapter;
import com.hadesky.cacw.presenter.SearchPresenter;
import com.hadesky.cacw.ui.fragment.SearchFragment;
import com.hadesky.cacw.ui.fragment.SearchPersonFragment;
import com.hadesky.cacw.ui.fragment.SearchTeamFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,SearchFragment.OnFragmentLoadingListener {

    private EditText mSearchEditText;
    private FragmentManager mFragmentManager;
    private SwipeRefreshLayout mRefreshLayout;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private SearchPresenter mSearchTaskPresenter;//人和团队的present在各自的fragment里面
    private SearchTaskAdapter mSearchTaskAdapter;
    private RecyclerView mSearchTaskRecyclerView;

    private int mLoadingFragmentCount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mSearchEditText = (EditText) findViewById(R.id.et);
        mFragmentManager = getSupportFragmentManager();
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //mSearchTaskPresenter = new SearchTaskPresenterImpl(this, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setupView() {
        List<View> views = new ArrayList<>();
        View personOrTeamView = View.inflate(this, R.layout.layout_search_person_team, null);
        View taskView = View.inflate(this, R.layout.layout_search_task, null);
        views.add(personOrTeamView);
        views.add(taskView);
        ViewPagerAdapter adapter = new ViewPagerAdapter(views);
        adapter.setTitles(new String[]{"人或团队", "任务"});
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        View view = findViewById(R.id.iv_arrow_back);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    hideAllFragment();
                }
                loadSearchPersonFragment(s.toString());
                loadSearchTeamFragment(s.toString());
                //loadSearchTask(s.toString());
            }
        });
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(ResourcesCompat.getColor(getResources(), R.color.color_primary, null));
        mRefreshLayout.setProgressViewOffset(true, -100, 50);
    }

    private void loadSearchTask(String s) {
        if (mSearchTaskRecyclerView == null) {
            mSearchTaskRecyclerView = (RecyclerView) findViewById(R.id.rv);
            mSearchTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            if (mSearchTaskAdapter != null) {
                mSearchTaskRecyclerView.setAdapter(mSearchTaskAdapter);
            }
        }
        mSearchTaskPresenter.search(s);
    }

    private void hideAllFragment() {
        if (mFragmentManager.getFragments() != null) {
            for (Fragment fragment : mFragmentManager.getFragments()) {
                mFragmentManager.beginTransaction().hide(fragment).commit();
            }
        }
    }

    private void loadSearchTeamFragment(String s) {
        SearchTeamFragment fragment = (SearchTeamFragment) getTeamFragment();
        if (fragment == null) {
            fragment = SearchTeamFragment.newInstance(SearchTeamFragment.class, s);
            mFragmentManager.beginTransaction()
                    .add(R.id.container_team, fragment)
                    .commit();
        } else {
            fragment.updateSearchKey(s);
        }
    }

    private void loadSearchPersonFragment(String s) {
        SearchPersonFragment fragment = (SearchPersonFragment) getPersonFragment();
        if (fragment == null) {
            fragment = SearchPersonFragment.newInstance(SearchPersonFragment.class, s);
            mFragmentManager.beginTransaction()
                    .add(R.id.container_person, fragment)
                    .commit();
        } else {
            fragment.updateSearchKey(s);
        }
    }

    private Fragment getPersonFragment() {
        return mFragmentManager.findFragmentById(R.id.container_person);
    }

    private Fragment getTeamFragment() {
        return mFragmentManager.findFragmentById(R.id.container_team);
    }

    @Override
    public void onRefresh() {
        refreshAllResult();
    }

    private void refreshAllResult() {
        if (mFragmentManager.getFragments() != null) {
            for (Fragment fragment : mFragmentManager.getFragments()) {
                if (fragment instanceof SearchFragment) {
                    ((SearchFragment) fragment).updateSearchKey(mSearchEditText.getText().toString());
                }
            }
        } else {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onFragmentLoadingStart() {
        mLoadingFragmentCount++;
        if (mRefreshLayout != null && !mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onFragmentLoadingEnd() {
        mLoadingFragmentCount--;
        if (mRefreshLayout != null && mLoadingFragmentCount <= 0) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    public void setSearchTaskAdapter(SearchTaskAdapter adapter) {
        mSearchTaskAdapter = adapter;
    }
}
