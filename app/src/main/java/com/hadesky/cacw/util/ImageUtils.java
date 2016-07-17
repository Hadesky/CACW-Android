package com.hadesky.cacw.util;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by MicroStudent on 2016/7/17.
 */

public class ImageUtils {
    public static Drawable tintDrawable(Drawable drawable, int color) {
        final Drawable wrapDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrapDrawable, color);
        return wrapDrawable;
    }

    public Drawable tintDrawable(Drawable drawable, ColorStateList colorStateList) {
        final Drawable wrapDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrapDrawable, colorStateList);
        return wrapDrawable;
    }
}
