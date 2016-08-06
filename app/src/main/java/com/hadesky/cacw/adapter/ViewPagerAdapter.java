package com.hadesky.cacw.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> mListViews;
    private String[] mTitles;

    public ViewPagerAdapter(List<View> mListViews) {
        this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)   {
        container.removeView(mListViews.get(position));//删除页卡
    }

    public String[] getTitles() {
        return mTitles;
    }

    public void setTitles(String[] titles) {
        mTitles = titles;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //这个方法用来实例化页卡
        container.addView(mListViews.get(position));//添加页卡
        return mListViews.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null && mTitles.length > position) {
            return mTitles[position];
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return  mListViews.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;//官方提示这样写
    }
}
