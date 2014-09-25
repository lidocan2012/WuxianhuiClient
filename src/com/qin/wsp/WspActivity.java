package com.qin.wsp;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;



//import com.jsondemo.activity.MainActivity.MyTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import android.content.Intent;


import cn.jpush.android.api.InstrumentedActivity;

import com.qin.wsp.WifiAsyncTask;
import com.example.wsplog.app1.R;

public class WspActivity extends InstrumentedActivity {
	
	int cishu=0;
  int choice = 0;
	WifiManager wifiManager;
	ListView listView;
	List<ScanResult> scanResults;

	ListAdapter adapter;
	static Timer timer;

	Handler handler = new MyHandler(this);

	LocationManager locationManager;
	TextView tvLocation;
	EditText scanPosition;
	RadioButton radioButton;
	final int MAX_SCAN = 5;
	static int curScan = 0;
	int count = 0;
    String wifinamebssid;
	SQLiteDatabase scanDatabase;
    String	mStrResult;

    String SSID = "";
   String passwd = "";
   String BSSID="";
   String DisplayName="";
   String WspUserName="";
   private MyTask mTask;
   String SSIDArray[];
   String passwdArray[];
   String BSSIDArray[];
   String DisplayNameArray[];
   String WspUserNameArray[];
   WifiManager wifiM;
   WifiConnect wc;
   WifiAsyncTask wa;
   netState ns;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_wsp);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title_wifiprovider);
		listView = (ListView) findViewById(R.id.list_wifi_info);
		//tvLocation = (TextView) findViewById(R.id.tv_location);
	
		wifiM = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
         
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			Toast.makeText(this, "请打开Wifi连接", Toast.LENGTH_SHORT).show();
			//wifiManager.setWifiEnabled(true);
			//return;
			if(android.os.Build.VERSION.SDK_INT > 10) {
		        // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面 
		         startActivity(new Intent( android.provider.Settings.ACTION_SETTINGS)); 
		         } else { 
		        startActivity(new Intent( android.provider.Settings.ACTION_WIRELESS_SETTINGS)); 
		         }
			return;
			
		}
		
		
		ns = new netState();
		int type = netState.getNetWorkConnectionType(this);
		switch(type)
		{
		case ConnectivityManager.TYPE_WIFI:
			System.out.println("wifi:");
			choice=1;
			break;
		case ConnectivityManager.TYPE_MOBILE:
			System.out.println("mobile:");
			choice=2;
			break;
		case -1:
			//打开GPRS
			Gprs gprs = new Gprs(this);
			gprs.gprsEnable(true);
			System.out.println("gprs:");
			choice=2;
			break;
			default:
				System.out.println("出错了");
			
		
		}
	
		wifiManager.startScan();
		 wifiManager.getScanResults();

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			updateLocation(location);
		} else {
			Toast.makeText(this, "Last known location is NULL",
					Toast.LENGTH_SHORT).show();
		}

		//String scanDbPath = getResources().getString(R.string.scan_db);
	

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, 0.1f, new LocationListener() {
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
					}

					public void onProviderEnabled(String provider) {

					}

					public void onProviderDisabled(String provider) {
					}

					public void onLocationChanged(Location location) {
						if (location != null) {
							Toast.makeText(WspActivity.this, "LocationUpdate",
									Toast.LENGTH_SHORT).show();
							updateLocation(location);
						} else {
							Toast.makeText(WspActivity.this,
									" ",
									Toast.LENGTH_SHORT).show();
						}

					}
				});
		
		startMonotor();
	
		
		
	}

	private class MyTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			HttpClient hc = new DefaultHttpClient();
			// 这里是服务器的IP，不要写成localhost了，即使在本机测试也要写上本机的IP地址，localhost会被当成模拟器自身的
			//String address = "http://192.168.1.125:8080/ServerJsonDemo/JsonServlet";

			//String gender = "";
			try {
				
				String[] strs = params[0].split(",");//请求单个或多个对象
			if(choice==1)
			{
					if(strs.length == 1){			//请求单个对象
				
					// 封装JSON对象
					System.out.println("strs.lenth==1");
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("mac", params[0]);
					//设置请求IP
					String address = "http://192.168.1.108:8080/WuxianhuiServer/getwifi.action";
					
					// 创建请求并绑定请求到Entity
					HttpPost hp = new HttpPost(address);
					hp.setEntity(new StringEntity(jsonObj.toString()));
					HttpResponse response = hc.execute(hp);
					// 返回200即请求成功
					System.out.println("StatusCode: " + response.getStatusLine().getStatusCode());
					if (response.getStatusLine().getStatusCode() == 200) {
						// 获取响应中的数据，这也是一个JSON格式的数据
						mStrResult = EntityUtils.toString(response.getEntity());//string格式
						// 将返回结果生成JSON对象
						JSONObject result = new JSONObject(mStrResult);
						// 从中提取需要的值
						SSID = result.getString("wifiname");
						BSSID = result.getString("mac");
						DisplayName=result.getString("displayname");
						WspUserName=result.getString("wspusername");
						//gender = result.getString("gender");
						System.out.println("result" + mStrResult);
					} else {
						System.out.println("连接失败0");
					}
				}else if(strs.length > 1){		
					//请求多个对象，封装成json数组
					JSONArray jsons = new JSONArray();
					for(int i = 0; i < strs.length; i++){
						JSONObject obj = new JSONObject();
						obj.put("mac", strs[i]);//strs[]中存储的为id
						jsons.put(obj);
					}
					String tmp=jsons.toString();
					System.out.println("jsons  :"+tmp);
					
					
					String address = "http://192.168.1.108:8080/WuxianhuiServer/getWifiList.action";
					HttpPost hp = new HttpPost(address);
					hp.setEntity(new StringEntity(jsons.toString()));//数组toString

					HttpResponse response = hc.execute(hp);
					 
					System.out.println("StatusCode2: " + response.getStatusLine().getStatusCode());
					if (response.getStatusLine().getStatusCode() == 200) {
					String	temp = response.getEntity().toString();
					System.out.println("temp为:"+temp);
						mStrResult = EntityUtils.toString(response.getEntity());
						//System.out.println("EntityUtils.toString(response.getEntity()):::"+EntityUtils.toString(response.getEntity()));
						System.out.println("mStrResult:::"+mStrResult);
						JSONArray jsonArray = new JSONArray(mStrResult);
						for(int i = 0; i < jsonArray.length(); i++){
							JSONObject obj = jsonArray.getJSONObject(i);
							SSID += obj.getString("wifiname") + ",";
							BSSID += obj.getString("mac") + ",";
							DisplayName +=obj.getString("displayname") + ",";
							WspUserName +=obj.getString("wspusername")+",";
						//	gender += obj.getString("gender") + ",";
						}
					} else {
					//	Toast.makeText(getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
						System.out.println("连接失败1");
					}
				}
			}else if(choice==2)
			{String url = "http://192.168.1.108:8080/WuxianhuiServer/getWifiList.action";
				if(strs.length==1){
				  		
	    		try {
	    			JSONObject jsonObj = new JSONObject();
					jsonObj.put("mac", params[0]);
					 HttpClient client = new DefaultHttpClient();  
					     //当请求的网络为wap的时候，就需要添加中国移动代理  
					      
					        HttpHost proxy = new HttpHost("10.0.0.172", 80);  
					        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,  
					                proxy);  
					      
					    HttpPost hp = new HttpPost(url);  
					   // hp.setHeader("Charset", "UTF-8");  
					  //  hp.setHeader("Content-Type", "application/x-www-form-urlencoded");  
					   
					      
					     
					   
					    hp.setEntity(new StringEntity(jsonObj.toString()));  
					    HttpResponse response = null;  
					    response = client.execute(hp);  
					    System.out.println("StatusCode: " + response.getStatusLine().getStatusCode());
						if (response.getStatusLine().getStatusCode() == 200) {
							// 获取响应中的数据，这也是一个JSON格式的数据
							mStrResult = EntityUtils.toString(response.getEntity());//string格式
							// 将返回结果生成JSON对象
							JSONObject result = new JSONObject(mStrResult);
							// 从中提取需要的值
							SSID = result.getString("wifiname");
							BSSID = result.getString("mac");
							DisplayName=result.getString("displayname");
							WspUserName=result.getString("wspusername");
							//gender = result.getString("gender");
							System.out.println("result" + mStrResult);  
						}
				} catch (Exception e) {
					e.printStackTrace();
				}
				}
				else if(strs.length>1)
				{
					JSONArray jsons = new JSONArray();
					for(int i = 0; i < strs.length; i++){
						JSONObject obj = new JSONObject();
						obj.put("mac", strs[i]);//strs[]中存储的为id
						jsons.put(obj);
					}
					String tmp=jsons.toString();
					System.out.println("jsons  :"+tmp);
					 HttpClient client = new DefaultHttpClient();  
				     //当请求的网络为wap的时候，就需要添加中国移动代理  
				      
				        HttpHost proxy = new HttpHost("10.0.0.172", 80);  
				        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,  
				                proxy);  
				      
				    HttpPost hp = new HttpPost(url);  
				    hp.setEntity(new StringEntity(jsons.toString()));
					HttpResponse response = hc.execute(hp);
					System.out.println("StatusCode2: " + response.getStatusLine().getStatusCode());
					if (response.getStatusLine().getStatusCode() == 200) {
					String	temp = response.getEntity().toString();
					System.out.println("temp为:"+temp);
						mStrResult = EntityUtils.toString(response.getEntity());
						//System.out.println("EntityUtils.toString(response.getEntity()):::"+EntityUtils.toString(response.getEntity()));
						System.out.println("mStrResult:::"+mStrResult);
						JSONArray jsonArray = new JSONArray(mStrResult);
						for(int i = 0; i < jsonArray.length(); i++){
							JSONObject obj = jsonArray.getJSONObject(i);
							SSID += obj.getString("wifiname") + ",";
							BSSID += obj.getString("mac") + ",";
							DisplayName +=obj.getString("displayname") + ",";
							WspUserName +=obj.getString("wspusername")+",";
						//	gender += obj.getString("gender") + ",";
						}
					} else {
					//	Toast.makeText(getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
						System.out.println("连接失败1");
					}
				}
			}//choice==2
			} //try
		
			
			
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "SSID: " + SSID+"BSSID :"+BSSID;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//mTvResult.setText(result);
			SSIDArray=SSID.split(",");
			BSSIDArray=BSSID.split(",");
			DisplayNameArray=DisplayName.split(",");
			WspUserNameArray=WspUserName.split(",");
			wifiInfo();
		}
	}
	private void updateLocation(Location location) {
		tvLocation.setText("X:" + String.format("%3.7f", location.getLongitude()) + " Y:"
				+ String.format("%3.7f", location.getLatitude())
				+ " A:" + location.getAccuracy() + "米");
	}

	static class MyHandler extends Handler {
		private WeakReference<WspActivity> _myWifiInfo;

		public MyHandler(WspActivity wifiInfo) {
			_myWifiInfo = new WeakReference<WspActivity>(wifiInfo);
		}

		@Override
		public void handleMessage(Message msg) {
			WspActivity activity = _myWifiInfo.get();
			if (activity != null) {
				activity.wifi();
			}
			super.handleMessage(msg);
		}
	}
private void wifi()
    {

  wifiManager.startScan();
   scanResults = wifiManager.getScanResults();

   String srinfo = scanResults.toString();//扫描的结果，为一个数组
   System.out.println(srinfo);
   
    Collections.sort(scanResults, new Comparator<ScanResult>() {
	public int compare(ScanResult lhs, ScanResult rhs) {
		return lhs.BSSID.compareTo(rhs.BSSID);
	      }
      });
    wifinamebssid=scanResults.get(0).BSSID+",";
    for(int i=1;i<scanResults.size();i++)
	{
	 wifinamebssid+=scanResults.get(i).BSSID;
	 if(i<scanResults.size()-1)
	 {
		 wifinamebssid+=",";
	 }
	}
    System.out.println("scanResults.size:"+scanResults.size());
    System.out.println("wifiname :"+wifinamebssid);
   mTask = new MyTask();
     mTask.execute(wifinamebssid);
	}
	private void wifiInfo() {
		
		//循环向BaseAdapter中加入数据
		final int[] wifipower = new int[SSIDArray.length];
		for(int i=0;i<BSSIDArray.length;i++)
		{
			for(int j=0;j<scanResults.size();j++)
			{
				if(BSSIDArray[i].equals(scanResults.get(j).BSSID))
				{
					wifipower[i]=scanResults.get(j).level+100;
					break;
				}
				
			}
		}
		
		adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.wifi_info, null);
				TextView tvwsp = (TextView) v.findViewById(R.id.tv_wsp_info);//获取控件
				TextView tvwifiinfo = (TextView) v.findViewById(R.id.wifi_info);
				ImageView imageview = (ImageView) v.findViewById(R.id.imagewifipower);
				//ProgressBar progressLevel = (ProgressBar) v
					//	.findViewById(R.id.level);//获取控件
				//ScanResult sr = scanResults.get(position);
				String s=SSIDArray[position];
				String sd=DisplayNameArray[position];
				String wsp=WspUserNameArray[position];
			//	String passwd=passwdArray[position];  
				//tvSSID.setText("SSID: " + sr.SSID + "  LEVEL: " + sr.level);//设置控件显示内容
			
				if(40<wifipower[position])
				{
					imageview.setImageResource(R.drawable.wifi4);
				}
				else if(30<wifipower[position])
				{
					imageview.setImageResource(R.drawable.wifi3);
				}else if(20<wifipower[position])
				{
					imageview.setImageResource(R.drawable.wifi2);
				}else 
				{
					imageview.setImageResource(R.drawable.wifi1);
				}
				tvwsp.setText("WSP提供者:"+wsp);
				tvwifiinfo.setText("WIFI : "+s);
			
                
				return v;
			}

			public long getItemId(int position) {
				return position;
			}

			public Object getItem(int position) {
				return null;
			}

			public int getCount() {
				return SSIDArray.length;
			}

		
		};//BaseAdapter
		listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				Intent intent= new Intent(WspActivity.this,wifiActivity.class);
				intent.putExtra("ssid", SSIDArray[position].toString().trim());
				intent.putExtra("bssid", BSSIDArray[position].toString().trim());
				startActivity(intent);
				
				 
				 }
        	
		}); 
	
	}//wifiInfo

	private void startMonotor() {
		if (timer == null)
			timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 0x111;
				handler.sendMessage(msg);
			}
		}, 0, 3000000);
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.wifi_info_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_open_wifi:
			if (!wifiManager.isWifiEnabled()) {
				wifiManager.setWifiEnabled(true);
			}
			break;
		case R.id.menu_scan_wifi:
			startMonotor();
			break;
		case R.id.menu_close_monitor:
			Log.i("MyWifiInfo", "Stop timer");
			timer.cancel();
			timer = null;
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}*/

	@Override
	protected void onDestroy() {
	
		super.onDestroy();
	}
	
}