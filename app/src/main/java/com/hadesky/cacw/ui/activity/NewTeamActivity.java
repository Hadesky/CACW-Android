package com.hadesky.cacw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hadesky.cacw.R;
import com.hadesky.cacw.presenter.NewTeamPresenter;
import com.hadesky.cacw.presenter.NewTeamPresenterImpl;
import com.hadesky.cacw.ui.view.NewTeamView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.ui.widget.CircleImageView;
import com.hadesky.cacw.util.FileUtil;
import com.hadesky.cacw.util.ImageResizer;

import java.io.File;

public class NewTeamActivity extends BaseActivity implements NewTeamView {

    CircleImageView mCircleImageViews;
    Toolbar mToolbars;
    Button mBtnSubmit;
    EditText mEdtTeamName;
    NewTeamPresenter mPresenters;


    private static final String TEMP_FILE_NAME = "team_cache_bitmap.jpg";
    private File mAvatarFile;
    private AnimProgressDialog mProgressDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_team;
    }

    @Override
    public void initView() {
        mCircleImageViews = (CircleImageView) findViewById(R.id.cv_team_avatar);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mToolbars = (Toolbar) findViewById(R.id.toolbar);
        mEdtTeamName = (EditText) findViewById(R.id.edt_team_name);
        mProgressDialog = new AnimProgressDialog(this,true, null, "创建中...");
    }

    @Override
    public void setupView() {
        mCircleImageViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureChooserDialog();
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTeam();
            }
        });
        setSupportActionBar(mToolbars);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenters = new NewTeamPresenterImpl(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }


    private void createTeam() {
        String tname = mEdtTeamName.getText().toString();
        mPresenters.createTeam(tname, mAvatarFile);
    }

    /**
     * 弹出选择照片的Dialog
     */
    private void showPictureChooserDialog() {
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
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.getTempUri(NewTeamActivity.this, TEMP_FILE_NAME));
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
                        Bitmap selectedImage = BitmapFactory.decodeFile(filePath.getPath());
                        mCircleImageViews.setImageBitmap(selectedImage);
                        mAvatarFile = ImageResizer.getCompressBitmap(filePath.getPath(), TeamInfoActivity.TeamIconFileName_Low, this);
                    }
                }
        }
    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);
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
    public void Close() {
        finish();
        if(mPresenters!=null)
            mPresenters.cancel();
    }
}
