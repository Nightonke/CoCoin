package com.nightonke.saver.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nightonke.saver.fragment.EditMoneyFragment;
import com.nightonke.saver.fragment.EditRemarkFragment;

/**
 * Created by Weiping on 2016/1/19.
 */
public class EditMoneyRemarkFragmentAdapter extends FragmentPagerAdapter {

    private int type;

    public EditMoneyRemarkFragmentAdapter(FragmentManager fm, int type) {
        super(fm);
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return EditMoneyFragment.newInstance(0, type);
        else return EditRemarkFragment.newInstance(1, type);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
