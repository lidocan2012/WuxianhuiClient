package com.wuxianhui.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsondemo.activity.R;
import com.wuxianhui.business.ItemListFragment.ListFragmentCallBack;
import com.wuxianhui.tools.AppController;
import com.wuxianhui.tools.OrderInformation;

public class PlaceOrderActivity extends FragmentActivity implements ListFragmentCallBack{
	FragmentManager fm;
	FragmentTransaction ft;
	TextView orderSumTV;
	TextView animBallTV;
	String[] imageUrls = new String[]{
			"http://www.maxcoo.com.cn/mxhm/msj/multi/pix/mxc201031514931865158.jpg",
			"http://www.7qsj.cn/uploads/allimg/100514/1430243D6-0.jpg",
			"http://pic17.nipic.com/20111020/1365591_133021352000_2.jpg",
			"http://pic17.nipic.com/20111020/1365591_132501134000_2.jpg",
			"http://a3.att.hudong.com/18/94/05300000874931127768941945288.jpg"
	};
	float[] prices = new float[]{88f,69f,89f,79f,98f};
	String[] dishNames = new String[]{"Ìã°ò","ÂéÆÅ¶¹¸¯","¸É¹øÏº","Ñ¼ÀÏ¿Ç","ºìÉÕÈâ"};
	OrderInformation orderInfo;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_place_order);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_placeorder_layout);
		fm = this.getSupportFragmentManager();
		orderInfo = AppController.getInstance().getOrderInfo();
		ImageView backIV = (ImageView)findViewById(R.id.back);
		orderSumTV = (TextView)findViewById(R.id.order_sum);
		orderSumTV.setText(orderInfo.getWillCommitNum()+"");
		if(orderInfo.getWillCommitNum()>0){
			orderSumTV.setVisibility(View.VISIBLE);
		}
		ImageButton orderSumBT = (ImageButton)findViewById(R.id.order_sum_bt);
		orderSumBT.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PlaceOrderActivity.this,CurrentOrderActivity.class);
				intent.putExtra("imageUrls", imageUrls);
				intent.putExtra("prices", prices);
				intent.putExtra("dishNames",dishNames);
				startActivity(intent);
			}
		});
		backIV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	protected void onResume(){
		super.onResume();
		if(orderSumTV!=null){
			orderSumTV.setText(orderInfo.getWillCommitNum()+"");
		}
		if(orderInfo.getWillCommitNum()==0){
			orderSumTV.setVisibility(View.INVISIBLE);
		}
	}
	public String[] getImageUrls() {
		return imageUrls;
	}
	public float[] getPrices() {
		return prices;
	}
	public String[] getDishNames() {
		return dishNames;
	}
	public FragmentManager getFm(){
		return fm;
	}
	public TextView getOrderSumTV(){
		return orderSumTV;
	}
	public void onItemSelected(int position) {
		orderInfo.addWillCommit(position);
		orderSumTV.setText(orderInfo.getWillCommitNum()+"");
		orderSumTV.setVisibility(View.VISIBLE);
		orderSumTV.startAnimation(AnimationUtils.loadAnimation(this, R.anim.jump_ainm));
	} 
}
