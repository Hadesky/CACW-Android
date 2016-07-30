package com.hadesky.cacw.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.Palette;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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



    public static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成bitmap
    {
        if (drawable != null) {
            int width = drawable.getIntrinsicWidth();// 取drawable的长宽
            int height = drawable.getIntrinsicHeight();
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;// 取drawable的颜色格式
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
            Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);// 把drawable内容画到画布中
            return bitmap;
        }
        return null;
    }

    public static void getBitmapFromFresco(String url, Context context, final Callback callback) {
        if (callback != null && url != null) {
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(url))
                    .setProgressiveRenderingEnabled(true)
                    .build();
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>>
                    dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                     @Override
                                     public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                         callback.receiveBitmap(bitmap);
                                     }

                                     @Override
                                     public void onFailureImpl(DataSource dataSource) {
                                     }
                                 },
                    CallerThreadExecutor.getInstance());
        }
    }

    public interface Callback {
        void receiveBitmap(@Nullable Bitmap bitmap);
    }
}
