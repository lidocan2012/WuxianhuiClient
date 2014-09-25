package com.qin.wsp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;

//import com.example.jpushdemo.ExampleUtil;
//import com.example.jpushdemo.MainActivity;




import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceive extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		
		// TODO Auto-generated method stub
		  Bundle bundle = intent.getExtras();
			Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
	            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
	            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
	            
	            //send the Registration Id to your server...
	                        
	        } 
			else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
	        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
	        	processCustomMessage(context, bundle);//处理自定义消息
	        	}else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
	                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
	                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
	                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);}
	}
	
	
	
	
	// 打印所有的 intent extra 数据
		private static String printBundle(Bundle bundle) {
			StringBuilder sb = new StringBuilder();
			for (String key : bundle.keySet()) {
				if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
					sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
				}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
					sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
				} 
				else {
					sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
				}
			}
			return sb.toString();
		}
		
		private void processCustomMessage(Context context, Bundle bundle) {//处理自定义消息
			if (wifiActivity.isForeground) {
				String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
				String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
				Intent msgIntent = new Intent(wifiActivity.MESSAGE_RECEIVED_ACTION);
				msgIntent.putExtra(wifiActivity.KEY_MESSAGE, message);
				if (!ExampleUtil.isEmpty(extras)) {
					try {
						JSONObject extraJson = new JSONObject(extras);
						if (null != extraJson && extraJson.length() > 0) {
							msgIntent.putExtra(wifiActivity.KEY_EXTRAS, extras);
						}
					} catch (JSONException e) {

					}

				}
				System.out.println("收到自定义消息");
				context.sendBroadcast(msgIntent);// 向 AuthoriseActivity 发送广播!!!!
			}
		}
}
