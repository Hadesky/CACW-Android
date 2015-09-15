package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hadesky.cacw.R;
import com.hadesky.cacw.bean.TaskBean;

import java.util.List;

/**
 * Created by ziyue on 2015/7/24 0024.
 */
public class MyTaskRecylerAdapter extends RecyclerView.Adapter<MyTaskRecylerAdapter.MyTaskViewHolder> {

    private Context mContext;
    private List<TaskBean> mDatas;
    private LayoutInflater mInflater;


    public MyTaskRecylerAdapter(Context context, List<TaskBean> list) {

        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDatas = list;


    }

    @Override
    public MyTaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.list_item_teamtask, viewGroup, false);


        MyTaskViewHolder myViewHolder = new MyTaskViewHolder(view);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyTaskViewHolder viewHolder, int pos) {

       //设置属性


    }

    @Override
    public void onViewRecycled(MyTaskViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }



    public static class MyTaskViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView content;


        public MyTaskViewHolder(View itemView) {
            super(itemView);

        }
    }
}


