package com.qin.wsp;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class netState {
	/**
	 * 
	 * @return �Ƿ��л����������
	 */
	public final boolean hasNetWorkConnection(Context context){
		//��ȡ���ӻ������
		final ConnectivityManager connectivityManager= (ConnectivityManager) context.
				getSystemService(Context.CONNECTIVITY_SERVICE);
		//��ȡ����������Ϣ
		final NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
		
		return (networkInfo!= null && networkInfo.isAvailable());
		
	}
	/**
	 * @return ����boolean ,�Ƿ�Ϊwifi����
	 * 
	 */
	public final boolean hasWifiConnection(Context context)
	{	
		final ConnectivityManager connectivityManager= (ConnectivityManager) context.
				getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		//�Ƿ������粢���Ѿ�����
		return (networkInfo!=null&& networkInfo.isConnectedOrConnecting());
		
		
	}
	
	/**
	 * @return ����boolean,�ж������Ƿ����,�Ƿ�Ϊ�ƶ�����
	 * 
	 */
	
	public final boolean hasGPRSConnection(Context context){
		//��ȡ����ӹ�����
		final ConnectivityManager connectivityManager= (ConnectivityManager) context.
				getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return (networkInfo!=null && networkInfo.isAvailable());
		
	}
	/**
	 * @return  �ж������Ƿ���ã��������������ͣ�ConnectivityManager.TYPE_WIFI��ConnectivityManager.TYPE_MOBILE�������÷���-1
	 */
	public static final int getNetWorkConnectionType(Context context){
		final ConnectivityManager connectivityManager=(ConnectivityManager) context.
				getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo wifiNetworkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final NetworkInfo mobileNetworkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		final NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		
		if(info!=null &&info.getType()==ConnectivityManager.TYPE_WIFI)
		{
			return ConnectivityManager.TYPE_WIFI;
		}
		else if(info!=null &&info.getType()==ConnectivityManager.TYPE_MOBILE)
		{
			return ConnectivityManager.TYPE_MOBILE;
		}
		else {
			return -1;
		}
		
		
	}
	

	
}
