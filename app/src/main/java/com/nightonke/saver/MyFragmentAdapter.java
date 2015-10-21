package com.nightonke.saver;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by 伟平 on 2015/10/20.
 */
public class MyFragmentAdapter extends FragmentStatePagerAdapter {

    public MyFragmentAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return RecordManager.TAGS.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return RecordManager.TAGS.get(position % RecordManager.TAGS.size());
    }
}
