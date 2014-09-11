package com.wuxianhui.business;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jsondemo.activity.R;
import com.wuxianhui.tools.AppController;
import com.wuxianhui.tools.OrderInformation;
import com.wuxianhui.tools.OrderInformation.OrderGoods;

public class CurrentOrderActivity extends Activity {
	OrderInformation orderInfo;
	LayoutInflater inflater;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	String wspId = AppController.getInstance().getWspId();
	WillCommitGridAdapter willAdapter;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_current_order);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_regist_layout);
		orderInfo = AppController.getInstance().getOrderInfo();
		inflater =getLayoutInflater();
		ImageView backIV = (ImageView)findViewById(R.id.back);
		TextView titleTV = (TextView)findViewById(R.id.titleTV);
		final MyGridView willCommitGV = (MyGridView)findViewById(R.id.will_commit);
		Button commitBT = (Button)findViewById(R.id.commit_Button);
		final TextView totalTV = (TextView)findViewById(R.id.total);
		Button checkOutBT = (Button)findViewById(R.id.checkout);
		if(orderInfo.getWillCommitNum()==0){
			commitBT.setVisibility(View.GONE);
		}
		final MyGridView commitedGV = (MyGridView)findViewById(R.id.commited);
		willAdapter =new WillCommitGridAdapter();
		willCommitGV.setAdapter(willAdapter);
		final CommitedGridAdapter comAdapter = new CommitedGridAdapter();
		commitedGV.setAdapter(comAdapter);
//		comAdapter.notifyDataSetChanged();
		titleTV.setText("当前下单");
		totalTV.setText("￥"+orderInfo.getCommitedSum());
		backIV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		final LayoutInflater inflater = CurrentOrderActivity.this.getLayoutInflater();
		commitBT.setOnClickListener(new View.OnClickListener() {
			String tableId = AppController.getInstance().getTableId();
			public void onClick(View v) {
				if(tableId==null){
					View customDialog = inflater.inflate(R.layout.dialog_tableid, null);
					final EditText tableIdTV = (EditText)customDialog.findViewById(R.id.tableid_tv);
					AlertDialog dialog = new AlertDialog.Builder(CurrentOrderActivity.this).setTitle("请输入桌号").setIcon(
						android.R.drawable.ic_dialog_info).setView(
						customDialog).setPositiveButton("确定",new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int which) {
								String text = tableIdTV.getText().toString();
								if((text==null)||(text.trim().equals(""))){
									dialog.dismiss();
									Toast.makeText(CurrentOrderActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
								}else{
									AppController.getInstance().setTableId(text);
									tableId = text;
									orderInfo.commit();
									willAdapter.notifyDataSetChanged();
									totalTV.setText("￥"+orderInfo.getCommitedSum());
									comAdapter.notifyDataSetChanged();
								}
							}
							
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();
				}else{
					orderInfo.commit();
					willAdapter.notifyDataSetChanged();
					totalTV.setText("￥"+orderInfo.getCommitedSum());
					comAdapter.notifyDataSetChanged();
				}
			}
		});
		checkOutBT.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
			}
		});
	}
	class WillCommitGridAdapter extends BaseAdapter{
		ArrayList<OrderGoods> willCommitInfo = orderInfo.getWillCommitOrders();
		public int getCount() {
			return orderInfo.getWillCommitOrders().size();
		}
		public Object getItem(int position) {
			return position;
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(final int index, View convertView, ViewGroup parent) {
			View view = convertView;
			WillCommitViewHolder holder = null;
			final int number = willCommitInfo.get(index).getNumber();
			final String goodsName = willCommitInfo.get(index).getGoodsName();
			String url = getResources().getString(R.string.server_port)+"/wspusers/"+wspId+"/"+willCommitInfo.get(index).getImageUrl();
			final double price = willCommitInfo.get(index).getPrice();
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
				holder.deleteTV = (TextView)view.findViewById(R.id.delete);
				view.setTag(holder);
			}else{
				holder = (WillCommitViewHolder) view.getTag();
			}
			final TextView nameTV= holder.nameTV;
			nameTV.setText(goodsName);
			final TextView priceTV = holder.priceTV;
			priceTV.setText("￥"+price);
			holder.nimageView.setImageUrl(url, imageLoader);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(CurrentOrderActivity.this,R.layout.spinner_item,R.id.spinner_content,numbers);
			adapter.setDropDownViewResource(R.layout.spinner_item_down);
			holder.orderNumSp.setAdapter(adapter);
			holder.orderNumSp.setSelection(number-1);
			holder.orderNumSp.setOnItemSelectedListener(new OnItemSelectedListener(){
				public void onItemSelected(AdapterView<?> parent, View view,int num, long id) {
					orderInfo.setWillCommit(index, num+1);
					priceTV.setText("￥"+(num+1)*(price/number));
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
				
			});
			holder.deleteTV.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(CurrentOrderActivity.this)
						.setIcon(R.drawable.alert)
						.setTitle("您确定要移除"+goodsName+"?")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								orderInfo.willCommitDelete(index);
								willAdapter.notifyDataSetChanged();
							}
						}).setNegativeButton("取消", null).create().show();
						
				}
			});
			return view;
		}
	}
	class WillCommitViewHolder{
		NetworkImageView nimageView;
		TextView nameTV;
		TextView priceTV;
		Spinner orderNumSp;
		TextView deleteTV;
	}
	class CommitedGridAdapter extends BaseAdapter{
		ArrayList<OrderGoods> commitedInfo = orderInfo.getCommitedOrders();
		public int getCount() {
			return orderInfo.getCommitedOrders().size();
		}
		public Object getItem(int position) {
			return position;
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(int index, View convertView, ViewGroup parent) {
			View view = convertView;
			CommitedViewHolder holder = null;
			int number = commitedInfo.get(index).getNumber();
			double price = commitedInfo.get(index).getPrice();
			String goodsName = commitedInfo.get(index).getGoodsName();
			String url = getResources().getString(R.string.server_port)+"/wspusers/"+wspId+"/"+commitedInfo.get(index).getImageUrl();
			if(convertView==null){
				view = inflater.inflate(R.layout.commited_item, null);
				holder = new CommitedViewHolder();
				holder.nimageView = (NetworkImageView)view.findViewById(R.id.iv_image);
				holder.nameTV = (TextView)view.findViewById(R.id.commited_dish_name);
				holder.comNumTV = (TextView)view.findViewById(R.id.commited_order_num);
				holder.priceTV = (TextView)view.findViewById(R.id.commited_price);
				view.setTag(holder);
			}else{
				holder = (CommitedViewHolder) view.getTag();
				holder.nimageView.setImageUrl(url, imageLoader);
				holder.nameTV.setText(goodsName);
				holder.priceTV.setText("￥"+price);
				holder.comNumTV.setText(number+"份");
			}
			
			return view;
		}
	}
	class CommitedViewHolder{
		NetworkImageView nimageView;
		TextView nameTV;
		TextView priceTV;
		TextView comNumTV;
	}
	public void setGridViewHeight(GridView gridView) {  
        // get the list view adapter, so this function must be invoked after set the adapter.  
        BaseAdapter gridAdapter = (BaseAdapter) gridView.getAdapter();  
        if (gridAdapter == null) {  
            return;  
        }  
          
        int totalHeight = 0;  
        // get the ListView count  
        int count = gridAdapter.getCount();  
        for (int i = 0; i < count; i++) {
        	if(i%2==0){
                View gridItem = gridAdapter.getView(i, null, gridView);  
                // measure the child view  
                gridItem.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
                // calculate the total height of items  
                totalHeight += gridItem.getMeasuredHeight(); 
        	} 
        }  
          
        ViewGroup.LayoutParams params = gridView.getLayoutParams();  
        // get divider height for all items and add the total height  
        params.height = totalHeight;  
        gridView.setLayoutParams(params);  
    }  
//	class CurrentOrderTask extends SynTask<>
}
