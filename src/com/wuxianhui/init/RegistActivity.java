package com.wuxianhui.init;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		setActionBarLayout(R.layout.actionbar_regist_layout);
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
					phonenumberET.setError("��������ȷ��11λ�ֻ�����");
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
					passwordET.setError("�����볤�ȳ���6λ������");
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
					passwordConfirmET.setError("������������Ҫһ��");
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
					sendCodeButton.setText("��ȡ��֤��");
				}
			}
		};
		sendCodeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendCodeButton.setEnabled(false);
				sendCodeButton.setText("�Ե�һ���");
				sendCodeButton.setBackgroundResource(R.drawable.bg_alibuybutton_unable);

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
					Toast.makeText(RegistActivity.this,"δ�����ӵ�����", Toast.LENGTH_LONG).show();
					return;
				}
				if(!isPhoneCorrect){
					Toast.makeText(RegistActivity.this,"�����ֻ����벻��Ҫ��",Toast.LENGTH_SHORT).show();
					return;
				}
				if(!isPasswordCorrect){
					Toast.makeText(RegistActivity.this, "�������벻��Ҫ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!isPasswordConfirmCorrect){
					Toast.makeText(RegistActivity.this, "�����ٴ����벻��Ҫ��", Toast.LENGTH_SHORT).show();
					return;
				}
				new RegistTask().execute(phonenumber+","+password);
			}
		});
		helper = new SPHelper(RegistActivity.this,"information");
		content = new SmsContent(new Handler());
        //ע����ű仯����
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
	}
	public void setActionBarLayout(int layoutId){
		ActionBar actionBar = getActionBar();
		if(actionBar != null){
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View view =inflater.inflate(layoutId, null);
			backIV = (ImageView)view.findViewById(R.id.back);
			backIV.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(RegistActivity.this,LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			});
			ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(view,params);
		}
	}
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
					String idString = "id="+responseJSON.getString("id");
					return idString;
				}else{
					return "����ʧ��";
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
			return "json�쳣";
		} 
		protected void onPostExecute(String result) {
			if(result.equals("err10001")){
				Toast.makeText(RegistActivity.this,"�ֻ����Ѿ�ע�����", Toast.LENGTH_SHORT).show();
				return;
			}
			Toast.makeText(RegistActivity.this,"ע��ɹ�", Toast.LENGTH_SHORT).show();
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
	        //��ȡ�ռ�����ָ������Ķ���  
	        cursor = managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read", "body"},  
	                " address=? and read=?", new String[]{"106900801834", "0"}, "_id desc");//��id���������date����Ļ����޸��ֻ�ʱ��󣬶�ȡ�Ķ��žͲ�׼��  
	        
	        if (cursor != null && cursor.getCount() > 0) {  
	            ContentValues values = new ContentValues();  
	            values.put("read", "1");        //�޸Ķ���Ϊ�Ѷ�ģʽ  
	            cursor.moveToNext();  
	            int smsbodyColumn = cursor.getColumnIndex("body");  
	            String smsBody = cursor.getString(smsbodyColumn);  
	            Pattern pattern = Pattern.compile("[^0-9]");
	            Matcher matcher = pattern.matcher(smsBody);
	            vertificationCodeET.setText(matcher.replaceAll("")); 
	  
	        }  
	  
	        //����managedQuery��ʱ�򣬲�����������close()������ ������Android 4.0+��ϵͳ�ϣ� �ᷢ������  
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
