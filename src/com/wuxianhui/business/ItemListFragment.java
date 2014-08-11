package com.wuxianhui.business;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jsondemo.activity.R;
import com.wuxianhui.tools.AppController;

public class ItemListFragment extends ListFragment{
	String[] imageUrls;
	String[] prices;
	String[] dishNames;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	LayoutInflater inflater =null;
	public interface ListFragmentCallBack{  
        public void onItemSelected(int position);  
    }
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageUrls = ((PlaceOrderActivity)getActivity()).getImageUrls();
		prices = ((PlaceOrderActivity)getActivity()).getPrices();
		dishNames = ((PlaceOrderActivity)getActivity()).getDishNames();
		this.setListAdapter(new ListAdapter());
	}
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		this.inflater = inflater;
		return inflater.inflate(R.layout.fragment_item_list, container, false);
	}
	public void onListItemClick(ListView parent, View v, final int position, long id) {
		super.onListItemClick(parent, v, position, id);
		final ViewHolder holder= (ViewHolder)v.getTag();
		holder.placeBT.setVisibility(View.VISIBLE);
		holder.placeBT.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				holder.placeBT.setVisibility(View.INVISIBLE);
				((ListFragmentCallBack) getActivity()).onItemSelected(position);
			}
		});
	}
	class ListAdapter extends BaseAdapter{

		public int getCount() {
			return imageUrls.length;
		}
		public Object getItem(int position) {
			return position;
		}
		public long getItemId(int position) {
			return position;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder viewHolder = null;
			if(convertView == null){
				view = inflater.inflate(R.layout.list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.nimageView=(NetworkImageView)view.findViewById(R.id.iv_image);
				viewHolder.priceTV=(TextView)view.findViewById(R.id.price);
				viewHolder.nameTV=(TextView)view.findViewById(R.id.name);
				viewHolder.placeBT=(Button)view.findViewById(R.id.a_order);
				view.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)view.getTag();
			}
			viewHolder.nameTV.setText(dishNames[position]);
			viewHolder.priceTV.setText(prices[position]);
			viewHolder.nimageView.setImageUrl(imageUrls[position],imageLoader);
			return view;
		}
		
	}
	class ViewHolder{
		NetworkImageView nimageView;
		TextView nameTV;
		TextView priceTV;
		Button placeBT;
	}
}
