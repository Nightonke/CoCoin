package com.nightonke.saver;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 伟平 on 2015/10/20.
 */
public class TagViewFragmentAdapter extends FragmentStatePagerAdapter {

    public TagViewFragmentAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return TagViewFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return RecordManager.TAGS.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return RecordManager.TAGS.get(position % RecordManager.TAGS.size()).getName();
    }
}
