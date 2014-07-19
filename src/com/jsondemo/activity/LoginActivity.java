package com.jsondemo.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.jsondemo.tools.SPHelper;

public class LoginActivity extends Activity {

	EditText phonenumberET;
	EditText passwordET;
	Button loginButton;
	Button registButton;
	CheckBox isRememberCB;
	SPHelper helper;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setActionBarLayout(R.layout.actionbar_login_layout);
		helper = new SPHelper(LoginActivity.this,"information");
		phonenumberET = (EditText)findViewById(R.id.phone_number_login);
		passwordET = (EditText)findViewById(R.id.password_login);
		if(helper.getValue("telephone")!=null){
			phonenumberET.setText(helper.getValue("telephone"));
			passwordET.setText(helper.getValue("password"));
		}
		phonenumberET.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			public void afterTextChanged(Editable s) {
				if(!phonenumberET.getText().toString().matches("[1][358]\\d{9}")){
					phonenumberET.setError("请输入正确的11位手机好码");
				}
			}
			
		});
		passwordET.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			public void afterTextChanged(Editable s) {
				if(!(passwordET.getText().toString().trim().length()>=6)){
					passwordET.setError("请输入长度超过6位的密码");
				}
			}
			
		});
		loginButton = (Button)findViewById(R.id.login);
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String phonenumber = phonenumberET.getText().toString().trim();
				String password = passwordET.getText().toString().trim();
				if(!phonenumber.matches("[1][358]\\d{9}")){
					Toast.makeText(LoginActivity.this,"手机号码输入不合要求",Toast.LENGTH_SHORT).show();
					return;
				}
				if(!(password.length()>=6)){
					Toast.makeText(LoginActivity.this, "密码输入不合要求", Toast.LENGTH_SHORT).show();
				}
				new LoginTask().execute(phonenumber+","+password);
			}
		});
		registButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,RegistActivity.class);
				registButton.setTextColor(Color.parseColor("#AA3411"));
				startActivity(intent);
			}
		});
		isRememberCB = (CheckBox)findViewById(R.id.check_remember);
		isRememberCB.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked==false){
					helper.remove("id");
					helper.remove("telephone");
					helper.remove("password");
				}
			}
			
		});
	}
	public void setActionBarLayout(int layoutId){
		ActionBar actionBar = getActionBar();
		if(actionBar != null){
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View view =inflater.inflate(layoutId, null);
			registButton = (Button)view.findViewById(R.id.text_regist);
			ActionBar.LayoutParams params = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(view,params);
		}
	}
	class LoginTask extends AsyncTask<String,Void,String>{
		protected String doInBackground(String... params) {
			JSONObject requestJSON = new JSONObject();
			String[] requestStrings = params[0].split(",");
			try {
				requestJSON.put("tel",requestStrings[0]);
				requestJSON.put("passwd", requestStrings[1]);
				String address = getResources().getString(R.string.server_port)+"/PrivateUserLogin.action";
				HttpPost httpPost = new HttpPost(address);
				httpPost.setEntity(new StringEntity(requestJSON.toString()));
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpPost);
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
			if(result.equals("err10002")){
				Toast.makeText(LoginActivity.this, "输入的密码不正确", Toast.LENGTH_SHORT).show();
				return;
			}else if(result.equals("err10003")){
				Toast.makeText(LoginActivity.this, "输入的手机号还未注册", Toast.LENGTH_SHORT).show();
				return;
			}
			helper.putValue("id", result);
			helper.putValue("telephone", phonenumberET.getText().toString().trim());
			helper.putValue("password", passwordET.getText().toString().trim());
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
