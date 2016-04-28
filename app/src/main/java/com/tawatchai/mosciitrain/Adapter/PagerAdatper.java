package com.tawatchai.mosciitrain.Adapter;

/**
 * Created by tawatchaitongtea on 4/26/16 AD.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdatper extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String>titles = new ArrayList<>();

    public PagerAdatper(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titles.get(position);
    }

    public void addFragment(Fragment fragment, String title){

        fragments.add(fragment);
        titles.add(title);
    }
}
