package com.wuxianhui.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.wsplog.app1.R;
import com.wuxianhui.init.WelcomeActivity;
import com.wuxianhui.tools.AppController;
import com.wuxianhui.tools.GoodsClass;
import com.wuxianhui.tools.GoodsInfo;

public class MainActivity extends FragmentActivity {
    private FragmentManager fm;
    private RadioGroup radioGroup;
    LayoutInflater inflater;
    int count = 0;
    String wspId;
    GoodsInfo goodsInfo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        inflater = LayoutInflater.from(this);
        fm = this.getSupportFragmentManager();
        wspId = AppController.getInstance().getWspId();
        goodsInfo = AppController.getInstance().getGoodsInfo();
        new MainTask().execute(wspId);
        radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        String menuString = getIntent().getStringExtra("menuString");
        if(menuString!=null&&!menuString.equals("")){
        	setRadioGroup(menuString);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = fm.beginTransaction();
                Fragment fragment = FragmentFactory.getInstanceByIndex(group.getCheckedRadioButtonId());
                transaction.replace(R.id.main_content, fragment);
                transaction.commit();
            }
        });
        radioGroup.check(1);
    }
    private void setRadioGroup(String menuString) {
    	if(menuString.contains("门户"))
            addRadioButton(1,"门户",R.drawable.lock);
    	if(menuString.contains("商务"))
            addRadioButton(2,"商务",R.drawable.lock);
    	if(menuString.contains("WSP搜索"))
            addRadioButton(3,"WSP搜索",R.drawable.lock);
    	if(menuString.contains("我的"))
            addRadioButton(4,"我的",R.drawable.lock);
	}
	public void addRadioButton(int id,String text,int drawableId){
    	RadioButton radioButton = (RadioButton)inflater.inflate(R.layout.radio_button, null);
        radioButton.setId(id);
        radioButton.setText(text);
        Drawable topDrawable = getResources().getDrawable(drawableId);
        radioButton.setCompoundDrawablesWithIntrinsicBounds(null, topDrawable, null, null);
        radioGroup.addView(radioButton);
        radioButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
    }
	/**
	 * public boolean onKeyDown(int keyCode, KeyEvent event){  
        if (keyCode == KeyEvent.KEYCODE_BACK ){
        	count++;
        	if(count==1){
        		Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        	}else if(count>=2){
        		finish();
        	}
        }
        return true;
    }
	 */
    
    public FragmentManager getFm(){
    	return fm;
    }
    class MainTask extends AsyncTask<String,Void,String>{
		protected String doInBackground(String... params) {
			JSONObject requestJSON = new JSONObject();
			try{
				requestJSON.put("wspid", params[0]);
				String address = getResources().getString(R.string.server_port)+"/getDishes.action";
				HttpPost httpPost = new HttpPost(address);
				httpPost.setEntity(new StringEntity(requestJSON.toString()));
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpPost);
				if(httpResponse.getStatusLine().getStatusCode()==200){
					String responseString = EntityUtils.toString(httpResponse.getEntity());
					JSONArray jsonArray = new JSONArray(responseString);
					for(int i = 0; i < jsonArray.length(); i++){
						JSONObject obj = jsonArray.getJSONObject(i);
						String goodsType = obj.getString("goodsType");
						String goodsName = obj.getString("goodsName");
						double price = obj.getDouble("price");
						String imageUrl = obj.getString("imageUrl");
						Long goodsId = obj.getLong("goodsId");
						int index =-1;
						GoodsClass goodsClass;
						if(goodsInfo.getGoodsTypes().contains(goodsType)){
							index = goodsInfo.getGoodsTypes().indexOf(goodsType);
							goodsClass = goodsInfo.getGoodsClasses().get(index);
						}else{
							goodsInfo.getGoodsTypes().add(goodsType);
							goodsClass = new GoodsClass();
							goodsInfo.getGoodsClasses().add(goodsClass);
						}
						goodsClass.setGoodsType(goodsType);
						goodsClass.getDishNames().add(goodsName);
						goodsClass.getPrices().add(price);
						goodsClass.getImageUrls().add(imageUrl);
						goodsClass.getGoodsIds().add(goodsId);
					}
					return goodsInfo.getGoodsTypes().toString();
				}else{
					return "连接失败";
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
			return "JSON异常";
		}
		protected void onPostExecute(String result) {
			Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
		}
    }
}