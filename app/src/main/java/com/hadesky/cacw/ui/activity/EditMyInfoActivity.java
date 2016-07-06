package com.hadesky.cacw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;

import com.hadesky.cacw.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditMyInfoActivity extends BaseActivity implements View.OnClickListener {


    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_my_info;
    }

    @Override
    public void initView() {

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
                .setItems(new CharSequence[]{"拍照","从图库中选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //选择拍照
                            Intent getCameraImage = new Intent("android.media.action.IMAGE_CAPTURE");

                            startActivityForResult(getCameraImage, 0);
                        } else if (which == 1) {
                            //选择从图库选择
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            Intent chooser = Intent.createChooser(intent, "选择一张图片");
                            startActivityForResult(chooser, 0);
                        }
                    }
                });
        builder.create().show();
    }

    private void registerOnClickListener(View view) {
        if (view != null) {
            view.setOnClickListener(this);
        }
    }
}
