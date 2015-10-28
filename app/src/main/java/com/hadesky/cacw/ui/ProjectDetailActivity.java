package com.hadesky.cacw.ui;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.EditableMembersAdapter;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.database.DataBaseManager;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.util.FullyGridLayoutManager;
import com.hadesky.cacw.widget.StickView;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private EditableMembersAdapter editableMembersAdapter;
    private List<UserBean> members;
    private StickView allTaskStick;
    private StickView doneTaskStick;
    private StickView undoTaskStick;

    private DataBaseManager manager;
    private long projectId;


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
        manager = DataBaseManager.getInstance(context);
        projectId = getIntent().getLongExtra(IntentTag.TAG_PROJECT_ID, 0);

        DataBaseManager manager = DataBaseManager.getInstance(context);

        //查询操作
        DataBaseManager.UserCursor userCursor = manager.queryUserFromProject(projectId);
        userCursor.moveToFirst();
        members = new ArrayList<>();
        for (int i = 0; i < userCursor.getCount(); i++) {
            members.add(userCursor.getUserBean());
            userCursor.moveToNext();
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

        editableMembersAdapter = new EditableMembersAdapter(members, context, new EditableMembersAdapter.OnMemberDeleteListener() {
            @Override
            public void onMemberDelete(long user_id) {
                manager.deleteUserFromProject(user_id, projectId);
            }
        });

        editableMembersAdapter.setAbleToDelete(true);
        editableMembersAdapter.setAbleToAdd(true);

        recyclerView.setAdapter(editableMembersAdapter);
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
        if (editableMembersAdapter.getMode() == EditableMembersAdapter.MODE_DELETE) {
            editableMembersAdapter.setMode(EditableMembersAdapter.MODE_NORMAL);
        } else {
            super.onBackPressed();
        }
    }
}
