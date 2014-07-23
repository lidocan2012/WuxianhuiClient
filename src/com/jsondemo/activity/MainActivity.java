package com.jsondemo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jsondemo.tools.FragmentFactory;

public class MainActivity extends FragmentActivity {
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    private int count=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        fragmentManager = this.getSupportFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment fragment = FragmentFactory.getInstanceByIndex(checkedId);
                transaction.replace(R.id.content, fragment);
                transaction.commit();
            }
        });
        radioGroup.check(1);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){  
        if (keyCode == KeyEvent.KEYCODE_BACK ){
        	count++;
        	if(count==1){
        		Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        	}else if(count>=2){
        		finish();
        	}
        }
        return true;
    }
}