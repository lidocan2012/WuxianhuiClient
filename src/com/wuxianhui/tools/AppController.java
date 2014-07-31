package com.wuxianhui.tools;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.text.TextUtils;

public class AppController extends Application{
	public static final String TAG = AppController.class.getSimpleName();
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	private boolean isConnInternet;
	
	private static AppController instance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	public static synchronized AppController getInstance(){
		return instance;
	}
	
	public RequestQueue getRequestQueue(){
		if(requestQueue == null){
			requestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return requestQueue;
	}
	
	public ImageLoader getImageLoader(){
		getRequestQueue();
		if(imageLoader == null){
			imageLoader = new ImageLoader(this.requestQueue, new LruBitmapCache());
		}
		return this.imageLoader;
	}
	
	public <T> void addToRequestQueue(Request<T> req, String tag){
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}
	
	public <T> void addToRequestQueue(Request<T> req){
		req.setTag(TAG);
		getRequestQueue().add(req);
	}
	
	public void cancelPendingRequests(Object tag){
		if(requestQueue != null){
			requestQueue.cancelAll(TAG);
		}
	}

	public boolean isConnInternet() {
		return isConnInternet;
	}

	public void setConnInternet(boolean isConnInternet) {
		this.isConnInternet = isConnInternet;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
