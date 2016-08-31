package com.hadesky.cacw.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.widget.StickView;

// TODO: 2016/7/10 0010 项目没有成员，需要大改
public class ProjectDetailActivity extends BaseActivity implements View.OnClickListener{

    private StickView allTaskStick;
    private StickView doneTaskStick;
    private StickView undoneTaskStick;

    private ProjectBean mProjectBean;


    @Override
    public int getLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void initView() {

        allTaskStick = (StickView) findViewById(R.id.stick_all);
        doneTaskStick = (StickView) findViewById(R.id.stick_done);
        undoneTaskStick = (StickView) findViewById(R.id.stick_undone);

        initData();
    }

    private void initData() {
        mProjectBean = (ProjectBean) getIntent().getSerializableExtra(IntentTag.TAG_PROJECT_BEAN);
        if (mProjectBean == null) {
            finish();
        }
    }

    @Override
    public void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mProjectBean.getName());
        }

        //设置阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float elevation = getResources().getDimension(R.dimen.elevation_normal);
            allTaskStick.setElevation(elevation);
        }

//        editableMembersAdapter = new EditableMembersAdapter(members, context, new EditableMembersAdapter.OnMemberEditListener() {
//            @Override
//            public boolean onMemberDelete(UserBean user) {
//                //manager.deleteUserFromProject(user_id, projectId);
//                return true;
//            }
//        });
//
//        editableMembersAdapter.setAbleToDelete(true);
//        editableMembersAdapter.setAbleToAdd(true);
        //membersRecyclerView.setAdapter(editableMembersAdapter);
//        latestNewsRecyclerView.setAdapter(new LatestNewsAdapter(context));
//        latestNewsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if(v!=null){
            Intent intent;
            intent = new Intent(context,TaskListActivity.class);
            switch (v.getId()) {
                case R.id.stick_all:
                    intent.putExtra(IntentTag.TAG_TASK_STATUS, getString(R.string.all_task));
                    startActivity(intent);
                    break;
                case R.id.stick_done:
                    intent.putExtra(IntentTag.TAG_TASK_STATUS, getString(R.string.done_task));
                    startActivity(intent);
                    break;
                case R.id.stick_undone:
                    intent.putExtra(IntentTag.TAG_TASK_STATUS, getString(R.string.undone_task));
                    startActivity(intent);
                    break;
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
