package com.hadesky.cacw.ui.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.ProjectAdapter;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.presenter.MyProjectPresenter;
import com.hadesky.cacw.presenter.MyProjectPresenterImpl;
import com.hadesky.cacw.ui.view.MyProjectView;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目页面
 * Created by Bright Van on 2015/9/7/007.
 */
public class ProjectFragment extends BaseFragment implements MyProjectView {
    private RecyclerView recyclerView;
    private ProjectAdapter mAdapter;
    private MyProjectPresenter myProjectPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static final String TeamBundleTAG = "team";
    public static final String FregmentTAG = "team";


    @Override
    public int getLayoutId() {
        return R.layout.fragment_project;
    }

    @Override
    protected void initViews(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_project);
        mAdapter = new ProjectAdapter(new ArrayList<ProjectBean>(), R.layout.list_item_project);
    }


    @Override
    protected void setupViews(Bundle bundle) {

        TeamBean teamBean = null;

        if (getArguments() != null)
            teamBean = (TeamBean) getArguments().getSerializable(TeamBundleTAG);

        // teamBean为null表示这是个人的所有项目，不为Null表示这是团队的项目
        myProjectPresenter = new MyProjectPresenterImpl(this, teamBean);

        myProjectPresenter.loadProject();

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_primary));
        swipeRefreshLayout.setProgressViewOffset(true, -100, 50);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myProjectPresenter.loadProject();
            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new Decoration(getContext()));
    }

    public void refresh() {
        myProjectPresenter.loadProject();
    }

    @Override
    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMsg(String s) {
        showToast(s);
    }

    @Override
    public void showProject(List<ProjectBean> data) {
        mAdapter.setDatas(data);
    }

    @Override
    public void onFailure(String msg) {
        showToast(msg);
    }


    /**
     * 自定义分割线
     */
    public class Decoration extends RecyclerView.ItemDecoration {
        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };
        //分隔线
        private Drawable mDecoration;


        public Decoration(Context context) {
            mDecoration = context.obtainStyledAttributes(ATTRS).getDrawable(0);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View view = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
                final int top = view.getBottom() + params.bottomMargin;
                final int bottom = top + mDecoration.getIntrinsicHeight();
                mDecoration.setBounds(left, top, right, bottom);
                mDecoration.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, 0, mDecoration.getIntrinsicHeight());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myProjectPresenter.onDestroy();
    }
}
