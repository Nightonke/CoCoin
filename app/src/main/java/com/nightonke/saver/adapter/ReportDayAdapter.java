package com.nightonke.saver.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nightonke.saver.R;
import com.nightonke.saver.activity.CoCoinApplication;
import com.nightonke.saver.fragment.ReportViewFragment;
import com.nightonke.saver.util.CoCoinUtil;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Weiping on 2016/2/2.
 */

public class ReportDayAdapter extends BaseAdapter {

    private ArrayList<double[]> dayExpense;
    private int month;

    public ReportDayAdapter(ArrayList<double[]> dayExpense, int month) {
        this.dayExpense = dayExpense;
        this.month = month;
    }

    @Override
    public int getCount() {
        return min(dayExpense.size() - 1, ReportViewFragment.MAX_DAY_EXPENSE);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_day, null);

        TextView icon = (TextView)convertView.findViewById(R.id.month);
        TextView name = (TextView)convertView.findViewById(R.id.month_name);
        TextView expense = (TextView)convertView.findViewById(R.id.month_expense);
        TextView records = (TextView)convertView.findViewById(R.id.month_sum);

        icon.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        name.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        expense.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        records.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);

        icon.setBackgroundResource(getBackgroundResource());
        icon.setText("" + ((int)dayExpense.get(position + 1)[2]));
        name.setText(CoCoinUtil.getInstance().GetCalendarStringDayExpenseSort(CoCoinApplication.getAppContext(), (int)dayExpense.get(position + 1)[0], (int)dayExpense.get(position + 1)[1] + 1, (int)dayExpense.get(position + 1)[2]) + CoCoinUtil.getInstance().GetPurePercentString(dayExpense.get(position + 1)[4] * 100));
        expense.setText(CoCoinUtil.getInstance().GetInMoney((int) dayExpense.get(position + 1)[3]));
        records.setText(CoCoinUtil.getInstance().GetInRecords((int) dayExpense.get(position + 1)[5]));

        return convertView;
    }

    private int getBackgroundResource() {
        Random random = new Random();
        switch (random.nextInt(6)) {
            case 0: return R.drawable.bg_month_icon_small_0;
            case 1: return R.drawable.bg_month_icon_small_1;
            case 2: return R.drawable.bg_month_icon_small_2;
            case 3: return R.drawable.bg_month_icon_small_3;
            case 4: return R.drawable.bg_month_icon_small_4;
            case 5: return R.drawable.bg_month_icon_small_5;
            default:return R.drawable.bg_month_icon_small_0;
        }
    }

    private int min(int a, int b) {
        return (a < b ? a : b);
    }

}
