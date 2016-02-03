package com.nightonke.saver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nightonke.saver.R;
import com.nightonke.saver.activity.CoCoinApplication;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.util.CoCoinUtil;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class DrawerMonthViewRecyclerViewAdapter
        extends RecyclerView.Adapter<DrawerMonthViewRecyclerViewAdapter.viewHolder> {

    private ArrayList<Double> expenses;
    private ArrayList<Integer> records;
    private ArrayList<Integer> months;
    private ArrayList<Integer> years;

    private Context mContext;

    OnItemClickListener onItemClickListener;

    public DrawerMonthViewRecyclerViewAdapter(Context context) {
        mContext = context;
        expenses = new ArrayList<>();
        records = new ArrayList<>();
        months = new ArrayList<>();
        years = new ArrayList<>();

        if (RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.size() != 0) {

            int currentYear = RecordManager.RECORDS.
                    get(RecordManager.RECORDS.size() - 1).getCalendar().get(Calendar.YEAR);
            int currentMonth = RecordManager.RECORDS.
                    get(RecordManager.RECORDS.size() - 1).getCalendar().get(Calendar.MONTH);
            double currentMonthSum = 0;
            int currentMonthRecord = 0;

            for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                if (RecordManager.RECORDS.get(i).getCalendar().get(Calendar.YEAR) == currentYear
                        && RecordManager.RECORDS.get(i).
                        getCalendar().get(Calendar.MONTH) == currentMonth) {
                    currentMonthSum += RecordManager.RECORDS.get(i).getMoney();
                    currentMonthRecord++;
                } else {
                    expenses.add(currentMonthSum);
                    records.add(currentMonthRecord);
                    years.add(currentYear);
                    months.add(currentMonth);
                    currentMonthSum = RecordManager.RECORDS.get(i).getMoney();
                    currentMonthRecord = 1;
                    currentYear = RecordManager.RECORDS.get(i).getCalendar().get(Calendar.YEAR);
                    currentMonth = RecordManager.RECORDS.get(i).getCalendar().get(Calendar.MONTH);
                }
            }
            expenses.add(currentMonthSum);
            records.add(currentMonthRecord);
            years.add(currentYear);
            months.add(currentMonth);

        }


    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    @Override
    public DrawerMonthViewRecyclerViewAdapter.viewHolder
        onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_month_view_drawer, parent, false);
        return new viewHolder(view) {
        };

    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        holder.month.setText(CoCoinUtil.GetMonthShort(months.get(position) + 1));
        holder.month.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);

        holder.year.setText(years.get(position) + "");
        holder.year.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);

        holder.sum.setText(CoCoinUtil.getInstance().GetInRecords(records.get(position)));
        holder.sum.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);

        holder.money.setText(CoCoinUtil.getInstance().GetInMoney((int) (double) (expenses.get(position))));
        holder.money.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
    }

    public class viewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        @Optional
        @InjectView(R.id.month)
        TextView month;
        @Optional
        @InjectView(R.id.year)
        TextView year;
        @Optional
        @InjectView(R.id.money)
        TextView money;
        @Optional
        @InjectView(R.id.sum)
        TextView sum;

        viewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.onItemClickListener = mItemClickListener;
    }

}
