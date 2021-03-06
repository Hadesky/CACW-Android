package com.hadesky.cacw.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.base.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.presenter.TeamInfoPresenter;
import com.hadesky.cacw.presenter.TeamInfoPresenterImpl;
import com.hadesky.cacw.tag.IntentTag;
import com.hadesky.cacw.ui.view.TeamInfoView;
import com.hadesky.cacw.ui.widget.ColorfulAnimView.ColorfulAnimView;
import com.hadesky.cacw.ui.widget.PullToZoomBase;
import com.hadesky.cacw.ui.widget.PullToZoomScrollViewEx;
import com.hadesky.cacw.util.BlurProcessor;
import com.hadesky.cacw.util.FileUtil;
import com.hadesky.cacw.util.FullyGridLayoutManager;
import com.hadesky.cacw.util.ImageResizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TeamInfoActivity extends BaseActivity implements TeamInfoView {

    static public final String TeamIconFileName_Low = "team_icon_low.jpg";
    static public final String TeamIconFileName= "team_icon.jpg";
    private TextView mTvTeamName;
    private TextView mTvTeamId;
    private TextView mTvSummary;
    private TextView mTvProjectCount;
    private TextView mTvMemberCount;
    private TextView mTvNotice;
    private RecyclerView mRcvMembers;

    private BaseAdapter<UserBean> mAdapter;
    private TeamInfoPresenter mPresenters;
    private TeamBean mTeam;
    private SimpleDraweeView mSimpleDraweeView;
    private SimpleDraweeView mZoom;
    private ColorfulAnimView mAnimView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_team_info;
    }

    @Override
    public void initView() {
        mTvTeamId = (TextView) findViewById(R.id.tv_teamid);
        mTvTeamName = (TextView) findViewById(R.id.tv_team_name);
        mTvSummary = (TextView) findViewById(R.id.tv_team_summary);
        mAnimView = (ColorfulAnimView) findViewById(R.id.view_anim);
        mRcvMembers = (RecyclerView) findViewById(R.id.rcv_team_member);
        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.sdv_team_icon);
        mZoom = (SimpleDraweeView) findViewById(R.id.iv_zoom);
        mTvProjectCount = (TextView) findViewById(R.id.tv_project_count);
        mTvMemberCount = (TextView) findViewById(R.id.tv_team_member_count);
        mTvNotice = (TextView) findViewById(R.id.tv_team_notice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null)
            setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }

    @Override
    public void setupView() {
        //隐藏状态栏
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        //从Intent载入信息
        loadTeamBeanFromIntent();

        showInfo();

        mAdapter = new BaseAdapter<UserBean>(new ArrayList<UserBean>(), R.layout.list_item_member) {
            @Override
            public BaseViewHolder<UserBean> createHolder(View v, Context context) {
                BaseViewHolder<UserBean> viewHolder = new BaseViewHolder<UserBean>(v) {
                    @Override
                    public void setData(UserBean o) {
                        setTextView(R.id.tv, o.getNickName());
                        if (o.getAvatarUrl() != null) {
                            SimpleDraweeView avatar = findView(R.id.iv_avatar);
                            avatar.setImageURI(o.getAvatarUrl());
                        }
                    }
                };
                viewHolder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        Intent intent = new Intent(TeamInfoActivity.this, UserInfoActivity.class);
                        intent.putExtra(com.hadesky.cacw.tag.IntentTag.TAG_USER_BEAN, (Parcelable)mDatas.get(position));
                        startActivity(intent);
                    }
                });
                return viewHolder;
            }
        };

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 5);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRcvMembers.setLayoutManager(manager);
        mRcvMembers.setVerticalFadingEdgeEnabled(false);
        mRcvMembers.setAdapter(mAdapter);

        //Presenters 加载数据
        mPresenters = new TeamInfoPresenterImpl(this,mTeam.getId());
        mPresenters.getTeamMembers();
        mPresenters.getTeamInfo();

        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.zoom_scrollView);
        if (scrollView != null) {
            scrollView.addOnPullZoomListeners(new PullToZoomBase.OnPullZoomListener() {
                @Override
                public void onPullZooming(int newScrollValue) {

                }
                @Override
                public void onPullZoomEnd() {
                    mPresenters.getTeamInfo();
                    mPresenters.getTeamMembers();
                    mRcvMembers.setLayoutFrozen(false);
                }
            });
        }

        //点击简介，修改内容
        View v = findViewById(R.id.team_summary);
        if (v != null) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSummaryClick();
                }
            });
        }

        //点击项目，打开项目列表
        v = findViewById(R.id.layout_team_project);
        if (v != null) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(TeamInfoActivity.this, ProjectsActivity.class);
                    i.putExtra(IntentTag.TAG_TEAM_BEAN , mTeam);
                    startActivity(i);
                }
            });
        }
        //点击公告
        v = findViewById(R.id.team_notice);
        if (v != null) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     onNoticeClick();
                }
            });
        }


        //点击团队成员，打开团队成员列表
        v = findViewById(R.id.layout_team_member);
        if (v != null) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(TeamInfoActivity.this, TeamMemberActivity.class);
                    i.putExtra(com.hadesky.cacw.tag.IntentTag.TAG_TEAM_BEAN, mTeam);
                    startActivity(i);
                }
            });
        }
    }

    private void loadTeamBeanFromIntent() {
        Intent i = getIntent();
        mTeam = (TeamBean) i.getSerializableExtra(IntentTag.TAG_TEAM_BEAN);
        if (mTeam == null) {
            finish();
        }
    }


    private void showInfo() {

        mTvTeamId.setText(String.valueOf(mTeam.getId()));
        mTvTeamName.setText(mTeam.getTeamName());
        mTvSummary.setText(mTeam.getSummary());
        mTvNotice.setText(mTeam.getNotice());
        mTvProjectCount.setText(String.format(getString(R.string.sum_of_projects),mTeam.getProjectCount()));
        mTvMemberCount.setText(String.format(getString(R.string.member_count),mTeam.getMemberCount()));

        if (mTeam.getTeamAvatarUrl() != null) {
            Uri uri = Uri.parse(mTeam.getTeamAvatarUrl());
            mSimpleDraweeView.setImageURI(uri);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri).setPostprocessor(new BlurProcessor()).build())
                    .build();
            mZoom.setController(controller);
        }

    }

    @Override
    public void showInfo(TeamBean bean) {
        if (bean != null) {
            mTeam = bean;
            showInfo();
        }
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //点击公告
    private void onNoticeClick() {
        if (mTeam.getAdminId()==MyApp.getCurrentUser().getId()) {
            View view = getLayoutInflater().inflate(R.layout.dialog_nick_name, null);
            final EditText editText = (EditText) view.findViewById(R.id.edit_text);
            editText.setText(mTvNotice.getText());
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.notice)
                    .setView(view)
                    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenters.modifyNotice(editText.getText().toString());
                        }
                    });
            builder.create().show();
        }
    }


    //点击简介
    private void onSummaryClick() {

        if (mTeam.getAdminId()==MyApp.getCurrentUser().getId()) {
            View view = getLayoutInflater().inflate(R.layout.dialog_nick_name, null);
            final EditText editText = (EditText) view.findViewById(R.id.edit_text);
            editText.setText(mTvSummary.getText());
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.changeSummary))
                    .setView(view)
                    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenters.modifySummary(editText.getText().toString());
                        }
                    });
            builder.create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_icon:
                changeTeamIcon();
                return true;
            case R.id.action_exit_team:
                showExitDialog();
                return true;
            case R.id.action_del_team:
                showDelTeamDialog();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDelTeamDialog() {
        new AlertDialog.Builder(this).setMessage("你确定要解散此团队吗？(与之相关的任务等会被清除，不可恢复)")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenters.delCurrentTeam();
                    }
                }).setNegativeButton(android.R.string.cancel, null).create().show();
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this).setMessage("你确定要退出该团队吗？")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenters.exitTeam();
                    }
                }).setNegativeButton(android.R.string.cancel, null).create().show();
    }

    private void changeTeamIcon() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setItems(new CharSequence[]{"拍照", "从图库中选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //选择拍照
                            Intent getCameraImage = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(getCameraImage, 0);
                        } else if (which == 1) {
                            //选择从图库选择
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("outputX", 600);
                            intent.putExtra("outputY", 600);
                            intent.putExtra("scale", true);
                            intent.putExtra("return-data", false);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.getTempUri(TeamInfoActivity.this,TeamIconFileName));
                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                            intent.putExtra("noFaceDetection", true); // 关闭人脸检测
                            startActivityForResult(intent, 0);
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        Uri filePath = data.getData();
                        System.out.println("path " + filePath);
                        File result =
                                ImageResizer.getCompressBitmap(filePath.getPath(), TeamIconFileName_Low, this);
                        mPresenters.saveTeamIcon(result);
                    }
                }
        }
    }


    @Override
    public void showMembers(List<UserBean> list) {
        mAdapter.setDatas(list);
        mRcvMembers.setLayoutFrozen(true);
    }


    @Override
    public void showProgress() {
        mAnimView.startAnim();
    }

    @Override
    public void hideProgress() {
        mAnimView.stopAnimAndHide();
    }

    @Override
    public void showMsg(String s) {
        showToast(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenters.onDestroy();
    }
}
