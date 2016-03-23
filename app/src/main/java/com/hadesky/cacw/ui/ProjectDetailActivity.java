package com.hadesky.cacw.ui;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.EditableMembersAdapter;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.database.DatabaseManager;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.util.FullyGridLayoutManager;
import com.hadesky.cacw.widget.StickView;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView membersRecyclerView;
    private RecyclerView taskRecyclerView;
    private EditableMembersAdapter editableMembersAdapter;
    private List<UserBean> members;
    private StickView allTaskStick;
    private StickView doneTaskStick;
    private StickView undoneTaskStick;

    private DatabaseManager manager;
    private long projectId;


    @Override
    public int getLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void initView() {
        membersRecyclerView = (RecyclerView) findViewById(R.id.rv_members);

        allTaskStick = (StickView) findViewById(R.id.stick_all);
        doneTaskStick = (StickView) findViewById(R.id.stick_done);
        undoneTaskStick = (StickView) findViewById(R.id.stick_undone);

        taskRecyclerView = (RecyclerView) findViewById(R.id.rv_task);

        initData();
    }

    private void initData() {
        manager = DatabaseManager.getInstance(getApplicationContext());
        projectId = getIntent().getLongExtra(IntentTag.TAG_PROJECT_ID, -1);
        if (projectId == -1) {
            return;
        }

        DatabaseManager manager = DatabaseManager.getInstance(getApplicationContext());

        //查询操作
        DatabaseManager.UserCursor userCursor = manager.queryUserFromProject(projectId);
        userCursor.moveToFirst();
        members = new ArrayList<>();
        for (int i = 0; i < userCursor.getCount(); i++) {
            members.add(userCursor.getUserBean());
            userCursor.moveToNext();
        }
        userCursor.close();

    }

    @Override
    public void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra(IntentTag.TAG_PROJECT_NAME));
        }

        editableMembersAdapter = new EditableMembersAdapter(members, context, new EditableMembersAdapter.OnMemberDeleteListener() {
            @Override
            public void onMemberDelete(long user_id) {
                manager.deleteUserFromProject(user_id, projectId);
            }
        });

        editableMembersAdapter.setAbleToDelete(true);
        editableMembersAdapter.setAbleToAdd(true);
        setupSpanCount();
        membersRecyclerView.setAdapter(editableMembersAdapter);

        setupStickView();
    }

    private void setupStickView() {
        allTaskStick.setTaskCount(10);
        doneTaskStick.setTaskCount(3);
        undoneTaskStick.setTaskCount(7);
        allTaskStick.setOnClickListener(this);
        doneTaskStick.setOnClickListener(this);
        undoneTaskStick.setOnClickListener(this);
    }



    private void setupSpanCount() {
        membersRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                int member_size = (int) getResources().getDimension(R.dimen.member_size);
                int width = membersRecyclerView.getWidth();
                membersRecyclerView.setLayoutManager(new FullyGridLayoutManager(context, width / member_size));
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

    @Override
    public void onClick(View v) {
        if(v!=null){
            Intent intent;
            switch (v.getId()) {
                case R.id.stick_all:
                    intent = new Intent(context,TaskListActivity.class);
                    intent.putExtra(IntentTag.TAG_TASK_STATUS, R.string.all_task);
                    startActivity(intent);
                    break;
                case R.id.stick_done:
                    intent = new Intent();
                    intent.putExtra(IntentTag.TAG_TASK_STATUS, R.string.done_task);
                    startActivity(new Intent(context, TaskListActivity.class));
                    break;
                case R.id.stick_undone:
                    intent = new Intent();
                    intent.putExtra(IntentTag.TAG_TASK_STATUS, R.string.undone_task);
                    startActivity(new Intent(context, TaskListActivity.class));
                    break;
            }
        }
    }
}
