package com.hadesky.cacw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;



/**这是一个adapter的公共类，配合自定义的ViewHolder可以提高代码复用率
 * 用法：复写getView方法，通过viewHolder.getViewHolder获取当前item的viewholder对象
 * 获得viewholder对象后，可通过getView方法获取holder内的控件对象并设置对应的值
 * 最后通过getContentView获取复用的view并返回
 *
 * Created by dzysg on 2015/9/14 0014.
 */
public abstract class CommenAdapter<T> extends BaseAdapter
{
	protected List<T> mDatas;
	protected Context mContext;
	protected LayoutInflater mInflater;
	
	
	
	public CommenAdapter(Context context,List<T> list)
	{
		mContext = context;
		mDatas = list;
		mInflater = LayoutInflater.from(context);
	}



	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

	

	

}
