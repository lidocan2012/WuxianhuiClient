package com.wuxianhui.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsondemo.activity.R;
import com.wuxianhui.business.ItemListFragment.ListFragmentCallBack;
import com.wuxianhui.business.ItemListFragment.ViewHolder;

public class PlaceOrderActivity extends FragmentActivity implements ListFragmentCallBack{
	FragmentManager fm;
	FragmentTransaction ft;
	TextView orderSumTV;
	TextView animBallTV;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_place_order);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_placeorder_layout);
		fm = this.getSupportFragmentManager();
		ImageView backIV = (ImageView)findViewById(R.id.back);
		orderSumTV = (TextView)findViewById(R.id.order_sum);
		ImageButton orderSumBT = (ImageButton)findViewById(R.id.order_sum_bt);
		animBallTV = (TextView)findViewById(R.id.anim_ball);
		orderSumBT.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PlaceOrderActivity.this,CurrentOrderActivity.class);
				startActivity(intent);
			}
		});
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
	public void onItemSelected(View view, int position,long id) {
		TextView priceTV = ((ViewHolder)view.getTag()).price;
		int paddingTop = priceTV.getPaddingTop();
		int paddingRight = priceTV.getPaddingRight()-animBallTV.getWidth();
		int paddingbottom = priceTV.getPaddingBottom();
		int paddingLeft = priceTV.getPaddingLeft()+priceTV.getWidth();
		animBallTV.setPadding(paddingLeft, paddingTop, paddingRight, paddingbottom);
		animBallTV.setVisibility(View.VISIBLE);
	}
	public static void setLayout(View view,int x,int y){  
        MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());  
        margin.setMargins(x,y, x+margin.width, y+margin.height);  
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);  
        view.setLayoutParams(layoutParams);  
    } 
}
