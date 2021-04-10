package com.test.volleyrequestdemo.img;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Lyq
 * on 2021-04-09
 */
public class BitmapCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> mCache;

    public BitmapCache() {
//        int maxSize = 10 * 1024 * 1024;
        long maxSize = Runtime.getRuntime().maxMemory() / 8;
        mCache = new LruCache<String, Bitmap>((int) maxSize) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);

    }
}
