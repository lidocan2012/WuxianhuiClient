package com.wuxianhui.initactivity;

import com.jsondemo.activity.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;
public class WelcomeActivity extends FragmentActivity {
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	int count = 0;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		fragmentManager = this.getSupportFragmentManager();
		Button button = (Button)findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				fragmentTransaction = fragmentManager.beginTransaction();
				AdFragment adFragment = new AdFragment();
				fragmentTransaction.replace(R.id.welcome_content, adFragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				fragmentTransaction.commit();
			}
		});
	}
}
