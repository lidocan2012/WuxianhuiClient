package com.jsondemo.activity;

import org.json.JSONException;
import org.json.JSONObject;
//import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.ImageLoader.ImageContainer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class VolleyRequestActivity extends Activity{
	private EditText edInput;
	private Button mBtnSubmit;
	//private TextView mTvResult;
	ImageView image;
	ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.volley_activity_layout);
		edInput = (EditText) findViewById(R.id.ed_input2);
		image = (ImageView) findViewById(R.id.img_result);
		mBtnSubmit = (Button) findViewById(R.id.btn_submit2);
		pDialog = new ProgressDialog(this);
		
		mBtnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*String url = "http://192.168.1.125:8080/test_struts/getPerson.action";
				pDialog.setMessage("Loading...");
				pDialog.show();
				
				JSONObject jsonObjectReq = new JSONObject();
				try {
					jsonObjectReq.put("id", edInput.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.d("JSON Request", jsonObjectReq.toString());
				JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST, url, 
						jsonObjectReq, new Response.Listener<JSONObject>() {
							public void onResponse(JSONObject arg0) {
								Log.d("JSON Response", arg0.toString());
								//mTvResult.setText(arg0.toString());
								pDialog.dismiss();
							};
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError arg0) {
								VolleyLog.d("JSON ERROR", "Error: " + arg0.getMessage());
								pDialog.dismiss();
							}
						});
				AppController.getInstance().addToRequestQueue(jsonObjReq);*/
				
				String url = "http://192.168.1.125:8080/test_struts/getImage.action";
				ImageLoader imageLoader = AppController.getInstance().getImageLoader();
				imageLoader.get(url, new ImageListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(VolleyRequestActivity.this, "RequestImageError", Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onResponse(ImageContainer arg0, boolean arg1) {
						image.setImageBitmap(arg0.getBitmap());
					}
				});
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
