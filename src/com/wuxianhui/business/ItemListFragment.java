package com.wuxianhui.business;

import com.jsondemo.activity.R;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class ItemListFragment extends ListFragment{
	String[] imageUrls = new String[]{
			"http://www.maxcoo.com.cn/mxhm/msj/multi/pix/mxc201031514931865158.jpg",
			"http://www.7qsj.cn/uploads/allimg/100514/1430243D6-0.jpg",
			"http://pic17.nipic.com/20111020/1365591_133021352000_2.jpg",
			"http://pic17.nipic.com/20111020/1365591_132501134000_2.jpg",
			"http://a3.att.hudong.com/18/94/05300000874931127768941945288.jpg"
	};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setListAdapter(new ListAdapter());
	}
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_item_list, container, false);
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
			
			return null;
		}
		
	}
}
