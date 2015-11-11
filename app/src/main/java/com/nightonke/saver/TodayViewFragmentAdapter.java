package com.nightonke.saver;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class TodayViewFragmentAdapter extends FragmentStatePagerAdapter {

    private static int TODAY_VIEW_FRAGMENT_NUMBER = 8;

    public TodayViewFragmentAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return TodayViewFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return TODAY_VIEW_FRAGMENT_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Utils.GetTodayViewTitle(position % TODAY_VIEW_FRAGMENT_NUMBER);
    }
}
