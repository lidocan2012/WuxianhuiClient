package com.jsondemo.tools;

import com.jsondemo.activity.AvilableWifiFragment;
import com.jsondemo.activity.HistoryFragment;
import com.jsondemo.activity.WspSeacherFragment;
import com.jsondemo.activity.MyFragment;

import android.support.v4.app.Fragment;

public class FragmentFactory {
    public static Fragment getInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case 1:
                fragment = new AvilableWifiFragment();
                break;
            case 2:
                fragment = new HistoryFragment();
                break;
            case 3:
                fragment = new WspSeacherFragment();
                break;
            case 4:
                fragment = new MyFragment();
                break;
        }
        return fragment;
    }
}
