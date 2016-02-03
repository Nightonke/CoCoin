package com.nightonke.saver.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nightonke.saver.R;
import com.nightonke.saver.activity.CoCoinApplication;
import com.nightonke.saver.fragment.HelpCoCoinFragment;
import com.nightonke.saver.fragment.HelpFeedbackFragment;
import com.nightonke.saver.fragment.HelpMeFragment;
import com.nightonke.saver.fragment.HelpOpenLibraryFragment;

/**
 * Created by Weiping on 2016/2/2.
 */

public class HelpFragmentAdapter extends FragmentStatePagerAdapter {

    public HelpFragmentAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return HelpCoCoinFragment.newInstance();
            case 1: return HelpFeedbackFragment.newInstance();
            case 2: return HelpOpenLibraryFragment.newInstance();
            case 3: return HelpMeFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return CoCoinApplication.getAppContext().getResources().getString(R.string.help_cocoin_title);
            case 1: return CoCoinApplication.getAppContext().getResources().getString(R.string.help_feedback_title);
            case 2: return CoCoinApplication.getAppContext().getResources().getString(R.string.help_open_library_title);
            case 3: return CoCoinApplication.getAppContext().getResources().getString(R.string.help_me_title);
        }
        return "";
    }
}
