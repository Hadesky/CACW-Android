package com.hadesky.cacw.ui.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.ProjectAdapter;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.database.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Bright Van on 2015/9/7/007.
 */
public class ProjectFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ProjectAdapter mAdapter;
    private List<ProjectBean> mData;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_project;
    }

    @Override
    protected void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_project);
        initData();
        mAdapter = new ProjectAdapter(getContext(), mData);
    }

    private void initData() {
        DatabaseManager manager = DatabaseManager.getInstance(getContext());

        //查询操作
        DatabaseManager.ProjectCursor projectCursor = manager.queryProjectByUserId(0);//0换为当前用户的UserID
        projectCursor.moveToFirst();
        mData = new ArrayList<>();
        for (int i = 0; i < projectCursor.getCount(); i++) {
            mData.add(projectCursor.getProjectBean());
            projectCursor.moveToNext();
        }
    }

    @Override
    protected void setupViews(Bundle bundle) {
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new Decoration(getContext()));
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
}
