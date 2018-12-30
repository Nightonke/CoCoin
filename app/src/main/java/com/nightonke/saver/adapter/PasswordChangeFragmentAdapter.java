package com.nightonke.saver.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nightonke.saver.fragment.PasswordChangeFragment;

/**
 * Created by Weiping on 2016/1/19.
 */

public class PasswordChangeFragmentAdapter extends FragmentPagerAdapter {

    public PasswordChangeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PasswordChangeFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
