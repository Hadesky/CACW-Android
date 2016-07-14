package com.hadesky.cacw.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MicroStudent on 2016/7/14.
 */
public class ImageViewPagerAdapter extends PagerAdapter {
    private List<String> mImageUrls;
    private Context mContext;
    private List<SimpleDraweeView> mImageViews;

    public ImageViewPagerAdapter(String imageUrl, Context context) {
        mImageUrls = new ArrayList<>();
        mImageUrls.add(imageUrl);
        mContext = context;
        mImageViews = new ArrayList<>();
    }

    public ImageViewPagerAdapter(List<String> imagesUrl, Context context) {
        mImageUrls = imagesUrl;
        mContext = context;
        mImageViews = new ArrayList<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        while (position % 4 >= mImageViews.size()) {
            //需要new一个
            SimpleDraweeView view = new SimpleDraweeView(mContext);
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(mContext.getResources())
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
            view.setHierarchy(builder.build());
            mImageViews.add(view);
        }
        SimpleDraweeView simpleDraweeView = getImageView(position);
        if (simpleDraweeView != null) {
            simpleDraweeView.setImageURI(mImageUrls.get(position));
        }
        container.addView(simpleDraweeView);
        return mImageViews.get(position % 4);
    }

    private SimpleDraweeView getImageView(int position) {
        if (position >= getCount()) {
            return null;
        }
        return mImageViews.get(position % 4);
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(getImageView(position));
    }
}