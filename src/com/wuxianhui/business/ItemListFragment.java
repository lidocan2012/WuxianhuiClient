package com.wuxianhui.business;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jsondemo.activity.R;
import com.wuxianhui.tools.AppController;

public class ItemListFragment extends ListFragment{
	String[] imageUrls = new String[]{
			"http://www.maxcoo.com.cn/mxhm/msj/multi/pix/mxc201031514931865158.jpg",
			"http://www.7qsj.cn/uploads/allimg/100514/1430243D6-0.jpg",
			"http://pic17.nipic.com/20111020/1365591_133021352000_2.jpg",
			"http://pic17.nipic.com/20111020/1365591_132501134000_2.jpg",
			"http://a3.att.hudong.com/18/94/05300000874931127768941945288.jpg"
	};
	String[] prices = new String[]{"£§88","£§69","£§89","£§79","£§98"};
	String[] dishNames = new String[]{"Ã„Ûo","¬È∆≈∂π∏Ø","∏…π¯œ∫","—º¿œø«","∫Ï…’»‚"};
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	LayoutInflater inflater =null;
	public interface ListFragmentCallBack{  
        public void onItemSelected(View view,int position,long id);  
    } 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setListAdapter(new ListAdapter());
	}
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		this.inflater = inflater;
		return inflater.inflate(R.layout.fragment_item_list, container, false);
	}
	public void onListItemClick(ListView parent, View v, int position, long id) {
		super.onListItemClick(parent, v, position, id);
		((ListFragmentCallBack) getActivity()).onItemSelected(v,position,id);
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
				viewHolder.price=(TextView)view.findViewById(R.id.price);
				viewHolder.name=(TextView)view.findViewById(R.id.name);
				view.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)view.getTag();
			}
			viewHolder.name.setText(dishNames[position]);
			viewHolder.price.setText(prices[position]);
			viewHolder.nimageView.setImageUrl(imageUrls[position],imageLoader);
			return view;
		}
		
	}
	class ViewHolder{
		NetworkImageView nimageView;
		TextView name;
		TextView price;
	}
}
