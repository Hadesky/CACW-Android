package com.hadesky.cacw.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 简单的调整Bitmap大小
 * Created by MicroStudent on 2015/8/1.
 */
public class ImageResizer {
    private static final String TAG = "ImageResizer";


    /**
     * 计算Bitmap缩小到请求的大小所需的insamplesize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        //先计算图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        //options已经获取到了真实图片大小
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //再次编码
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 将一个JPG图片在不减少分辨率的情况下压缩，以便上传服务器，默认压缩质量为70
     *
     * @return a bitmap
     */
    private static byte[] getCompressBitmapByte(Bitmap original) {
        if (original != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 70, out);
            return out.toByteArray();
        }
        return null;
    }

    /**
     * 将一个JPG图片在不减少分辨率的情况下压缩到100kb以下，以便上传服务器
     * @param original 原始bitmap
     * @param fileOut 文件输出流
     */
    public static void compressBitmapToFile(Bitmap original, FileOutputStream fileOut) {
        if (original != null && fileOut != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 100, out);
            int quantity = 100;
            while (out.size() > 1000000 && quantity >= 0) {
//                out.reset();reset不会清空stream里面的内容,只能重新new一个了。
                out = new ByteArrayOutputStream();
                quantity -= 10;
                original.compress(Bitmap.CompressFormat.JPEG, quantity, out);
            }
        }
    }

    @Nullable
    public static File getCompressBitmap(String oriPath, String fileName, Context context) {
        Bitmap original = BitmapFactory.decodeFile(oriPath);
//        Bitmap compressed = getCompressBitmap(original);
        File file = FileUtil.createTempFile(context, fileName);
        FileOutputStream out;
        if (file==null)
            return null;
        try {
            out = new FileOutputStream(file);
            out.write(getCompressBitmapByte(original));
//            compressBitmapToFile(original, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
