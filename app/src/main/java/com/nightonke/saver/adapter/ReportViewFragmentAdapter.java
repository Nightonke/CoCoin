package com.nightonke.saver.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.nightonke.saver.fragment.ReportViewFragment;

/**
 * Created by 伟平 on 2015/10/20.
 */

// Todo optimize this

public class ReportViewFragmentAdapter extends FragmentStatePagerAdapter {

    public ReportViewFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return ReportViewFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ReportViewFragment.REPORT_TITLE;
    }
}
