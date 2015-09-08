package com.hadesky.cacw.widget;

/**
 * 对话框封装类
 *
 * @author 凡帅
 * @date 2015/08/22
 */

import android.content.Context;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;

public class CustomDialog {

    /**
     * 创建消息对话框
     *
     * @param context  上下文 必填
     * @param title    标题 必填
     * @param message  显示内容 必填
     * @param btnName  按钮名称 必填
     * @param listener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createMessageDialog(Context context, String title,
                                             String message, String positiveName, OnClickListener listener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 设置对话框标题
        builder.setTitle(title);
        // 设置对话框消息
        builder.setMessage(message);
        // 设置按钮
        builder.setPositiveButton("确定", listener);
        // 创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    /**
     * 创建警示（确认、取消）对话框
     *
     * @param context             上下文 必填
     * @param title               标题 必填
     * @param message             显示内容 必填
     * @param positiveBtnName     确定按钮名称 必填
     * @param negativeBtnName     取消按钮名称 必填
     * @param positiveBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createConfirmDialog(Context context, String title,
                                             String message, String positiveName, String negativeName,
                                             OnClickListener positiveBtnListener,
                                             OnClickListener negativeBtnListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 设置对话框标题
        builder.setTitle(title);
        // 设置对话框消息
        builder.setMessage(message);
        // 设置确定按钮
        builder.setPositiveButton(positiveName, positiveBtnListener);
        // 设置取消按钮
        builder.setNegativeButton(negativeName, negativeBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();
        return dialog;
    }

    /**
     * 创建单选对话框
     *
     * @param context             上下文 必填
     * @param title               标题 必填
     * @param itemsString         选择项 必填
     * @param positiveBtnName     确定按钮名称 必填
     * @param negativeBtnName     取消按钮名称 必填
     * @param positiveBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param itemClickListener   监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createSingleChoiceDialog(Context context,
                                                  String title, String[] itemsString, String positiveName,
                                                  String negativeName, OnClickListener positiveBtnListener,
                                                  OnClickListener negativeBtnListener,
                                                  OnClickListener itemClickListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 设置对话框标题
        builder.setTitle(title);
        // 设置单选选项, 参数0: 默认第一个单选按钮被选中
        builder.setSingleChoiceItems(itemsString, 0, itemClickListener);
        // 设置确定按钮
        builder.setPositiveButton(positiveName, positiveBtnListener);
        // 设置确定按钮
        builder.setNegativeButton(negativeName, negativeBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    /**
     * 创建复选对话框
     *
     * @param context             上下文 必填
     * @param title               标题 必填
     * @param itemsString         选择项 必填
     * @param positiveBtnName     确定按钮名称 必填
     * @param negativeBtnName     取消按钮名称 必填
     * @param positiveBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param itemClickListener   监听器，需实现android.content.DialogInterface.
     *                            OnMultiChoiceClickListener;接口 必填
     * @return
     */
    public static Dialog createMultiChoiceDialog(Context context, String title,
                                                 String[] itemsString, String positiveName, String negativeName,
                                                 OnClickListener positiveBtnListener,
                                                 OnClickListener negativeBtnListener,
                                                 OnMultiChoiceClickListener itemClickListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 设置对话框标题
        builder.setTitle(title);
        // 设置选项
        builder.setMultiChoiceItems(itemsString, null, itemClickListener);
        // 设置确定按钮
        builder.setPositiveButton(positiveName, positiveBtnListener);
        // 设置确定按钮
        builder.setNegativeButton(negativeName, negativeBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    /**
     * 创建列表对话框
     *
     * @param context             上下文 必填
     * @param title               标题 必填
     * @param itemsString         列表项 必填
     * @param negativeBtnName     取消按钮名称 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createListDialog(Context context, String title,
                                          String[] itemsString, String negativeBtnName,
                                          OnClickListener negativeBtnListener,
                                          OnClickListener itemClickListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 设置对话框标题
        builder.setTitle(title);
        // 设置列表选项
        builder.setItems(itemsString, itemClickListener);
        // 设置确定按钮
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    /**
     * 创建自定义（含确认、取消）对话框
     *
     * @param context             上下文 必填
     * @param title               标题 必填
     * @param positiveBtnName     确定按钮名称 必填
     * @param negativeBtnName     取消按钮名称 必填
     * @param positiveBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param view                对话框中自定义视图 必填
     * @return
     */
    public static Dialog createRandomDialog(Context context, String title,
                                            String positiveName, String negativeName,
                                            OnClickListener positiveBtnListener,
                                            OnClickListener negativeBtnListener, View view) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 设置对话框标题
        builder.setTitle(title);
        builder.setView(view);
        // 设置确定按钮
        builder.setPositiveButton(positiveName, positiveBtnListener);
        // 设置确定按钮
        builder.setNegativeButton(negativeName, negativeBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();
        return dialog;
    }

    /**
     * 日期选择对话框
     *
     * @param context
     * @param title
     * @param callBack
     * @param positiveBtnListener
     * @return
     */
    public static Dialog createDatePickerDialog(Context context, String title,
                                                OnDateSetListener callBack, OnClickListener positiveBtnListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 设置对话框标题
        builder.setTitle(title);
        DatePicker view = new DatePicker(context);
        // view.init(2015, 1, 1, new DatePicker.OnDateChangedListener() {
        //
        // @Override
        // public void onDateChanged(DatePicker view, int year,
        // int monthOfYear, int dayOfMonth) {
        //
        // }
        // });
        builder.setView(view);
        // 设置确定按钮
        builder.setPositiveButton("确定", positiveBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();
        return dialog;
    }

}
