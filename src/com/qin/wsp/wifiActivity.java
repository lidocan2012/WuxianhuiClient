package com.qin.wsp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

import com.qin.wsp.ExampleUtil;
//import com.example.wsplog.AuthoriseActivity.MessageReceiver;
import com.qin.wsp.WifiConnect.WifiCipherType;
import com.qin.wsp.WifiConnect;
import com.qin.wsp.GuanggaoView;
import com.wuxianhui.init.WelcomeActivity;
import com.wuxianhui.tools.AppController;
import com.example.wsplog.app1.R;
public class wifiActivity extends InstrumentedActivity { 
	//WifiManager wifiManager;
	 WifiManager wifiM;
	 WifiConnect wc;
	 WifiAsyncTask wa;
	 TextView wificonnecttext;
	 String wifiname;
	 String wifibssid;
	 Button wifibt;
	 boolean wififlag;
	 public static boolean isForeground = false;
	 private String WifiRID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi);
		wificonnecttext =(TextView)findViewById(R.id.wificonnectinfo);
		wifibt = (Button)findViewById(R.id.wifibt);
		Intent i=getIntent();
		 wifiname=i.getStringExtra("ssid");
		 wifibssid=i.getStringExtra("bssid");
		 System.out.println("�����WIFI����Ϊ:"+wifiname);
		// JPushInterface.init(this); 
		 WifiRID=JPushInterface.getRegistrationID(getApplicationContext()); 
		 System.out.println("wifi�����û���IDΪ��"+WifiRID);
		 registerMessageReceiver();//ע����Ϣ������
		wificonnecttext.setText("�ѷ���WIFI������:"+wifiname+"\n��ȴ���Ȩ");
		wifiM=(WifiManager)getSystemService(Context.WIFI_SERVICE);
		wc=new WifiConnect(wifiM);
		 wa= new WifiAsyncTask(wifiname,wifibssid,wc);  
		 String username = AppController.getInstance().getUserName();
		 wa.execute(wifibssid,username,WifiRID); 
		wifibt.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(wififlag==true)
				{
					Intent i =new Intent(wifiActivity.this,WelcomeActivity.class);
					startActivity(i);
					
				}
				else
				{
					finish();
				}
			}
		});
		/*Intent intent = getIntent();
		String ssid = intent.getStringExtra("ssid");   
		String passwd = intent.getStringExtra("passwd");   
		 wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiConnect wc=new WifiConnect(wifiManager);
			boolean flage=wc.Connect("TP-LINK_318", "201331804", WifiCipherType.WIFICIPHER_WPA);
			System.out.println("����״̬�� "+flage);*/
	}
	public class WifiAsyncTask extends AsyncTask<String, Void, String> {
		WifiConnect wc;
		String passwd;
		String ssid;
		String mStrResult;
		Boolean authorise=true;
		String bssid;
	private	boolean flag=false;
		public WifiAsyncTask(String ssid,String bssid,WifiConnect wc)
		  { this.wc=wc;
			this.ssid=ssid;
			this.bssid=bssid;
			//this.passwd=passwd;
		  }
		@Override
		protected String doInBackground(String... params) { 
			HttpClient hc = new DefaultHttpClient();
			// �����Ƿ�������IP����Ҫд��localhost�ˣ���ʹ�ڱ�������ҲҪд�ϱ�����IP��ַ��localhost�ᱻ����ģ���������
			//String address = "http://192.168.1.125:8080/ServerJsonDemo/JsonServlet";

			//String gender = "";
			try {
				String strs = params[0].toString();//���󵥸���������
				String mac = params[0].toString().trim(); 
				String username = params[1].toString().trim();//���󵥸�����
					String RID =params[2].toString().trim(); 
					System.out.println("wsp:mac,username,RID:"+mac+","+username+","+RID);
							// ��װJSON����
					System.out.println("strs.lenth==1");
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("mac", mac);
					jsonObj.put("username", username);
					jsonObj.put("WifiRID", RID);
					System.out.println("jsonObj:"+jsonObj);
					//��������IP
					String address = getResources().getString(R.string.server_port)+"/getAuthorise.action";
					// �������󲢰�����Entity
					HttpPost hp = new HttpPost(address);
					hp.setEntity(new StringEntity(jsonObj.toString()));
					HttpResponse response = hc.execute(hp);
					// ����200������ɹ�
					System.out.println("StatusCode: " + response.getStatusLine().getStatusCode());
					if (response.getStatusLine().getStatusCode() == 200) {
						// ��ȡ��Ӧ�е����ݣ���Ҳ��һ��JSON��ʽ������
						mStrResult = EntityUtils.toString(response.getEntity());//string��ʽ
						// �����ؽ������JSON����
						JSONObject result = new JSONObject(mStrResult);
						System.out.println("result::::::" + mStrResult);
						// ������ȡ��Ҫ��ֵ
						ssid = result.getString("wifiname");
						passwd = result.getString("passwd");
						authorise=result.getBoolean("authorise");
						//gender = result.getString("gender");
						System.out.println("ssid" + ssid);
						
						
					} else {
						System.out.println("����ʧ��0");
					}
				
			}
				 catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
					// boolean flage=this.wc.Connect(this.ssid, this.passwd, WifiCipherType.WIFICIPHER_WPA);
					//System.out.println(SSIDArray[position]);
					//System.out.println(passwdArray[position]);
					//System.out.println("�첽����״̬�� "+flage);//SSIDArray[position];
			return "SSID:"+ssid+"passwd:"+passwd+"authorise:"+authorise;
				

		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			if(authorise==false){ 
				if(passwd=="") 
			   {
				this.flag=wc.Connect(ssid,passwd,WifiCipherType.WIFICIPHER_NOPASS);
			
			   }else{ this.flag=wc.Connect(ssid, passwd,WifiCipherType.WIFICIPHER_WPA);
				System.out.println("flag: "+flag);
				}
				if(flag==true){
				 Toast toast=Toast.makeText(getApplicationContext(), "wifi ���ӳɹ�", Toast.LENGTH_SHORT);
				 toast.show();
				 wificonnecttext.setText("�ѻ��"+wifiname+"ʹ��wifi����Ȩ");
				 wifibt.setText("��ȴ�wifi���������Ժ󣬵������");
				 wififlag=true;
				 
				}else
				{ 
					 Toast toast=Toast.makeText(getApplicationContext(), "wifi ��������ʧ��", Toast.LENGTH_SHORT);
					 toast.show();
					 wifibt.setText("wifi����ʧ�ܣ�����ϵ����Ա");
					// wifibt.setTextScaleX(size)
					 wififlag=false;
					 
				}
			}else{
				 Toast toast=Toast.makeText(getApplicationContext(), "wifi ������", Toast.LENGTH_SHORT);
                 toast.show();
                 wificonnecttext.setText(wifiname+"��Ȩ�ȴ���");
                 wifibt.setText("��ȴ�wifi���������Ժ󣬵������");
                // wififlag=false;
			 } 
		}

		
	}
	MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.qin.wsp.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public void registerMessageReceiver() {//ע����Ϣ������
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}
	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub	
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
	              String messge = intent.getStringExtra(KEY_MESSAGE);
	              String extras = intent.getStringExtra(KEY_EXTRAS);
	              StringBuilder showMsg = new StringBuilder();
	            //  String[] msg = messge.split(",");
	             // XianChengID= msg[0].toString();
	             showMsg.append(messge);//KEY_MESSAGE + " : " +
	              if (!ExampleUtil.isEmpty(extras)) {
	            	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
	              }
	              setCostomMsg(showMsg.toString());
				}
			}
		}//MessageReceiver
	private void setCostomMsg(String msg){
		 if (msg.length()>0) {//null != msgText
			// msgText.setText(msg);
			// msgText.setVisibility(android.view.View.INVISIBLE);
			 //listdata.add(msg);//��list�����Ϣ
			// MyAdapter adapter = new MyAdapter(AuthoriseActivity.this);
			 System.out.println("listdata����������������Ϣ:"+msg);
			 Toast toast1=Toast.makeText(getApplicationContext(), "wifi"+"listdata����������������Ϣ:"+msg, Toast.LENGTH_SHORT);
			 toast1.show();
			 String passwd = msg;
			 if(msg!="")
			 {
				/* wififlag= wc.Connect(wifiname, passwd.trim(),WifiCipherType.WIFICIPHER_WPA);
				 if(wififlag==true)
				 {
					 Toast toast=Toast.makeText(getApplicationContext(), "wifi ���ӳɹ�", Toast.LENGTH_SHORT);
					 toast.show();
					 wificonnecttext.setText("�ѻ��"+wifiname+"ʹ��wifi����Ȩ");
					 wifibt.setText("����Ϊ:"+msg+":");
					 wififlag=true;
				 }*/
				 Task task = new Task();
				 task.execute(msg);
				 
			 }
			 }
			 else
			 {
				 wificonnecttext.setText(wifiname+"�ܾ���Ȩ");
                 wifibt.setText("��˷���");
                 wififlag=false;
			 }
			// myAdapter.notifyDataSetChanged();
			 
       }
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isForeground=true;
		super.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isForeground=true;
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}
		
public class Task extends AsyncTask<String, Void, Boolean> 
{

	@Override
	protected Boolean doInBackground(String... params) {//����������params[0]
		// TODO Auto-generated method stub
		 wififlag= wc.Connect(wifiname, params[0].trim(),WifiCipherType.WIFICIPHER_WPA);
		return wififlag;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		
			 if(wififlag==true)
			 {
				 Toast toast=Toast.makeText(getApplicationContext(), "wifi ���ӳɹ�", Toast.LENGTH_SHORT);
				 toast.show();
				 wificonnecttext.setText("�ѻ��"+wifiname+"ʹ��wifi����Ȩ");
				// wifibt.setText("����Ϊ:"+msg+":");
				 wififlag=true;
		}
		
		//super.onPostExecute(result);
	}
	
	
	
	}
	
	
}
