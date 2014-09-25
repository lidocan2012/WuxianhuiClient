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
	/** ���˵�TextView�ؼ� */
	private TextView[] textViews;
	private TextView curTxt;
	
	/** ��������ScrollView */
	private HorizontalScrollView horiScroll;
	private LinearLayout linearLayout;
	private ViewPager vPager_Sc;
	/** ViewPager���������б� */
	private ArrayList<Fragment> pageList;
	/** �ƶ�С���� */
	private ImageView imgTransBg;
	/** �Զ���ViewPager������ */
	private ChatAdatper mAdapter;
	/** �ֻ���Ļ�Ŀ�� */
	private int disWidth;
	/** ����ScrollViewλ��ʱ�����ƫ�� */
	private int offset = 0;
	/** �Ƿ��Ѿ�������ƫ��ı�ʶ */
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
		// ��ǰTextViewĬ��Ϊ��һ��
		curTxt = textViews[0];
		// ����Ĭ��λ�õ�������ɫΪ��ɫ��ѡ��Ч��
		textViews[0].setTextColor(getResources().getColor(R.color.white));
		// ��ȡ�ֻ���Ļ���
		disWidth = getWindowManager().getDefaultDisplay().getWidth();
		Log.i(TAG, "�ֻ���Ļ���disWidth:" + disWidth);
	}

	/** ����ViewPager���������� */
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
	 * ����С������ƶ�����Ч��(û����ƫ��,������Ч������ȫʵ��)
	 * 
	 * @param endTxt
	 */
	private void imgTrans(TextView endTxt) {
		// ��ǰTextView�����ĵ�
		int startMid = curTxt.getLeft() + curTxt.getWidth() / 2;
		// �ƶ���ʼλ�����Ե
		int startLeft = startMid - imgTransBg.getWidth() / 2;
		// Ŀ��TextView�����ĵ�
		int endMid = endTxt.getLeft() + endTxt.getWidth() / 2;
		// �ƶ�����λ�����Ե
		int endLeft = endMid - imgTransBg.getWidth() / 2;
		// ���춯��
		TranslateAnimation move = new TranslateAnimation(startLeft, endLeft, 0,0);
		move.setDuration(100);
		move.setFillAfter(true);
		imgTransBg.startAnimation(move);

		/*
		 * ���²������ڴ���ScrollView���ݻ����λ������������Ĺ���, �Ա�ﵽ���õ��Ӿ�Ч��
		 */
		int[] location = new int[2];
		// ��ȡĿ��TextViw�ڵ�ǰ��Ļ�е������,��Ҫ�õ�X�᷽������:location[0]
		endTxt.getLocationOnScreen(location);
		// ����ScrollView��λ��
		if (location[0] < 0) {
			// Ŀ��λ�ó��������Ļ,�������������λ�õ����
			// �˴�ScrollViewֱ�Ӹ���λ�õ㻬��
			horiScroll.smoothScrollTo(endTxt.getLeft(), 0);
		} else if ((location[0] + endTxt.getWidth()) > disWidth) {
			// Ŀ��λ�ó����ұ���Ļ,�������������λ�õ��ұ�
			// �˴�ScrollView����㻬������
			horiScroll.smoothScrollBy(location[0] + endTxt.getWidth()
					- disWidth, 0);
		} else {
			// �˴����û������Ļ,Ҳ�豣��ԭ�ػ���
			// ������Ӹ�Ч��,�򻬿���ܳ��ֶ����ӳٻ�ͣ��
			horiScroll.smoothScrollTo(horiScroll.getScrollX(), 0);
		}

		// �л�������ɫ
		curTxt.setTextColor(getResources().getColor(R.color.deep_gray));
		endTxt.setTextColor(getResources().getColor(R.color.white));

		// ���µ�ǰTextView�ļ�¼
		curTxt = endTxt;
	}

	/** ƫ������ʱ���õ�,��������Ժ��� */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * ����getLocationOnScreen()���ܴ�����ƫ��:
			 * ��ΪgetLocationOnScreen()����Ҫ��UI��������ܻ�ȡ��ȷ��ֵ, ���Բ�����onCreate()��ֱ�Ӽ���,
			 * �˴�����һ��boolean��ʶ,�ڵ�һ�δ�����Ļʱ��ȡһ��
			 */
			if (curTxt == textViews[0] && !hasOffset) {
				// ��ȡ��һ��TextView������ֵ
				int[] heaDlocal = new int[2];
				curTxt.getLocationOnScreen(heaDlocal);
				// x�᷽��ƫ��ֵ
				offset = heaDlocal[0];
				Log.i(TAG, "ƫ��ֵoffset:" + offset);
				hasOffset = true;
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * ����С������ƶ�����Ч��(������ƫ��,ϸ��Ч������), �˴���ƫ����Ҫ��ʾ��ʱ����ܹ۲쵽
	 * 
	 * @param endTxt
	 */
	private void imgTransMod(TextView endTxt) {
		// ��ǰTextView�����ĵ�
		int startMid = curTxt.getLeft() + curTxt.getWidth() / 2;
		// �ƶ���ʼλ�����Ե
		int startLeft = startMid - imgTransBg.getWidth() / 2;
		// Ŀ��TextView�����ĵ�
		int endMid = endTxt.getLeft() + endTxt.getWidth() / 2;
		// �ƶ�����λ�����Ե
		int endLeft = endMid - imgTransBg.getWidth() / 2;
		// ���춯��
		TranslateAnimation move = new TranslateAnimation(startLeft, endLeft, 0,
				0);
		move.setDuration(100);
		move.setFillAfter(true);
		imgTransBg.startAnimation(move);

		/*
		 * ���²������ڴ���ScrollView���ݻ����λ������������Ĺ���,�Ա�ﵽ���õ��Ӿ�Ч��
		 */
		int[] location = new int[2];
		// ��ȡĿ��TextViw�ڵ�ǰ��Ļ�е������,��Ҫ�õ�X�᷽������:location[0]
		endTxt.getLocationOnScreen(location);

		// ����ScrollView��λ��
		if ((location[0] - offset) < 0) {
			Log.i(TAG, "��������Ļ");
			// Ŀ��λ�ó��������Ļ,�������������λ�õ����
			// �˴�ScrollViewֱ�Ӹ���λ�õ㻬��
			horiScroll.smoothScrollTo(endTxt.getLeft(), 0);
		} else if ((location[0] + endTxt.getWidth()) > disWidth) {
			Log.i(TAG, "��������Ļ");
			Log.i(TAG, "�ƶ����ص�:"
					+ (location[0] + offset + endTxt.getWidth() - disWidth));
			// Ŀ��λ�ó����ұ���Ļ,�������������λ�õ��ұ�
			// �˴�ScrollView����㻬������
			horiScroll.smoothScrollBy(location[0] + offset + endTxt.getWidth()
					- disWidth, 0);
		} else {
			// ���û������Ļ,Ҳ�豣��ԭ�ػ���
			// �����Ӹ�Ч��,�򻬿���ܳ��ֶ����ӳٻ�ͣ��
			horiScroll.smoothScrollTo(horiScroll.getScrollX(), 0);
		}

		// �л�������ɫ
		curTxt.setTextColor(getResources().getColor(R.color.deep_gray));
		endTxt.setTextColor(getResources().getColor(R.color.white));

		// ���µ�ǰTextView�ļ�¼
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
