package com.jsondemo.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class AvilableWifiFragment extends Fragment {
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合

	private int[] imageResId; // 图片ID
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem = 0; // 当前图片的索引号

	// An ExecutorService that can schedule commands to run after a given delay,or to execute periodically.
	private ScheduledExecutorService scheduledExecutorService;

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
		View view = inflater.inflate(R.layout.fragment_avilable_wifi,container,false);
		imageResId = new int[] { R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e };
		//titles = new String[imageResId.length];
		imageViews = new ArrayList<ImageView>();

		// 初始化图片资源
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}
		dots = new ArrayList<View>();
		dots.add(view.findViewById(R.id.v_dot0));
		dots.add(view.findViewById(R.id.v_dot1));
		dots.add(view.findViewById(R.id.v_dot2));
		dots.add(view.findViewById(R.id.v_dot3));
		dots.add(view.findViewById(R.id.v_dot4));
		viewPager = (ViewPager) view.findViewById(R.id.vp);
		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		return view;
	}
	public void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每三秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 3, 3, TimeUnit.SECONDS);
		super.onStart();
	}
	public void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		super.onStop();
	}
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
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
			return imageResId.length;
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
