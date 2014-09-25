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
	 
//打开或关闭GPRS
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
	
	//检测GPRS是否打开
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
	
	//开启/关闭GPRS
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
