package com.nightonke.saver.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nightonke.saver.fragment.MonthViewFragment;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 伟平 on 2015/10/20.
 */

// Todo optimize this

public class MonthViewFragmentAdapter extends FragmentStatePagerAdapter {

    public List<MonthViewFragment> list;

    private int monthNumber;

    private int startYear;
    private int startMonth;
    private int endYear;
    private int endMonth;

    public MonthViewFragmentAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);

        startYear = RecordManager.RECORDS.get(0).getCalendar().get(Calendar.YEAR);
        endYear = RecordManager.RECORDS.
                get(RecordManager.RECORDS.size() - 1).getCalendar().get(Calendar.YEAR);
        startMonth = RecordManager.RECORDS.get(0).getCalendar().get(Calendar.MONTH);
        endMonth = RecordManager.RECORDS.
                get(RecordManager.RECORDS.size() - 1).getCalendar().get(Calendar.MONTH);
        monthNumber = (endYear - startYear) * 12 + endMonth - startMonth + 1;

        list = new ArrayList<>();
        for (int i = 0; i < monthNumber; i++) {
            list.add(MonthViewFragment.newInstance(i, monthNumber));
        }
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return monthNumber;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int nowMonth = (startMonth + (monthNumber - position - 1)) % 12;
        int nowYear = startYear + (startMonth + (monthNumber - position - 1)) / 12;
        return Util.GetMonthShort(nowMonth + 1) + " " + nowYear;
    }
}
