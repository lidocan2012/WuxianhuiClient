package com.jsondemo.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver{
	public void onReceive(Context context, Intent intent) {
		boolean success = false;   
		  
	    //获得网络连接服务   
	    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);   
	    // State state = connManager.getActiveNetworkInfo().getState();   
	    // 获取WIFI网络连接状态  
	    State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();   
	    // 判断是否正在使用WIFI网络   
	    if (State.CONNECTED == state) {  
	    	success = true;   
	    }   
	    // 获取GPRS网络连接状态   
	    state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();  
	    // 判断是否正在使用GPRS网络   
	    if (State.CONNECTED == state){   
	    	success = true;   
	    }   
	    AppController.getInstance().setConnInternet(success);
	    if (!success) {   
	    	Toast.makeText(context,"未能连接到网络", Toast.LENGTH_LONG).show();   
	    } 
	}

}
