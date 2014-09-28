package com.wuxianhui.business;

import java.util.List;

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
import com.example.wsplog.app1.R;
import com.wuxianhui.tools.AppController;

public class ItemListFragment extends ListFragment{
	int indexOfGoodsTypes;
	List<String> imageUrls;
	List<Double> prices;
	List<String> dishNames;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	long wspId = AppController.getInstance().getWspId();
	LayoutInflater inflater =null;
	public ItemListFragment(int indexOfGoodsTypes, List<String> imageUrls,
			List<Double> prices, List<String> dishNames) {
		super();
		this.indexOfGoodsTypes = indexOfGoodsTypes;
		this.imageUrls = imageUrls;
		this.prices = prices;
		this.dishNames = dishNames;
	}
	public interface ListFragmentCallBack{
        public void onItemSelected(int indexOfGoodsType,int position);  
    }
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setListAdapter(new ListAdapter());
	}
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		this.inflater = inflater;
		return inflater.inflate(R.layout.fragment_item_list, container, false);
	}
	public void onListItemClick(ListView parent, View v, final int position, long id) {
		super.onListItemClick(parent, v, position, id);
		final ViewHolder holder= (ViewHolder)v.getTag();
		if(!(holder.placeBT.isShown())){
			holder.placeBT.setVisibility(View.VISIBLE);
			holder.placeBT.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					holder.placeBT.setVisibility(View.INVISIBLE);
					((ListFragmentCallBack) getActivity()).onItemSelected(indexOfGoodsTypes,position);
				}
			});
		}else{
			holder.placeBT.setVisibility(View.GONE);
		}
		
	}
	class ListAdapter extends BaseAdapter{

		public int getCount() {
			return imageUrls.size();
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
			viewHolder.nameTV.setText(dishNames.get(position));
			viewHolder.priceTV.setText("гд"+prices.get(position));
			String url = getResources().getString(R.string.server_port)+"/wspusers/"+wspId+"/"+imageUrls.get(position);
			viewHolder.nimageView.setImageUrl(url,imageLoader);
			viewHolder.placeBT.setFocusable(false);
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
