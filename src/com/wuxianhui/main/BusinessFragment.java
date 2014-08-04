package com.wuxianhui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jsondemo.activity.R;
import com.wuxianhui.business.PlaceOrderActivity;

public class BusinessFragment extends Fragment {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_business,container,false);
		Button iWillOrderButton = (Button)view.findViewById(R.id.i_will_order);
		iWillOrderButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				FragmentManager fm = ((MainActivity) getActivity()).getFm();
				FragmentTransaction ft = fm.beginTransaction();
				ft.addToBackStack(null);
				ft.commit();
				Intent intent = new Intent(getActivity(),PlaceOrderActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}
}
