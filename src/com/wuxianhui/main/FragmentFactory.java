package com.wuxianhui.main;

import android.support.v4.app.Fragment;

public class FragmentFactory {
    public static Fragment getInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case 1:
                fragment = new DoorFragment();
                break;
            case 2:
                fragment = new BusinessFragment();
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
