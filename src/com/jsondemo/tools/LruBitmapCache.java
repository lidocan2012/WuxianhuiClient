package com.jsondemo.tools;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.util.LruCache;

public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {
	public LruBitmapCache() {
		this(getDefaultLruCacheSize());
	}
	
	public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }
	
	public static int getDefaultLruCacheSize() {
	        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	        final int cacheSize = maxMemory / 8;
	        return cacheSize;
	    }
	
	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight() / 1024;
	}

	@Override
	public Bitmap getBitmap(String arg0) {
		return get(arg0);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}
}
