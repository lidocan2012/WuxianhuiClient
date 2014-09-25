package com.qin.wsp;

import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;

public class Gprs {
	private ConnectivityManager mCM;
public Gprs(Context context)
{
		mCM = (ConnectivityManager)context.
				getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	 
//�򿪻�ر�GPRS
	public boolean gprsEnable(boolean bEnable)
	{
		Object[] argObjects = null;
				
		boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled");
		if(isOpen == !bEnable)
		{
			setGprsEnable("setMobileDataEnabled", bEnable);
		}
		
		return isOpen;	
	}
	
	//���GPRS�Ƿ��
	public boolean gprsIsOpenMethod(String methodName)
	{
		Class cmClass 		= mCM.getClass();
		Class[] argClasses 	= null;
		Object[] argObject 	= null;
		
		Boolean isOpen = false;
		try
		{
			Method method = cmClass.getMethod(methodName, argClasses);

			isOpen = (Boolean) method.invoke(mCM, argObject);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return isOpen;
	}
	
	//����/�ر�GPRS
	public void setGprsEnable(String methodName, boolean isEnable)
	{
		Class cmClass 		= mCM.getClass();
		Class[] argClasses 	= new Class[1];
		argClasses[0] 		= boolean.class;
		
		try
		{
			Method method = cmClass.getMethod(methodName, argClasses);
			method.invoke(mCM, isEnable);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
