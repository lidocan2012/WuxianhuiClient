package com.wuxianhui.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jsondemo.activity.R;

public class MainActivity extends FragmentActivity {
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    LayoutInflater inflater;
    int count = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        inflater = LayoutInflater.from(this);
        fragmentManager = this.getSupportFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        String menuString = getIntent().getStringExtra("menuString");
        if(menuString!=null&&!menuString.equals("")){
        	setRadioGroup(menuString);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment fragment = FragmentFactory.getInstanceByIndex(group.getCheckedRadioButtonId());
                transaction.replace(R.id.main_content, fragment);
                transaction.commit();
            }
        });
        radioGroup.check(1);
    }
    private void setRadioGroup(String menuString) {
    	if(menuString.contains("门户"))
            addRadioButton(1,"门户",R.drawable.lock);
    	if(menuString.contains("商务"))
            addRadioButton(2,"商务",R.drawable.lock);
    	if(menuString.contains("WSP搜索"))
            addRadioButton(3,"WSP搜索",R.drawable.lock);
    	if(menuString.contains("我的"))
            addRadioButton(4,"我的",R.drawable.lock);
	}
	public void addRadioButton(int id,String text,int drawableId){
    	RadioButton radioButton = (RadioButton)inflater.inflate(R.layout.radio_button, null);
        radioButton.setId(id);
        radioButton.setText(text);
        Drawable topDrawable = getResources().getDrawable(drawableId);
        radioButton.setCompoundDrawablesWithIntrinsicBounds(null, topDrawable, null, null);
        radioGroup.addView(radioButton);
        radioButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
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