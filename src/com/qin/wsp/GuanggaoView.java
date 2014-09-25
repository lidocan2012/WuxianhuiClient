package com.qin.wsp;

import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import com.example.wsplog.app1.R;


//import com.zgl.testgis.MyIndoorView.TabFragmentPagerAdapter;

import android.app.ActionBar;

import android.app.ActionBar.Tab;

import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class GuanggaoView extends FragmentActivity implements TabListener{
	 static List<Fragment> fragmentList = new ArrayList<Fragment>();
	 List<String> titleList = new ArrayList<String>();
	 ViewPager vp;
	 private TabFragmentPagerAdapter mAdapter;
	 int guanggaocount=3;
	// private TabFragmentPagerAdapter mAdapter;
	 private static final String SELECTED_ITEM = "SELECTED_ITEM";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guanggaoye);
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		vp=(ViewPager)findViewById(R.id.viewpager);
		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		for(int i=1;i<=guanggaocount;i++)
		{
			fragmentList.add(new GuanggaoViewFragment(i));
			
			Tab tab = actionBar.newTab();
			tab.setText(mAdapter.getPageTitle(i-1)).setTabListener(this);
			tab.setText("第"+(i)+"页");
			actionBar.addTab(tab);
			
			
	//	
	}
		vp.setAdapter(mAdapter);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(arg0);
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	 
	
	
public static	class TabFragmentPagerAdapter extends FragmentPagerAdapter
	{

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return (fragmentList == null ? 0 : fragmentList.size());
		}}


public void onTabSelected(Tab tab, FragmentTransaction ft) {
	/*Fragment fragment = new MyIndoorViewFragment();
	//设置参数
	Bundle bundle = new Bundle();
	bundle.putInt("floor_number", getActionBar().getSelectedNavigationIndex());//得到导航条目的位置,如第一个
	fragment.setArguments(bundle);
	FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
	fTransaction.replace(R.id.container, fragment);
	fTransaction.commit();*/
	vp.setCurrentItem(tab.getPosition());  
}


@Override
protected void onSaveInstanceState(Bundle outState) {
	// TODO Auto-generated method stub
	outState.putInt(SELECTED_ITEM, getActionBar().getSelectedNavigationIndex());
}


@Override
protected void onRestoreInstanceState(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(SELECTED_ITEM));
}


public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}


public void onTabReselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}


}
