package com.wuxianhui.business;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsplog.app1.R;
import com.wuxianhui.business.ItemListFragment.ListFragmentCallBack;
import com.wuxianhui.tools.AppController;
import com.wuxianhui.tools.GoodsClass;
import com.wuxianhui.tools.GoodsInfo;
import com.wuxianhui.tools.OrderInformation;

public class PlaceOrderActivity extends FragmentActivity implements OnClickListener,OnPageChangeListener,ListFragmentCallBack{
	private static final String TAG = "ScrollMenu---->";
	private static final int BASEID = 100;
	/** 各菜单TextView控件 */
	private TextView[] textViews;
	private TextView curTxt;
	
	/** 顶部横向ScrollView */
	private HorizontalScrollView horiScroll;
	private LinearLayout linearLayout;
	private ViewPager vPager_Sc;
	/** ViewPager内容数据列表 */
	private ArrayList<Fragment> pageList;
	/** 移动小方框 */
	private ImageView imgTransBg;
	/** 自定义ViewPager适配器 */
	private ChatAdatper mAdapter;
	/** 手机屏幕的宽度 */
	private int disWidth;
	/** 调整ScrollView位置时计算的偏差 */
	private int offset = 0;
	/** 是否已经计算了偏差的标识 */
	private boolean hasOffset = false;
	FragmentManager fm;
	FragmentTransaction ft;
	TextView orderSumTV;
	TextView animBallTV;
	List<String> goodsTypes = AppController.getInstance().getGoodsInfo().getGoodsTypes();
	List<GoodsClass> goodsClasses = AppController.getInstance().getGoodsInfo().getGoodsClasses();
	OrderInformation orderInfo;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_place_order);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_placeorder_layout);
		fm = this.getSupportFragmentManager();
		orderInfo = AppController.getInstance().getOrderInfo();
		ImageView backIV = (ImageView)findViewById(R.id.back);
		orderSumTV = (TextView)findViewById(R.id.order_sum);
		orderSumTV.setText(orderInfo.getWillCommitNum()+"");
		if(orderInfo.getWillCommitNum()>0){
			orderSumTV.setVisibility(View.VISIBLE);
		}
		ImageButton orderSumBT = (ImageButton)findViewById(R.id.order_sum_bt);
		orderSumBT.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PlaceOrderActivity.this,CurrentOrderActivity.class);
				startActivity(intent);
			}
		});
		backIV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		initView();
		setPageAdapter();
	}
	protected void onResume(){
		super.onResume();
		if(orderSumTV!=null){
			orderSumTV.setText(orderInfo.getWillCommitNum()+"");
		}
		if(orderInfo.getWillCommitNum()==0){
			orderSumTV.setVisibility(View.INVISIBLE);
		}
	}
	public FragmentManager getFm(){
		return fm;
	}
	public TextView getOrderSumTV(){
		return orderSumTV;
	}
	public void onItemSelected(int indexOfGoodsType,int position) {
		orderInfo.addWillCommit(indexOfGoodsType,position);
		orderSumTV.setText(orderInfo.getWillCommitNum()+"");
		orderSumTV.setVisibility(View.VISIBLE);
		orderSumTV.startAnimation(AnimationUtils.loadAnimation(this, R.anim.jump_ainm));
	}
	private void initView() {
		textViews = new TextView[goodsTypes.size()];
		horiScroll = (HorizontalScrollView) findViewById(R.id.horiScroll);
		linearLayout = (LinearLayout)findViewById(R.id.linear_layout);
		vPager_Sc = (ViewPager) findViewById(R.id.vPager_Sc);
		imgTransBg = (ImageView) findViewById(R.id.imgTransBg);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(30, 10, 30, 10);
		for(int i=0;i<textViews.length;i++){
			textViews[i] = new TextView(this);
			linearLayout.addView(textViews[i],lp);
			textViews[i].setText(goodsTypes.get(i));
			textViews[i].setId(BASEID+i);
			textViews[i].setTextAppearance(this, R.style.scroll_text_style);
			textViews[i].setOnClickListener(this);
		}
		vPager_Sc.setOnPageChangeListener(this);
		// 当前TextView默认为第一个
		curTxt = textViews[0];
		// 设置默认位置的字体颜色为白色的选中效果
		textViews[0].setTextColor(getResources().getColor(R.color.white));
		// 获取手机屏幕宽度
		disWidth = getWindowManager().getDefaultDisplay().getWidth();
		Log.i(TAG, "手机屏幕宽度disWidth:" + disWidth);
	}

	/** 设置ViewPager数据适配器 */
	private void setPageAdapter() {
		pageList = new ArrayList<Fragment>();
		for(int i=0;i<goodsTypes.size();i++){
			List<String> imageUrls =goodsClasses.get(i).getImageUrls();
			List<String> goodsNames =goodsClasses.get(i).getDishNames();
			List<Double> prices = goodsClasses.get(i).getPrices();
			Fragment fragment = new ItemListFragment(i,imageUrls,prices,goodsNames);
			pageList.add(fragment);
		}
		mAdapter = new ChatAdatper(fm,pageList);
		vPager_Sc.setAdapter(mAdapter);
	}

	/**
	 * 设置小方框的移动动画效果(没计算偏差,但基本效果能完全实现)
	 * 
	 * @param endTxt
	 */
	private void imgTrans(TextView endTxt) {
		// 当前TextView的中心点
		int startMid = curTxt.getLeft() + curTxt.getWidth() / 2;
		// 移动开始位置左边缘
		int startLeft = startMid - imgTransBg.getWidth() / 2;
		// 目的TextView的中心点
		int endMid = endTxt.getLeft() + endTxt.getWidth() / 2;
		// 移动结束位置左边缘
		int endLeft = endMid - imgTransBg.getWidth() / 2;
		// 构造动画
		TranslateAnimation move = new TranslateAnimation(startLeft, endLeft, 0,0);
		move.setDuration(100);
		move.setFillAfter(true);
		imgTransBg.startAnimation(move);

		/*
		 * 以下步骤用于处理ScrollView根据滑块的位置来调整自身的滚动, 以便达到更好的视觉效果
		 */
		int[] location = new int[2];
		// 获取目的TextViw在当前屏幕中的坐标点,主要用到X轴方向坐标:location[0]
		endTxt.getLocationOnScreen(location);
		// 调整ScrollView的位置
		if (location[0] < 0) {
			// 目的位置超出左边屏幕,则调整到紧靠该位置的左边
			// 此处ScrollView直接根据位置点滑动
			horiScroll.smoothScrollTo(endTxt.getLeft(), 0);
		} else if ((location[0] + endTxt.getWidth()) > disWidth) {
			// 目的位置超出右边屏幕,则调整到紧靠该位置的右边
			// 此处ScrollView需计算滑动距离
			horiScroll.smoothScrollBy(location[0] + endTxt.getWidth()
					- disWidth, 0);
		} else {
			// 此处如果没超出屏幕,也需保持原地滑动
			// 如果不加该效果,则滑块可能出现动画延迟或停滞
			horiScroll.smoothScrollTo(horiScroll.getScrollX(), 0);
		}

		// 切换字体颜色
		curTxt.setTextColor(getResources().getColor(R.color.deep_gray));
		endTxt.setTextColor(getResources().getColor(R.color.white));

		// 更新当前TextView的记录
		curTxt = endTxt;
	}

	/** 偏差计算的时候用到,不用则可以忽略 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * 计算getLocationOnScreen()可能带来的偏差:
			 * 因为getLocationOnScreen()方法要等UI绘制完才能获取正确的值, 所以不能在onCreate()中直接计算,
			 * 此处设置一个boolean标识,在第一次触动屏幕时获取一次
			 */
			if (curTxt == textViews[0] && !hasOffset) {
				// 获取第一个TextView的坐标值
				int[] heaDlocal = new int[2];
				curTxt.getLocationOnScreen(heaDlocal);
				// x轴方向偏差值
				offset = heaDlocal[0];
				Log.i(TAG, "偏差值offset:" + offset);
				hasOffset = true;
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 设置小方框的移动动画效果(计算了偏差,细节效果更好), 此处的偏差需要演示的时候才能观察到
	 * 
	 * @param endTxt
	 */
	private void imgTransMod(TextView endTxt) {
		// 当前TextView的中心点
		int startMid = curTxt.getLeft() + curTxt.getWidth() / 2;
		// 移动开始位置左边缘
		int startLeft = startMid - imgTransBg.getWidth() / 2;
		// 目的TextView的中心点
		int endMid = endTxt.getLeft() + endTxt.getWidth() / 2;
		// 移动结束位置左边缘
		int endLeft = endMid - imgTransBg.getWidth() / 2;
		// 构造动画
		TranslateAnimation move = new TranslateAnimation(startLeft, endLeft, 0,
				0);
		move.setDuration(100);
		move.setFillAfter(true);
		imgTransBg.startAnimation(move);

		/*
		 * 以下步骤用于处理ScrollView根据滑块的位置来调整自身的滚动,以便达到更好的视觉效果
		 */
		int[] location = new int[2];
		// 获取目的TextViw在当前屏幕中的坐标点,主要用到X轴方向坐标:location[0]
		endTxt.getLocationOnScreen(location);

		// 调整ScrollView的位置
		if ((location[0] - offset) < 0) {
			Log.i(TAG, "超出左屏幕");
			// 目的位置超出左边屏幕,则调整到紧靠该位置的左边
			// 此处ScrollView直接根据位置点滑动
			horiScroll.smoothScrollTo(endTxt.getLeft(), 0);
		} else if ((location[0] + endTxt.getWidth()) > disWidth) {
			Log.i(TAG, "超出右屏幕");
			Log.i(TAG, "移动像素点:"
					+ (location[0] + offset + endTxt.getWidth() - disWidth));
			// 目的位置超出右边屏幕,则调整到紧靠该位置的右边
			// 此处ScrollView需计算滑动距离
			horiScroll.smoothScrollBy(location[0] + offset + endTxt.getWidth()
					- disWidth, 0);
		} else {
			// 如果没超出屏幕,也需保持原地滑动
			// 若不加该效果,则滑块可能出现动画延迟或停滞
			horiScroll.smoothScrollTo(horiScroll.getScrollX(), 0);
		}

		// 切换字体颜色
		curTxt.setTextColor(getResources().getColor(R.color.deep_gray));
		endTxt.setTextColor(getResources().getColor(R.color.white));

		// 更新当前TextView的记录
		curTxt = endTxt;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		imgTransMod(textViews[id-BASEID]);
		vPager_Sc.setCurrentItem(id-BASEID);
	}

	@Override
	public void onPageSelected(int arg0) {
		textViews[arg0].performClick();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}
}
