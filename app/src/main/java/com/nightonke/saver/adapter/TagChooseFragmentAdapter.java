package com.nightonke.saver.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nightonke.saver.fragment.TagChooseFragment;

/**
 * Created by Weiping on 2016/1/19.
 */

public class TagChooseFragmentAdapter extends FragmentPagerAdapter {

    private int count;

    public TagChooseFragmentAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {
        return TagChooseFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return count;
    }
}
