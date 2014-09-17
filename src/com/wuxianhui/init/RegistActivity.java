package com.wuxianhui.init;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jsondemo.activity.R;
import com.wuxianhui.tools.AppController;
import com.wuxianhui.tools.SPHelper;

public class RegistActivity extends Activity {
	EditText phonenumberET;
	EditText passwordET;
	EditText passwordConfirmET;
	EditText vertificationCodeET;
	CheckBox ruleCB;
	ImageView backIV;
	Button registButton;
	Button sendCodeButton;
	SPHelper helper;
	Handler handler;
	SmsContent content;
	boolean isPhoneCorrect=false;
	boolean isPasswordCorrect=false;
	boolean isPasswordConfirmCorrect=false;
	String mobile_code=null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_regist);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_regist_layout);
		//setActionBarLayout(R.layout.actionbar_regist_layout);
		ImageView backIV = (ImageView)findViewById(R.id.back);
		backIV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		phonenumberET = (EditText)findViewById(R.id.telephone_number_regist);
		phonenumberET.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			public void afterTextChanged(Editable s) {
				String phonenumber = phonenumberET.getText().toString().trim();
				if(!phonenumber.matches("[1][358]\\d{9}")){
					phonenumberET.setError("请输入正确的11位手机好码");
				}else{
					isPhoneCorrect=true;
				}
			}
			
		});
		passwordET = (EditText)findViewById(R.id.password_regist);
		passwordET.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			public void afterTextChanged(Editable s) {
				String password = passwordET.getText().toString().trim();
				if(!(password.length()>=6)){
					passwordET.setError("请输入长度超过6位的密码");
				}else{
					isPasswordCorrect=true;
				}
			}
			
		});
		passwordConfirmET = (EditText)findViewById(R.id.confirm_regist);
		passwordConfirmET.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			public void afterTextChanged(Editable s) {
				String passwordConfirm = passwordConfirmET.getText().toString().trim();
				String password = passwordET.getText().toString().trim();
				if(!(passwordConfirm.equals(password))){
					passwordConfirmET.setError("两次输入密码要一样");
				}else{
					isPasswordConfirmCorrect=true;
				}
			}
			
		});
		vertificationCodeET = (EditText)findViewById(R.id.telephone_vertification_code);
		sendCodeButton = (Button)findViewById(R.id.send_vertification_code);
		handler = new Handler(){
			@SuppressLint("HandlerLeak")
			public void handleMessage(Message msg){
				if(msg.what==0x1233){
					sendCodeButton.setBackgroundResource(R.drawable.bg_alibuybutton);
					sendCodeButton.setEnabled(true);
					sendCodeButton.setText("获取验证码");
				}
			}
		};
		sendCodeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//注册短信变化监听
				content = new SmsContent(new Handler());
		        RegistActivity.this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
				sendCodeButton.setEnabled(false);
				sendCodeButton.setText("稍等一会儿");
				sendCodeButton.setBackgroundResource(R.drawable.bg_alibuybutton_unable);
				new Thread(new Runnable(){
					public void run() {
						sendsms(phonenumberET.getText().toString().trim());
					}
					
				}).start();
				new Timer().schedule(new TimerTask(){
					public void run() {
						handler.sendEmptyMessage(0x1233);
					}
				}, 10000);
			}
		});
		registButton = (Button)findViewById(R.id.regist_button);
		registButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String phonenumber = phonenumberET.getText().toString().trim();
				String password = passwordET.getText().toString().trim();
				if(!AppController.getInstance().isConnInternet()){
					Toast.makeText(RegistActivity.this,"未能连接到网络", Toast.LENGTH_LONG).show();
					return;
				}
				if(!isPhoneCorrect){
					Toast.makeText(RegistActivity.this,"输入手机号码不合要求",Toast.LENGTH_SHORT).show();
					return;
				}
				if(!isPasswordCorrect){
					Toast.makeText(RegistActivity.this, "输入密码不合要求", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!isPasswordConfirmCorrect){
					Toast.makeText(RegistActivity.this, "输入再次密码不合要求", Toast.LENGTH_SHORT).show();
					return;
				}
				String gettedCode = vertificationCodeET.getText().toString();
				if(mobile_code==null||gettedCode==null||!mobile_code.equals(gettedCode.trim())){
					Toast.makeText(RegistActivity.this, "输入的验证码不正确", Toast.LENGTH_SHORT).show();
					return;
				}
				new RegistTask().execute(phonenumber+","+password);
			}
		});
		helper = new SPHelper(RegistActivity.this,"information");
		
	}
	public void sendsms(String phonenumber){
		String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient(); 
		PostMethod method = new PostMethod(Url); 
			
		//client.getParams().setContentCharset("GBK");		
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

		
		mobile_code = (int)((Math.random()*9+1)*100000)+"";
		
		//System.out.println(mobile);
		
	    String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。"); 

		NameValuePair[] data = {//提交短信
			    new NameValuePair("account", "cf_lidocane"), 
			    new NameValuePair("password", "529193429"), //密码可以使用明文密码或使用32位MD5加密
			    //new NameValuePair("password", StringUtil.MD5Encode("529193429")),
			    new NameValuePair("mobile", phonenumber), 
			    new NameValuePair("content", content),
		};
		
		method.setRequestBody(data);		
		
		
		try {
			client.executeMethod(method);	
			
			String SubmitResult =method.getResponseBodyAsString();
					
			//System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult); 
			Element root = doc.getRootElement();


			String code = root.elementText("code");	
			String msg = root.elementText("msg");	
			String smsid = root.elementText("smsid");	
			
			
			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);
						
			if(code == "2"){
				System.out.println("短信提交成功");
			}
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * public void setActionBarLayout(int layoutId){
		ActionBar actionBar = getActionBar();
		if(actionBar != null){
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View view =inflater.inflate(layoutId, null);
			backIV = (ImageView)view.findViewById(R.id.back);
			backIV.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
			ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(view,params);
		}
	}
	 */
	
	class RegistTask extends AsyncTask<String,Void,String>{
		protected String doInBackground(String... params) {
			JSONObject requestJSON = new JSONObject();
			String[] requestStrings = params[0].split(",");
			try {
				requestJSON.put("tel",requestStrings[0]);
				requestJSON.put("passwd", requestStrings[1]);
				String address = getResources().getString(R.string.server_port)+"/PrivateUserRegiste.action";
				HttpPost httpPost = new HttpPost(address);
				httpPost.setEntity(new StringEntity(requestJSON.toString()));
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpPost);
				System.out.println(httpResponse.getStatusLine().getStatusCode());
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String responseString = EntityUtils.toString(httpResponse.getEntity());
					JSONObject responseJSON = new JSONObject(responseString);
					String idString =responseJSON.getString("id");
					return idString;
				}else{
					return "连接失败";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "json异常";
		} 
		protected void onPostExecute(String result) {
			if(result.equals("err10001")){
				Toast.makeText(RegistActivity.this,"手机号已经注册过了", Toast.LENGTH_SHORT).show();
				return;
			}
			Toast.makeText(RegistActivity.this,result, Toast.LENGTH_SHORT).show();
			helper.putValue("id", result);
			helper.putValue("telephone", phonenumberET.getText().toString().trim());
			helper.putValue("password", passwordET.getText().toString().trim());
			Intent intent = new Intent(RegistActivity.this,LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
	class SmsContent extends ContentObserver {  
		  
	    private Cursor cursor = null;  
	  
	    public SmsContent(Handler handler) {  
	        super(handler);  
	    }  
	  
	    @SuppressWarnings("deprecation")
		@Override  
	    public void onChange(boolean selfChange) {  
	  
	        super.onChange(selfChange);  
	        //读取收件箱中指定号码的短信  
	        cursor = managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read", "body"},  
	                " address=? and read=?", new String[]{"106900801834", "0"}, "_id desc");//按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了  
	        
	        if (cursor != null && cursor.getCount() > 0) {  
	            ContentValues values = new ContentValues();  
	            values.put("read", "1");        //修改短信为已读模式  
	            cursor.moveToNext();  
	            int smsbodyColumn = cursor.getColumnIndex("body");  
	            String smsBody = cursor.getString(smsbodyColumn);  
	            Pattern pattern = Pattern.compile("[^0-9]");
	            Matcher matcher = pattern.matcher(smsBody);
	            vertificationCodeET.setText(matcher.replaceAll("")); 
	  
	        }  
	  
	        //在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃  
	        if(Build.VERSION.SDK_INT < 14) {  
	            cursor.close();  
	        }  
	    }  
	}
	public void onDestroy(){
		this.getContentResolver().unregisterContentObserver(content);
		super.onDestroy();
	}
}
