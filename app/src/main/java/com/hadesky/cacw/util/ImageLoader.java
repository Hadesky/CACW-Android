package com.hadesky.cacw.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.hadesky.cacw.R;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.hadesky.cacw.util.Md5Util.string2Md5;

/**
 * 实现图片的同步、异步、以及LruCache、DiskLruCache
 * Created by MicroStudent on 2016/3/24.
 */
public class ImageLoader {
    public static final String TAG = "ImageLoader";


    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;//磁盘缓存50MB
    private static final int DISK_CACHE_INDEX = 0;//由于在diskLruCache的open方法处设置了1，因此默认1个cache节点就一份数据，为0
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final int TAG_KEY_URI = R.id.imageloader_uri;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;//核心线程池数
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;//线程池最大数量
    private static final long KEEP_ALIVE = 10L; //保持存活的最长时间
    private static final int MESSAGE_POST_RESULT = 1;

    //线程池的构建
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), sThreadFactory);

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            imageView.setImageBitmap(result.bitmap);
            String uri = (String) imageView.getTag(TAG_KEY_URI);
            if (uri.equals(result.uri)) {
                imageView.setImageBitmap(result.bitmap);
                Log.w(TAG, "iv has been set");
            } else {
                Log.w(TAG, "setting bitmap,but the url has changed,ignored!");
            }
        }
    };

    private Context mContext;
    private boolean mIsDiskLruCacheCreated = false;


    //内存缓存和磁盘缓存的实现
    private LruCache<String,Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;

    private ImageLoader(Context context) {
        this.mContext = context.getApplicationContext();
        initCache();
    }

    /**
     * 构造一个新实例返回
     * @param context context
     * @return a new instance of ImageLoader
     */
    public static ImageLoader build(Context context) {
        return new ImageLoader(context);
    }

    /**
     * 初始化diskLruCache和LruCache
     */
    private void initCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;//内存容量的大小
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (diskCacheDir.getUsableSpace() > DISK_CACHE_SIZE) {
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从内存、disk、网络获取一个bitmap，然后将其绑定到一个imageView
     * 必须在主线程使用
     * @param uri uri
     * @param imageView iv
     */
    public void bindBitmap(final String uri, final ImageView imageView) {
        bindBitmap(uri, imageView, 0, 0);
    }

    /**
     * 异步加载
     */
    public void bindBitmap(final String uri, final ImageView imageView, final int reqWidth, final int reqHeight) {
        imageView.setTag(TAG_KEY_URI, uri);
        final String key = string2Md5(uri);
        Bitmap bitmap = loadBitmapFromMemCache(key);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri, reqWidth, reqHeight);
                if (bitmap != null) {
                    LoaderResult result = new LoaderResult(imageView, uri, bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 从内存，disk、和网络同步获取一个bitmap，可能为null
     * @param uri http url
     * @param reqWidth 期望的宽
     * @param reqHeight 期望的高
     * @return nullable bitmap
     */
    public Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        final String key = string2Md5(uri);
        Bitmap bitmap = loadBitmapFromMemCache(key);
        if (bitmap != null) {
            return bitmap;
        }
        try {
            bitmap = loadBitmapFromDiskMemCache(uri, reqWidth, reqHeight);
            if (bitmap != null) {
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);

            if (bitmap == null && !mIsDiskLruCacheCreated) {
                bitmap = downloadBitmapFromUrl(uri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap downloadBitmapFromUrl(String urlString) throws IOException {
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(connection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            Log.e(TAG, "Error in downloadBitmap" + e);
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                in.close();
            }
        }
        return bitmap;
    }

    /**
     * 放置一bitmap进内存缓存
     * @param key key
     * @param bitmap bitmap
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (loadBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap loadBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


    private Bitmap loadBitmapFromHttp(String url,int reqWidth,int reqHeight)throws IOException{
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("cannot visit network form UI thread!");
        }
        if (mDiskLruCache == null) {
            return null;
        }
        String key = string2Md5(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(url, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskMemCache(url, reqWidth, reqHeight);
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) throws IOException {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
        return false;
    }



    private Bitmap loadBitmapFromDiskMemCache(String url,int reqWidth,int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("load bitmap from UI thread!");
        }
        if (mIsDiskLruCacheCreated && mDiskLruCache != null) {
            Bitmap result = null;
            String key = string2Md5(url);
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                FileDescriptor fileDescriptor = fileInputStream.getFD();
                result = ImageResizer.decodeSampledBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);
                if (result != null) {
                    addBitmapToMemoryCache(key, result);
                }
            }
            return result;
        }
        return null;
    }

    /**
     * 确认是否存在外置存储卡，若有，返回外置存储卡目录，否则返回本地存储目录
     * @param context context
     * @param uniqueName 唯一可识别的名字
     * @return 确认是否存在外置存储卡，若有，返回外置存储卡目录，否则返回本地存储目录
     */
    private File getDiskCacheDir(Context context, String uniqueName) {
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String result;
        if (externalStorageAvailable && context.getExternalCacheDir() != null) {
            result = context.getExternalCacheDir().getPath();
        }else {
            result = context.getCacheDir().getPath();
        }
        return new File(result + File.separator + uniqueName);
    }


    private static class LoaderResult{
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
}
