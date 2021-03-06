package com.wuxianhui.tools;

import cn.jpush.android.api.JPushInterface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

public class AppController extends Application{
    private static String username = "zhangsan";
	public static final String TAG = AppController.class.getSimpleName();
	private static AppController instance;
	private GoodsInfo goodsInfo = new GoodsInfo();
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	private boolean isConnInternet;
	private long userId=5;
	private long wspId=74;
	private String tableId=null;
	private OrderInformation orderInfo = new OrderInformation();
	public void onCreate() {
		super.onCreate();
		instance = this;
		JPushInterface.setDebugMode(true); 
		JPushInterface.init(this); 
		 Log.d(TAG, "[ExampleApplication] onCreate");
		System.out.println("wspע��ɹ�");
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

	public OrderInformation getOrderInfo() {
		return orderInfo;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getWspId() {
		return wspId;
	}

	public void setWspId(long wspId) {
		this.wspId = wspId;
	}

	public GoodsInfo getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(GoodsInfo goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getUserName()
	{
		return AppController.username;
		}
	public void setUserName(String name)
	{
		AppController.username = name;
	}
}
