package com.hadesky.cacw.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.hadesky.cacw.R;

/**
 * Created by dzysg on 2015/10/16 0016.
 */
public class DatePickerFagment extends DialogFragment
{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date,null);

        return new AlertDialog
                .Builder(getActivity())
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent i  = new Intent();
                        i.putExtra("data",12);
                        onActivityResult(1,1,i);
                    }
                }).setView(v).create();
    }
}
