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
import com.hadesky.cacw.adapter.TeamAdapter;
import com.hadesky.cacw.bean.TeamBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bright Van on 2015/9/7/007.
 */
public class TeamFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private TeamAdapter mAdapter;
    private List<TeamBean> mData;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_team;
    }

    @Override
    protected void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.lv_team_List);
        initData();
        mAdapter = new TeamAdapter(getContext(), mData);
    }

    private void initData() {
        mData = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            TeamBean bean = new TeamBean(R.drawable.default_user_image, "我的团队" + i, false);
            mData.add(bean);
        }
    }

    @Override
    protected void setupViews(Bundle bundle) {
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new LinkManDecoration(getContext()));
    }


    /**
     * 自定义分割线
     */
    public class LinkManDecoration extends RecyclerView.ItemDecoration {
        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };
        //分隔线
        private Drawable mDecoration;


        public LinkManDecoration(Context context) {
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
