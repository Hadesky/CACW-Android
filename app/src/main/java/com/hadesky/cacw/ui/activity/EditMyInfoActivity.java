package com.hadesky.cacw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.UserBean;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.presenter.EditMyInfoPresenter;
import com.hadesky.cacw.presenter.EditMyInfoPresenterImpl;
import com.hadesky.cacw.ui.view.EditMyInfoView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.util.FileUtil;
import com.hadesky.cacw.util.ImageResizer;
import com.hadesky.cacw.util.StringUtils;

import java.io.File;

public class EditMyInfoActivity extends BaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, EditMyInfoView {
    private static final int AFTER_CROP = 0;
    private static final int IMAGE_CAPTURE = 1;

    private static final String TAG = "EditMyInfoActivity";

    private static final String TEMP_FILE_NAME_ORIGINAL = "cached_avatar";//存放临时存放头像的文件名，原始数据
    public static final String TEMP_FILE_NAME_LOW = "cache_avatar_low";//存放临时头像的文件名，低像素

    private SimpleDraweeView mAvatarImageView;
    private View mSexLayout;
    private PopupMenu mSexPopupMenu;
    private TextView mNickNameTextView, mSexTextView, mSummaryTextView,
            mUserNameTextView, mPhoneTextView, mShortPhone, mAddressTextView;
    private EditMyInfoPresenter mPresenter;
    private AnimProgressDialog mProgressDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_my_info;
    }

    @Override
    public void initView() {
        mAvatarImageView = (SimpleDraweeView) findViewById(R.id.iv_avatar);
        mSexLayout = findViewById(R.id.layout_sex);
        mSexPopupMenu = new PopupMenu(this, mSexLayout, Gravity.END);
        mSexTextView = (TextView) findViewById(R.id.tv_sex);
        mSummaryTextView = (TextView) findViewById(R.id.tv_summary);
        mNickNameTextView = (TextView) findViewById(R.id.tv_nick_name);
        mUserNameTextView = (TextView) findViewById(R.id.tv_username);
        mPhoneTextView = (TextView) findViewById(R.id.tv_phone);
        mShortPhone = (TextView) findViewById(R.id.tv_short_phone);
        mAddressTextView = (TextView) findViewById(R.id.tv_address);

        mPresenter = new EditMyInfoPresenterImpl(this);
    }

    @Override
    public void setupView() {
        registerOnClickListener(findViewById(R.id.layout_avatar));
        registerOnClickListener(findViewById(R.id.layout_nick_name));
        registerOnClickListener(findViewById(R.id.layout_summary));
        registerOnClickListener(findViewById(R.id.layout_phone));
        registerOnClickListener(findViewById(R.id.layout_address));

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
            case R.id.layout_summary:
                showSummaryDialog();
                break;
            case R.id.layout_phone:
                showEditPhoneDialog();
                break;
            case R.id.layout_address:
                showAddressDialog();
                break;
        }
    }

    private void showAddressDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_with_edit_text_30, null);
        final EditText editText = (EditText) view.findViewById(R.id.edit_text);
        editText.setText(mAddressTextView.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.address))
                .setView(view)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.updateAddress(editText.getText().toString());
                    }
                });
        builder.create().show();
    }

    private void showEditPhoneDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_phone_number, null);
        final EditText phone = (EditText) view.findViewById(R.id.et_phone);
        final EditText shortPhone = (EditText) view.findViewById(R.id.et_short_phone);
        //限制15个字符
        phone.setText(mPhoneTextView.getText());
        shortPhone.setText(StringUtils.removeBrackets(mShortPhone.getText().toString()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.phone_number))
                .setView(view)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.updatePhone(phone.getText().toString());
                        mPresenter.updateShortPhone(shortPhone.getText().toString());
                    }
                });
        builder.create().show();
    }

    private void showSummaryDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_with_edit_text_30, null);
        final EditText editText = (EditText) view.findViewById(R.id.edit_text);
        editText.setText(mSummaryTextView.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.personal_summary))
                .setView(view)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.updateSummary(editText.getText().toString());
                    }
                });
        builder.create().show();
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
                        Intent intent;
                        if (which == 0) {
                            //选择拍照
                            intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.getTempUri(EditMyInfoActivity.this,
//                                    getTempFileName(TEMP_FILE_NAME_ORIGINAL)));
                            startActivityForResult(intent, IMAGE_CAPTURE);
                        } else if (which == 1) {
                            //选择从图库选择
                            intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("outputX", 600);
                            intent.putExtra("outputY", 600);
                            intent.putExtra("scale", true);
                            intent.putExtra("return-data", false);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.getTempUri(EditMyInfoActivity.this, getTempFileName(TEMP_FILE_NAME_ORIGINAL)));
                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                            intent.putExtra("noFaceDetection", true); // 关闭人脸检测
                            startActivityForResult(intent, AFTER_CROP);
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * 获取临时文件名，前面加上当前用户的id
     *
     * @return 临时文件名
     */
    private String getTempFileName(String originalOrLow) {
        return MyApp.getCurrentUser().getObjectId() + originalOrLow;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AFTER_CROP:
                if (resultCode == RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        Uri filePath = data.getData();
                        File result =
                                ImageResizer.getCompressBitmap(filePath.getPath(),
                                        getTempFileName(TEMP_FILE_NAME_LOW), this);
                        if (result != null) {
                            mPresenter.updateAvatar(result.getPath());
                        } else {
                            Log.e(TAG, "cannot compress bitmap");
                        }
//                        mPresenter.updateAvatar(filePath.getPath());
                    } else {
                        Log.e(TAG, "cannot get the result file");
                    }
                }
                break;
            case IMAGE_CAPTURE:
                if (resultCode == RESULT_OK && data != null) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    //intent.setClassName("com.android.gallery3d", "com.android.gallery3d.filtershow.crop.CropActivity");
                    // indicate image type and Uri
                    intent.setDataAndType(data.getData(), "image/*");
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 600);
                    intent.putExtra("outputY", 600);
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", false);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.getTempUri(EditMyInfoActivity.this, getTempFileName(TEMP_FILE_NAME_ORIGINAL)));
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    intent.putExtra("noFaceDetection", true); // 关闭人脸检测
                    // start the activity - we handle returning in onActivityResult
                    startActivityForResult(intent, AFTER_CROP);
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
        if (mProgressDialog == null) {
            mProgressDialog = new AnimProgressDialog(this, "上传中...");
        }
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showMsg(String s) {
        showToast(s);
    }

    @Override
    public void setAvatar(String avatarUrl) {
        if (avatarUrl != null && mAvatarImageView != null) {
            mAvatarImageView.setImageURI(avatarUrl);
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

    @Override
    public void setPhoneNumber(String phoneNumber) {
        if (mPhoneTextView != null) {
            mPhoneTextView.setText(phoneNumber);
        }
    }

    @Override
    public void setShortPhoneNumber(String shortNumber) {
        if (mShortPhone != null) {
            if (shortNumber != null && !shortNumber.isEmpty()) {
                mShortPhone.setText(StringUtils.roundWithBrackets(shortNumber));
                mShortPhone.setVisibility(View.VISIBLE);
            } else {
                mShortPhone.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setAddress(String address) {
        if (mAddressTextView != null) {
            mAddressTextView.setText(address);
        }
    }
}
