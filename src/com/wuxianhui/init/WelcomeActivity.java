package com.wuxianhui.init;

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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.jsondemo.activity.R;
public class WelcomeActivity extends FragmentActivity {
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	String menuString = "";
	String wspid = 1+"";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		fragmentManager = this.getSupportFragmentManager();
		Button button = (Button)findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new WelcomeTask().execute(wspid);
				fragmentTransaction = fragmentManager.beginTransaction();
				AdFragment adFragment = new AdFragment();
				fragmentTransaction.replace(R.id.welcome_content, adFragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				fragmentTransaction.commit();
			}
		});
	}
	class WelcomeTask extends AsyncTask<String,Void,String>{
		protected String doInBackground(String... params) {
			JSONObject requestJSON = new JSONObject();
			try{
				requestJSON.put("wspid", params[0]);
				String address = getResources().getString(R.string.server_port)+"/showMenuInfo.action";
				HttpPost httpPost = new HttpPost(address);
				httpPost.setEntity(new StringEntity(requestJSON.toString()));
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpPost);
				if(httpResponse.getStatusLine().getStatusCode()==200){
					String responseString = EntityUtils.toString(httpResponse.getEntity());
					JSONObject responseJSON = new JSONObject(responseString);
					String resultString = responseJSON.getString("info");
					menuString = resultString;
					return menuString;
				}else{
					return "¡¨Ω” ß∞‹";
				}
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "JSON“Ï≥£";
		}
		protected void onPostExecute(String result) {
			
		}
	}
	public String getMenuString(){
		return menuString;
	}
}
