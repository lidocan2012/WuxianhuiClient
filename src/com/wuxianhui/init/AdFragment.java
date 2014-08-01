package com.wuxianhui.init;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jsondemo.activity.R;
import com.wuxianhui.main.MainActivity;
import com.wuxianhui.tools.AppController;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

public class AdFragment extends Fragment{
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<NetworkImageView> imageViews; // 滑动的图片集合
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem = 0; // 当前图片的索引号
	private String[] imageUrls = new String[]{
			"http://www.maxcoo.com.cn/mxhm/msj/multi/pix/mxc201031514931865158.jpg",
			"http://www.7qsj.cn/uploads/allimg/100514/1430243D6-0.jpg",
			"http://pic17.nipic.com/20111020/1365591_133021352000_2.jpg",
			"http://pic17.nipic.com/20111020/1365591_132501134000_2.jpg",
			"http://a3.att.hudong.com/18/94/05300000874931127768941945288.jpg"
	};
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_ad,container,false);
		imageViews = new ArrayList<NetworkImageView>();

		// 初始化图片资源
		for (int i = 0; i < imageUrls.length; i++) {
			NetworkImageView nimageView = new NetworkImageView(getActivity());
			nimageView.setImageUrl(imageUrls[i], imageLoader);
			nimageView.setScaleType(ScaleType.FIT_XY);
			imageViews.add(nimageView);
		}
		imageViews.get(imageUrls.length-1).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),MainActivity.class);
				intent.putExtra("menuString", ((WelcomeActivity) getActivity()).getMenuString());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.in_anim, R.anim.out_anim);
				getActivity().finish();
			}
		});
		dots = new ArrayList<View>();
		dots.add(view.findViewById(R.id.v_dot0));
		dots.add(view.findViewById(R.id.v_dot1));
		dots.add(view.findViewById(R.id.v_dot2));
		dots.add(view.findViewById(R.id.v_dot3));
		dots.add(view.findViewById(R.id.v_dot4));
		viewPager = (ViewPager)view.findViewById(R.id.vp);
		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		return view;
	}
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;
		public void onPageSelected(int position) {
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}
		public void onPageScrollStateChanged(int arg0) {

		}
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}
	private class MyAdapter extends PagerAdapter {
		public int getCount() {
			return imageUrls.length;
		}
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}
		public Parcelable saveState() {
			return null;
		}
		public void startUpdate(View arg0) {

		}
		public void finishUpdate(View arg0) {

		}
	}
}
