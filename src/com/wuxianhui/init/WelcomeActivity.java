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
import android.widget.Toast;

import com.example.wsplog.app1.R;
import com.wuxianhui.tools.AppController;
public class WelcomeActivity extends FragmentActivity {
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	long wspuserId ;
	String menuString = "";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		wspuserId = 74;
		fragmentManager = this.getSupportFragmentManager();
		new WelcomeTask().execute("wspuserId");
		
	}
	class WelcomeTask extends AsyncTask<String,Void,String>{
		protected String doInBackground(String... params) {
			JSONObject requestJSON = new JSONObject();
			try{
				requestJSON.put("wspid", wspuserId);
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
			Toast.makeText(WelcomeActivity.this, wspuserId+result, Toast.LENGTH_SHORT).show();
			fragmentTransaction = fragmentManager.beginTransaction();
			AdFragment adFragment = new AdFragment();
			fragmentTransaction.replace(R.id.welcome_content, adFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			fragmentTransaction.commit();
		}
	}
	public long getWspuserId(){
		return wspuserId;
	}
	public String getMenuString(){
		return menuString;
	}
}
