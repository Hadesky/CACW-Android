package com.hadesky.cacw.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.ui.activity.MainActivity;

/**
 *
 * Created by dzysg on 2016/3/23 0023.
 */
public class SettingFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName("setting");
        addPreferencesFromResource(R.xml.setting);
    }


    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
    {
        if (preference.getKey() != null) {
            switch (preference.getKey()) {
                case "exit":
                    showExitDialog();
                    return true;
                case "change_password":
                    showConfirmPasswordDialog();
                    return true;
            }
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void showConfirmPasswordDialog() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        final EditText old = (EditText) view.findViewById(R.id.et_old_pw);
        final EditText newPw = (EditText) view.findViewById(R.id.et_new_pw);
        final EditText newPwConfirm = (EditText) view.findViewById(R.id.et_new_pw_confirm);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.change_password))
                .setView(view)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sOld = old.getText().toString();
                        String sNewPw = newPw.getText().toString();
                        String sNewPwConfirm = newPwConfirm.getText().toString();
                        if (!sNewPw.equals(sNewPwConfirm)) {
                            showToast("两次新密码不一致");
                            return;
                        }
                        tryToChangePw(sOld, sNewPw);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void tryToChangePw(String old, String newPw) {
//        BmobUser.updateCurrentUserPassword(old, newPw, new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e == null) {
//                    showToast("密码修改成功!");
//                } else {
//                    showToast(e.getMessage());
//                }
//            }
//        });
    }

    private void showToast(String toast) {
        if (toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void showExitDialog() {
        new AlertDialog.Builder(getActivity()).setMessage("退出蚂蚁团队?")
                .setNegativeButton(android.R.string.cancel,null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                }).create().show();
    }
}
