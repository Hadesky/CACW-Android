package com.hadesky.cacw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.util.ImageResizer;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditMyInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EditMyInfoActivity";

    private static final String TEMP_FILE_NAME = "cache_bitmap";//存放临时存放头像的文件名

    private ImageView mAvatarImageView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_my_info;
    }

    @Override
    public void initView() {
        mAvatarImageView = (ImageView) findViewById(R.id.iv_avatar);
    }

    @Override
    public void setupView() {
        registerOnClickListener(findViewById(R.id.layout_avatar));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_avatar:
                showPictureChooserDialog();
        }
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
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
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
                    if (data != null) {
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

    private Uri getTempUri() {
        File file = getTempFile();
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    private File getTempFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(getExternalCacheDir(),TEMP_FILE_NAME);
            try {
                if (file.createNewFile()) {
                    return file;
                } else {
                    file.delete();
                    file.createNewFile();
                    return file;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
