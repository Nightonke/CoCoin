package com.nightonke.saver.fragment;

import com.nightonke.saver.ui.CustomTitleSliderView;

import java.util.ArrayList;

/**
 * Created by Weiping on 2016/1/19.
 */
public class CoCoinFragmentManager {

    public static int MAIN_ACTIVITY_FRAGMENT = 0;
    public static EditMoneyFragment mainActivityEditMoneyFragment = null;
    public static EditRemarkFragment mainActivityEditRemarkFragment = null;

    public static int EDIT_RECORD_ACTIVITY_FRAGMENT = 1;
    public static EditMoneyFragment editRecordActivityEditMoneyFragment = null;
    public static EditRemarkFragment editRecordActivityEditRemarkFragment = null;

    public static PasswordChangeFragment passwordChangeFragment[] = new PasswordChangeFragment[3];

    public static ArrayList<TagChooseFragment> tagChooseFragments = new ArrayList<>();

    public static final int NUMBER_SLIDER = 0;
    public static final int EXPENSE_SLIDER = 1;
    public static CustomTitleSliderView numberCustomTitleSliderView = null;
    public static CustomTitleSliderView expenseCustomTitleSliderView = null;

    public static ReportViewFragment reportViewFragment = null;

































    private static CoCoinFragmentManager ourInstance = new CoCoinFragmentManager();

    public static CoCoinFragmentManager getInstance() {
        return ourInstance;
    }

    private CoCoinFragmentManager() {
    }
}
