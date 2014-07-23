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
		  
	    //����������ӷ���   
	    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);   
	    // State state = connManager.getActiveNetworkInfo().getState();   
	    // ��ȡWIFI��������״̬  
	    State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();   
	    // �ж��Ƿ�����ʹ��WIFI����   
	    if (State.CONNECTED == state) {  
	    	success = true;   
	    }   
	    // ��ȡGPRS��������״̬   
	    state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();  
	    // �ж��Ƿ�����ʹ��GPRS����   
	    if (State.CONNECTED == state){   
	    	success = true;   
	    }   
	    AppController.getInstance().setConnInternet(success);
	    if (!success) {   
	    	Toast.makeText(context,"δ�����ӵ�����", Toast.LENGTH_LONG).show();   
	    } 
	}

}
