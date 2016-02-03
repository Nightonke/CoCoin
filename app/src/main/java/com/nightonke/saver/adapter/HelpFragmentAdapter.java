package com.nightonke.saver.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nightonke.saver.R;
import com.nightonke.saver.activity.CoCoinApplication;
import com.nightonke.saver.fragment.HelpAboutFragment;
import com.nightonke.saver.fragment.HelpCoCoinFragment;
import com.nightonke.saver.fragment.HelpFeedbackFragment;

/**
 * Created by Weiping on 2016/2/2.
 */

public class HelpFragmentAdapter extends FragmentStatePagerAdapter {

    private int position = 0;

    public HelpFragmentAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    public HelpFragmentAdapter(android.support.v4.app.FragmentManager fm, int position) {
        super(fm);
        this.position = position;
    }

    @Override
    public Fragment getItem(int position) {
        switch (this.position) {
            case 0: return HelpCoCoinFragment.newInstance();
            case 1: return HelpFeedbackFragment.newInstance();
            case 2: return HelpAboutFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (this.position) {
            case 0: return CoCoinApplication.getAppContext().getResources().getString(R.string.app_name);
            case 1: return CoCoinApplication.getAppContext().getResources().getString(R.string.feedback);
            case 2: return CoCoinApplication.getAppContext().getResources().getString(R.string.about);
        }
        return "";
    }
}
