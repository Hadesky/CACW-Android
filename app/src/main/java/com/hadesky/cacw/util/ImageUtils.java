package com.hadesky.cacw.util;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.Palette;

/**
 * Created by MicroStudent on 2016/7/17.
 */

public class ImageUtils {
    public static Drawable tintDrawable(Drawable drawable, int color) {
        final Drawable wrapDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrapDrawable, color);
        return wrapDrawable;
    }

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colorStateList) {
        final Drawable wrapDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrapDrawable, colorStateList);
        return wrapDrawable;
    }

    /**
     * 依据一个bitmap获取主色调
     * @param bitmap 要获取主色调的bitmap
     * @return 主色调，或者默认白色
     */
    public static int getBitmapLightColor(Bitmap bitmap) {
        Palette palette = new Palette.Builder(bitmap).generate();
        return palette.getLightVibrantColor(Color.WHITE);
    }


}
