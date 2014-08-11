package com.wuxianhui.business;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jsondemo.activity.R;
import com.wuxianhui.tools.AppController;
import com.wuxianhui.tools.OrderInformation;
import com.wuxianhui.tools.OrderInformation.OrderMap;

public class CurrentOrderActivity extends Activity {

	String[] imageUrls;
	float[] prices;
	String[] dishNames;
	OrderInformation orderInfo;
	LayoutInflater inflater;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_current_order);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_regist_layout);
		Intent intent = this.getIntent();
		if(intent!=null){
			imageUrls = intent.getStringArrayExtra("imageUrls");
			prices = intent.getFloatArrayExtra("prices");
			dishNames = intent.getStringArrayExtra("dishNames");
		}
		orderInfo = AppController.getInstance().getOrderInfo();
		inflater =getLayoutInflater();
		ImageView backIV = (ImageView)findViewById(R.id.back);
		TextView titleTV = (TextView)findViewById(R.id.titleTV);
		GridView willCommitGV = (GridView)findViewById(R.id.will_commit);
		Button commitBT = (Button)findViewById(R.id.commit_Button);
		GridView commitedGV = (GridView)findViewById(R.id.commited);
		willCommitGV.setAdapter(new WillCommitGridAdapter());
		commitedGV.setAdapter(new CommitedGridAdapter());
		titleTV.setText("当前下单");
		backIV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		commitBT.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				orderInfo.commit();
			}
		});
	}
	class WillCommitGridAdapter extends BaseAdapter{
		ArrayList<OrderMap> willCommitInfo = orderInfo.getWillCommitOrders();
		public int getCount() {
			return orderInfo.getWillCommitOrders().size();
		}
		public Object getItem(int position) {
			return position;
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(int index, View convertView, ViewGroup parent) {
			View view = convertView;
			WillCommitViewHolder holder = null;
			int position = willCommitInfo.get(index).getPosition();
			int number = willCommitInfo.get(index).getNumber();
			String[] numbers = new String[100];
			for(int i=0;i<100;i++){
				numbers[i]=(i+1)+"份";
			}
			if(convertView==null){
				view = inflater.inflate(R.layout.will_commit_item, null);
				holder = new WillCommitViewHolder();
				holder.nimageView = (NetworkImageView)view.findViewById(R.id.iv_image);
				holder.nameTV = (TextView)view.findViewById(R.id.will_dish_name);
				holder.orderNumSp = (Spinner)view.findViewById(R.id.will_order_num);
				holder.priceTV = (TextView)view.findViewById(R.id.will_price);
				view.setTag(holder);
			}else{
				holder = (WillCommitViewHolder) view.getTag();
			}
			final TextView nameTV= holder.nameTV;
			nameTV.setText(dishNames[position]);
			final TextView priceTV = holder.priceTV;
			priceTV.setText("￥"+prices[position]);
			holder.nimageView.setImageUrl(imageUrls[position], imageLoader);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(CurrentOrderActivity.this,R.layout.spinner_item,R.id.spinner_content,numbers);
			adapter.setDropDownViewResource(R.layout.spinner_item_down);
			holder.orderNumSp.setAdapter(adapter);
			holder.orderNumSp.setSelection(number-1);
			holder.orderNumSp.setOnItemSelectedListener(new OnItemSelectedListener(){
				public void onItemSelected(AdapterView<?> parent, View view,int index, long id) {
					int position = positionOfDish(nameTV.getText().toString());
					orderInfo.setWillCommit(position, index+1);
					priceTV.setText("￥"+(index+1)*prices[position]);
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
				
			});
			return view;
		}
		public int positionOfDish(String dishName){
			for(int i=0;i<dishNames.length;i++){
				if(dishNames[i].equals(dishName))
					return i;
			}
			return -1;
		}
	}
	class WillCommitViewHolder{
		NetworkImageView nimageView;
		TextView nameTV;
		TextView priceTV;
		Spinner orderNumSp;
	}
	class CommitedGridAdapter extends BaseAdapter{
		public int getCount() {
			return orderInfo.getCommitedOrders().size();
		}
		public Object getItem(int position) {
			return null;
		}
		public long getItemId(int position) {
			return 0;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			return null;
		}
	}
	class CommitedViewHolder{
		NetworkImageView nimageView;
		TextView nameTV;
		TextView priceTV;
	}
}
