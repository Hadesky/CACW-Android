package com.hadesky.cacw.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hadesky.cacw.R;
import com.hadesky.cacw.adapter.BaseAdapter;
import com.hadesky.cacw.adapter.viewholder.BaseViewHolder;
import com.hadesky.cacw.bean.ProjectBean;
import com.hadesky.cacw.bean.TeamBean;
import com.hadesky.cacw.bean.TeamMember;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.presenter.TeamInfoPresenter;
import com.hadesky.cacw.presenter.TeamInfoPresenterImpl;
import com.hadesky.cacw.ui.view.TeamInfoView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.util.BlurProcessor;
import com.hadesky.cacw.util.FileUtil;
import com.hadesky.cacw.util.FullyGridLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TeamInfoActivity extends BaseActivity implements TeamInfoView {

    static public final String IntentTag = "team";
    private AnimProgressDialog mProgressDialog;

    private TextView mTvTeamName;
    private TextView mTvTeamId;
    private TextView mTvSummary;
    private RecyclerView mRcvMembers;
    private BaseAdapter<TeamMember> mAdapter;
    private TeamInfoPresenter mPresenters;
    private TeamBean mTeam;
    private SimpleDraweeView mSimpleDraweeView;
    private SimpleDraweeView mZoom;

    @Override
    public int getLayoutId() {
        return R.layout.activity_team_info;
    }

    @Override
    public void initView() {
        mTvTeamId = (TextView) findViewById(R.id.tv_teamid);
        mTvTeamName = (TextView) findViewById(R.id.tv_team_name);
        mTvSummary = (TextView) findViewById(R.id.tv_team_summary);
        mProgressDialog = new AnimProgressDialog(this, false, null, "请稍后...");
        mRcvMembers = (RecyclerView) findViewById(R.id.rcv_team_member);
        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.sdv_team_icon);
        mZoom = (SimpleDraweeView) findViewById(R.id.iv_zoom);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


    }

    @Override
    public void setupView() {

        Intent i = getIntent();
        mTeam = (TeamBean) i.getSerializableExtra(IntentTag);
        if (mTeam == null) {
            finish();
            return;
        }
        showInfo();
        mAdapter = new BaseAdapter<TeamMember>(new ArrayList<TeamMember>(), R.layout.list_item_member) {
            @Override
            public BaseViewHolder<TeamMember> createHolder(View v, Context context) {

                BaseViewHolder<TeamMember> holder = new BaseViewHolder<TeamMember>(v) {
                    @Override
                    public void setData(TeamMember o) {
                        setTextView(R.id.tv, o.getUser().getNickName());
                    }
                };

                return holder;
            }
        };


        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRcvMembers.setLayoutManager(manager);
        mRcvMembers.setVerticalFadingEdgeEnabled(false);
        mRcvMembers.setAdapter(mAdapter);

        mPresenters = new TeamInfoPresenterImpl(mTeam, this);
        mPresenters.getTeamMembers();

        View v = findViewById(R.id.team_summary);
        if (v != null) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSumaryClick();
                }
            });
        }
    }

    @Override
    public void showInfo() {
        mTvTeamId.setText(mTeam.getObjectId());
        mTvTeamName.setText(mTeam.getTeamName());
        mTvSummary.setText(mTeam.getSummary());
        if (mTeam.getTeamAvatar() != null)
        {
            Uri uri = Uri.parse(mTeam.getTeamAvatar().getUrl());
            mSimpleDraweeView.setImageURI(uri);

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri).setPostprocessor(new BlurProcessor()).build())
                    .build();
            mZoom.setController(controller);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void onSumaryClick() {
        if (mTeam.getAdminUserId().equals(MyApp.getCurrentUser().getObjectId())) {

            View view = getLayoutInflater().inflate(R.layout.dialog_nick_name, null);
            final EditText editText = (EditText) view.findViewById(R.id.edit_text);
            editText.setText(mTvSummary.getText());
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.changeSummary))
                    .setView(view)
                    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenters.changeeSummary(editText.getText().toString());
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

        if (item.getItemId() == R.id.changeIcon) {
            changeTeamIcon();
        }
        return super.onOptionsItemSelected(item);
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
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.getTempUri(TeamInfoActivity.this, "Team_icon_cache"));
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
                        File file = new File(filePath.getPath());
                        mPresenters.saveTeamIcon(file);
                    }
                }
        }
    }

    @Override
    public void showMembers(List<TeamMember> list) {
        mAdapter.setDatas(list);
    }

    @Override
    public void showProject(List<ProjectBean> list) {

    }

    @Override
    public void showProgress() {
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMsg(String s) {
        showToast(s);
    }
}
