package com.hadesky.cacw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.presenter.EditMyInfoPresenter;
import com.hadesky.cacw.presenter.EditMyInfoPresenterImpl;
import com.hadesky.cacw.ui.view.BaseView;
import com.hadesky.cacw.ui.view.EditMyInfoView;
import com.hadesky.cacw.util.FileUtil;

public class EditMyInfoActivity extends BaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, EditMyInfoView {

    private static final String TAG = "EditMyInfoActivity";

    private static final String TEMP_FILE_NAME = "cache_bitmap";//存放临时存放头像的文件名

    private ImageView mAvatarImageView;
    private View mSexLayout;
    private PopupMenu mSexPopupMenu;
    private TextView mNickNameTextView, mSexTextView, mSummaryTextView, mUserNameTextView;
    private EditMyInfoPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_my_info;
    }

    @Override
    public void initView() {
        mAvatarImageView = (ImageView) findViewById(R.id.iv_avatar);
        mSexLayout = findViewById(R.id.layout_sex);
        mSexPopupMenu = new PopupMenu(this, mSexLayout, Gravity.END);
        mSexTextView = (TextView) findViewById(R.id.tv_sex);
        mSummaryTextView = (TextView) findViewById(R.id.tv_summary);
        mNickNameTextView = (TextView) findViewById(R.id.tv_nick_name);
        mUserNameTextView = (TextView) findViewById(R.id.tv_username);

        mPresenter = new EditMyInfoPresenterImpl(this);
    }

    @Override
    public void setupView() {
        registerOnClickListener(findViewById(R.id.layout_avatar));
        registerOnClickListener(findViewById(R.id.layout_nick_name));

        getMenuInflater().inflate(R.menu.menu_sex, mSexPopupMenu.getMenu());
        mSexPopupMenu.setOnMenuItemClickListener(this);
        mSexLayout.setOnTouchListener(mSexPopupMenu.getDragToOpenListener());
        mSexLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSexPopupMenu.show();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mPresenter.loadInfo();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_avatar:
                showPictureChooserDialog();
                break;
            case R.id.layout_nick_name:
                showNickNameDialog();
                break;
        }
    }

    private void showNickNameDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_nick_name, null);
        final EditText editText = (EditText) view.findViewById(R.id.edit_text);
        editText.setText(mNickNameTextView.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.edit_nick_name))
                .setView(view)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.updateNickName(editText.getText().toString());
                    }
                });
        builder.create().show();
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
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.getTempUri(EditMyInfoActivity.this, TEMP_FILE_NAME));
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
                        mAvatarImageView.setImageBitmap(selectedImage);
                    }
                }
        }
    }

    private void registerOnClickListener(View view) {
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.male:
                mPresenter.updateSexual(UserBean.SEX_MALE);
                break;
            case R.id.female:
                mPresenter.updateSexual(UserBean.SEX_FEMALE);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String s) {
        showToast(s);
    }

    @Override
    public void setAvatar(Bitmap avatar) {
        if (avatar != null && mAvatarImageView != null) {
            mAvatarImageView.setImageBitmap(avatar);
        }
    }

    @Override
    public void setSex(Byte sex) {
        if (mSexTextView == null) {
            return;
        }
        if (sex.equals(UserBean.SEX_MALE)) {
            mSexTextView.setText("男");
        } else if (sex.equals(UserBean.SEX_FEMALE)) {
            mSexTextView.setText("女");
        } else {
            mSexTextView.setText("保密");
        }
    }

    @Override
    public void setSummary(String summary) {
        if (mSummaryTextView == null) {
            return;
        }
        mSummaryTextView.setText(summary);
    }

    @Override
    public void setNickName(String nickName) {
        if (mNickNameTextView != null) {
            mNickNameTextView.setText(nickName);
        }
    }

    @Override
    public void setUserName(String userName) {
        if (mUserNameTextView != null) {
            mUserNameTextView.setText(userName);
        }
    }

}
