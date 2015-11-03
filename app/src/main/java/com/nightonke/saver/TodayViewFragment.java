package com.nightonke.saver;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

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

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    static final int TODAY = 0;
    static final int YESTERDAY = 1;
    static final int THIS_WEEK = 2;
    static final int LAST_WEEK = 3;
    static final int THIS_MONTH = 4;
    static final int LAST_MONTH = 5;
    static final int THIS_YEAR = 6;
    static final int LAST_YEAR = 7;

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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        Calendar now = Calendar.getInstance();
        Calendar leftRange = Calendar.getInstance();
        Calendar rightRange = Calendar.getInstance();

        switch (position) {
            case TODAY:
                leftRange = Utils.GetTodayLeftRange(now);
                rightRange = Utils.GetTodayRightRange(now);
                for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (RecordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        break;
                    }
                    list.add(RecordManager.RECORDS.get(i));
                }
                break;
            case YESTERDAY:
                leftRange = Utils.GetYesterdayLeftRange(now);
                rightRange = Utils.GetYesterdayRightRange(now);
                for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (RecordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        break;
                    } else if (!RecordManager.RECORDS.get(i).getCalendar().after(rightRange)) {
                        list.add(RecordManager.RECORDS.get(i));
                    }
                }
                break;
            case THIS_WEEK:
                leftRange = Utils.GetThisWeekLeftRange(now);
                rightRange = Utils.GetThisWeekRightRange(now);
                for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (RecordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        break;
                    }
                    list.add(RecordManager.RECORDS.get(i));
                }
                break;
            case LAST_WEEK:
                leftRange = Utils.GetLastWeekLeftRange(now);
                rightRange = Utils.GetLastWeekRightRange(now);
                for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (RecordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        break;
                    } else if (RecordManager.RECORDS.get(i).getCalendar().before(rightRange)) {
                        list.add(RecordManager.RECORDS.get(i));
                    }
                }
                break;
            case THIS_MONTH:
                leftRange = Utils.GetThisMonthLeftRange(now);
                rightRange = Utils.GetThisMonthRightRange(now);
                for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (RecordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        break;
                    }
                    list.add(RecordManager.RECORDS.get(i));
                }
                break;
            case LAST_MONTH:
                leftRange = Utils.GetLastMonthLeftRange(now);
                rightRange = Utils.GetLastMonthRightRange(now);
                for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (RecordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        break;
                    } else if (RecordManager.RECORDS.get(i).getCalendar().before(rightRange)) {
                        list.add(RecordManager.RECORDS.get(i));
                    }
                }
                break;
            case THIS_YEAR:
                leftRange = Utils.GetThisYearLeftRange(now);
                rightRange = Utils.GetThisYearRightRange(now);
                for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (RecordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        break;
                    }
                    list.add(RecordManager.RECORDS.get(i));
                }
                break;
            case LAST_YEAR:
                leftRange = Utils.GetLastYearLeftRange(now);
                rightRange = Utils.GetLastYearRightRange(now);
                for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                    if (RecordManager.RECORDS.get(i).getCalendar().before(leftRange)) {
                        break;
                    } else if (RecordManager.RECORDS.get(i).getCalendar().before(rightRange)) {
                        list.add(RecordManager.RECORDS.get(i));
                    }
                }
                break;
        }

        mAdapter = new RecyclerViewMaterialAdapter(new TodayViewRecyclerViewAdapter(list, mContext, position));
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

}
