package com.hadesky.cacw.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.LatestNewsAdapter;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.database.DatabaseManager;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.widget.StickView;
import com.hadesky.cacw.util.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

// TODO: 2016/7/10 0010 项目没有成员，需要大改
public class ProjectDetailActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView membersRecyclerView;
    private RecyclerView latestNewsRecyclerView;
    //private EditableMembersAdapter editableMembersAdapter;
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
        latestNewsRecyclerView = (RecyclerView) findViewById(R.id.rv_latest_news);

        allTaskStick = (StickView) findViewById(R.id.stick_all);
        doneTaskStick = (StickView) findViewById(R.id.stick_done);
        undoneTaskStick = (StickView) findViewById(R.id.stick_undone);


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

        //设置阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float elevation = getResources().getDimension(R.dimen.elevation_normal);
            membersRecyclerView.setElevation(elevation);
            allTaskStick.setElevation(elevation);
        }

//        editableMembersAdapter = new EditableMembersAdapter(members, context, new EditableMembersAdapter.OnMemberDeleteListener() {
//            @Override
//            public boolean onMemberDelete(UserBean user) {
//                //manager.deleteUserFromProject(user_id, projectId);
//                return true;
//            }
//        });
//
//        editableMembersAdapter.setAbleToDelete(true);
//        editableMembersAdapter.setAbleToAdd(true);
        setupSpanCount();
        //membersRecyclerView.setAdapter(editableMembersAdapter);
        latestNewsRecyclerView.setAdapter(new LatestNewsAdapter(context));
        latestNewsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
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
//        if (editableMembersAdapter.getMode() == EditableMembersAdapter.MODE_DELETE) {
//            editableMembersAdapter.setMode(EditableMembersAdapter.MODE_NORMAL);
//        } else {
            super.onBackPressed();
//        }
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
