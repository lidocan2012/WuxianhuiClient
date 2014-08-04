package com.wuxianhui.business;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsondemo.activity.R;

public class PlaceOrderActivity extends FragmentActivity {
	FragmentManager fm;
	FragmentTransaction ft;
	TextView orderSumTV;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_place_order);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_placeorder_layout);
		fm = this.getSupportFragmentManager();
		ImageView backIV = (ImageView)findViewById(R.id.back);
		orderSumTV = (TextView)findViewById(R.id.order_sum);
		backIV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	public FragmentManager getFm(){
		return fm;
	}
	public TextView getOrderSumTV(){
		return orderSumTV;
	}
}
