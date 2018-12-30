package com.nightonke.saver.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.nightonke.saver.fragment.CustomViewFragment;

/**
 * Created by 伟平 on 2015/10/20.
 */

// Todo optimize this

public class CustomViewFragmentAdapter extends FragmentStatePagerAdapter {

    public CustomViewFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return CustomViewFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
