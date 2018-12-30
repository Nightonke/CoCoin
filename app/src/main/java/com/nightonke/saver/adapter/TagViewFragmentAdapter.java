package com.nightonke.saver.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.nightonke.saver.activity.CoCoinApplication;
import com.nightonke.saver.fragment.TagViewFragment;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.util.CoCoinUtil;

/**
 * Created by 伟平 on 2015/10/20.
 */
public class TagViewFragmentAdapter extends FragmentStatePagerAdapter {

    public TagViewFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return TagViewFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return RecordManager.getInstance(CoCoinApplication.getAppContext()).TAGS.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CoCoinUtil.GetTagName(
                RecordManager.getInstance(CoCoinApplication.getAppContext()).TAGS.get(position % RecordManager.TAGS.size()).getId());
    }
}
