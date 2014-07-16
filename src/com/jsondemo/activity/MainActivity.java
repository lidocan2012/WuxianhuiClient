package com.jsondemo.activity;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity{
	private TabHost tabHost;  
    private LayoutInflater layoutInflater;  
    String[] titles = new String[] { "首页", "留言", "评论", "收藏", "我的" };  
    int[] icons = new int[] { R.drawable.home, R.drawable.lock,  
            R.drawable.lock, R.drawable.lock, R.drawable.user };  
    int[] tabIds = new int[] { R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4,  
            R.id.tab5 };
    GestureDetector gestureDetector;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		setUser();
	}
	public void init() {   
		ActionBar actionbar = getActionBar();
		actionbar.hide();
        tabHost = getTabHost();  
        layoutInflater = LayoutInflater.from(this);  
        for (int i = 0; i < titles.length; i++) {  
            TabSpec tabSpec = tabHost.newTabSpec(titles[i])  
                    .setIndicator(getTabItemView(i)).setContent(tabIds[i]);  
            tabHost.addTab(tabSpec);  
            tabHost.getTabWidget().getChildAt(i)  
                    .setBackgroundResource(R.drawable.tabwidget_background);  
            tabHost.setup();
        }
        gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
			public void onShowPress(MotionEvent e) {
			}
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				return false;
			}
			public void onLongPress(MotionEvent e) {
			}
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				int currentTab = tabHost.getCurrentTab();
				if(e2.getRawX()-e1.getRawX()>80&&currentTab>0){
					tabHost.setCurrentTab(currentTab-1);
				}
				if(e1.getRawX()-e2.getRawX()>80&&currentTab<4){
					tabHost.setCurrentTab(currentTab+1);
				}
				return false;
			}
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
        
	}
	public View getTabItemView(int i) { 
        View view = layoutInflater.inflate(R.layout.tabwidget_layout, null);  
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);  
        imageView.setImageResource(icons[i]);  
        TextView textView = (TextView) view.findViewById(R.id.textview);  
        textView.setText(titles[i]);  
        return view;  
    }
	public boolean onTouchEvent(MotionEvent event){
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	public void setUser(){
		RelativeLayout userLayout = (RelativeLayout)findViewById(R.id.tab5);
		RelativeLayout clickLogin = (RelativeLayout)userLayout.findViewById(R.id.click_login_layout);
		clickLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
	}
}