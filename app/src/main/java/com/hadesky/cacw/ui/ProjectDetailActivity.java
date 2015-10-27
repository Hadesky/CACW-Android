package com.hadesky.cacw.ui;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.MembersAdapter;
import com.hadesky.cacw.bean.MemberBean;
import com.hadesky.cacw.util.FullyGridLayoutManager;
import com.hadesky.cacw.widget.StickView;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private MembersAdapter membersAdapter;
    private List<MemberBean> members;
    private StickView allTaskStick;
    private StickView doneTaskStick;
    private StickView undoTaskStick;


    @Override
    public int getLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_members);

        allTaskStick = (StickView) findViewById(R.id.stick_all);
        undoTaskStick = (StickView) findViewById(R.id.stick_undo);
        doneTaskStick = (StickView) findViewById(R.id.stick_done);

        initData();
    }

    private void initData() {
        members = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            members.add(new MemberBean("用户" + i, R.drawable.default_user_image));
        }
    }

    @Override
    public void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.project_detail);
        }

        membersAdapter = new MembersAdapter(members, context);

        membersAdapter.setAbleToDelete(true);
        membersAdapter.setAbleToAdd(true);

        recyclerView.setAdapter(membersAdapter);
        setupSpanCount();

        setupStickView();
    }

    private void setupStickView() {
        allTaskStick.setTaskCount(10);
        undoTaskStick.setTaskCount(7);
        doneTaskStick.setTaskCount(3);
        allTaskStick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, TaskListActivity.class));
            }
        });
    }

    private void setupSpanCount() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                int member_size = (int) getResources().getDimension(R.dimen.member_size);
                int width = recyclerView.getWidth();
                recyclerView.setLayoutManager(new FullyGridLayoutManager(context, width / member_size));
            }
        });
    }


    //TODO
    public boolean isAbleToAdd() {
        return true;
    }

    //TODO
    public boolean isAbleToDelete() {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (membersAdapter.getMode() == MembersAdapter.MODE_DELETE) {
            membersAdapter.setMode(MembersAdapter.MODE_NORMAL);
        } else {
            super.onBackPressed();
        }
    }
}