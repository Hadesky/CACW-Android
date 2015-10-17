package com.hadesky.cacw.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewTreeObserver;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MenbersAdapter;
import com.hadesky.cacw.bean.MemberBean;
import com.hadesky.cacw.widget.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<MemberBean> members;
    private float recyclerViewWidth;

    @Override
    public int getLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_project_detail);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerViewWidth = recyclerView.getWidth();
            }
        });

        initData();
    }

    private void initData() {
        members = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            members.add(new MemberBean("用户" + i, R.drawable.default_user_image));
        }
        if (isAbleToAdd()) {
            members.add(new MemberBean("", R.drawable.ic_action_add, MemberBean.TYPE_ADD));
        }
        if (isAbleToRed()) {
            members.add(new MemberBean("", R.drawable.ic_action_add, MemberBean.TYPE_REDUCE));
        }
    }

    @Override
    public void setupView() {
        recyclerView.setAdapter(new MenbersAdapter(members, context));
        recyclerView.setLayoutManager(new FullyGridLayoutManager(context, getSpanCount()));
    }

    private int getSpanCount() {
        int member_size = (int) getResources().getDimension(R.dimen.member_size);
        System.out.println("size = " + member_size + "width = " + recyclerViewWidth);
        //TODO
        return 4;
    }

    //TODO
    public boolean isAbleToAdd() {
        return true;
    }

    //TODO
    public boolean isAbleToRed() {
        return false;
    }
}
