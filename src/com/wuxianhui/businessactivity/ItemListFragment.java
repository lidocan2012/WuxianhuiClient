package com.wuxianhui.businessactivity;

import com.jsondemo.activity.R;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ItemListFragment extends ListFragment{

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setListAdapter(new ArrayAdapter<String>(getActivity(),R.layout.list_item,new String[]{"hello","hi","me","you"}));
	}
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_item_list, container, false);
	}
}
