package com.jsondemo.activity;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerFragmentAdapter extends FragmentPagerAdapter {
	 
    private List<Fragment> fragmentList;
    private List<String> titleList;

    public MyPagerFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    // ViewPage中显示的内容
    public Fragment getItem(int arg0) {
        return (fragmentList == null || fragmentList.size() == 0) ? null
                : fragmentList.get(arg0);
    }

    // Title中显示的内容
    public CharSequence getPageTitle(int position) {
        return (titleList.size() > position) ? titleList.get(position) : "";
    }
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

}
