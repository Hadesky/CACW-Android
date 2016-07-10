package com.hadesky.cacw.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 *  文件操作
 * Created by dzysg on 2016/7/10 0010.
 */
public class FileUtil {

    public static  File createTempFile(Context context,String filename) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(context.getExternalCacheDir(),filename);
            try {
                if (file.createNewFile()) {
                    return file;
                } else {
                    file.delete();
                    file.createNewFile();
                    return file;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static  Uri getTempUri(Context context,String filename) {
        File file = createTempFile(context,filename);
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }
}
