package com.nightonke.saver;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class TodayViewFragment extends Fragment {

    private int position;

    private List<Record> list = new ArrayList<>();

    private Context mContext;

    private ObservableScrollView scrollView;

    public static TodayViewFragment newInstance(int position) {
        TodayViewFragment fragment = new TodayViewFragment();
        Bundle args = new Bundle();
        args.putInt("POSITION", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        position = getArguments() != null ? getArguments().getInt("POSITION") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.today_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scrollView = (ObservableScrollView)view.findViewById(R.id.scrollView);

        if (position == 0) {
            // today
            Calendar now = Calendar.getInstance();
            Calendar leftDayRange = Utils.GetDayLeftRange(now);
            Calendar rightDayRange = Utils.GetDayRightRange(now);
            for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                if (RecordManager.RECORDS.get(i).getCalendar().before(leftDayRange)) {
                    break;
                }
                list.add(RecordManager.RECORDS.get(i));
            }
        }

        if (position == 1) {
            // this week
            Calendar now = Calendar.getInstance();
            Calendar leftWeekRange = Utils.GetWeekLeftRange(now);
            Calendar rightWeekRange = Utils.GetWeekRightRange(now);
            for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                if (RecordManager.RECORDS.get(i).getCalendar().before(leftWeekRange)) {
                    break;
                }
                list.add(RecordManager.RECORDS.get(i));
            }
        }
        if (position == 2) {
            // this month
            Calendar now = Calendar.getInstance();
            Calendar leftMonthRange = Utils.GetMonthLeftRange(now);
            Calendar rightMonthRange = Utils.GetMonthRightRange(now);
            for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                if (RecordManager.RECORDS.get(i).getCalendar().before(leftMonthRange)) {
                    break;
                }
                list.add(RecordManager.RECORDS.get(i));
            }
        }
        if (position == 3) {
            // this year
            Calendar now = Calendar.getInstance();
            Calendar leftYearRange = Utils.GetYearLeftRange(now);
            Calendar rightYearRange = Utils.GetYearRightRange(now);
            for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                if (RecordManager.RECORDS.get(i).getCalendar().before(leftYearRange)) {
                    break;
                }
                list.add(RecordManager.RECORDS.get(i));
            }
        }

        MaterialViewPagerHelper.registerScrollView(getActivity(), scrollView, null);
    }

}
