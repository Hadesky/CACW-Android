package com.hadesky.cacw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TaskBean;

import java.util.List;

/**
 * Created by dzysg on 2015/9/14 0014.
 */
public class MyTaskListAdapter extends CommonAdapter<TaskBean> {

    public MyTaskListAdapter(Context context, List<TaskBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getViewHolder(position,convertView,parent, R.layout.list_item_teamtask,mContext);





        return holder.getConvertView();
    }
}
