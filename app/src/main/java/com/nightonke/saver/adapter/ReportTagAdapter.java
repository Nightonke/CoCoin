package com.nightonke.saver.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightonke.saver.R;
import com.nightonke.saver.util.CoCoinUtil;

import java.util.ArrayList;

/**
 * Created by Weiping on 2016/1/30.
 */
public class ReportTagAdapter extends BaseAdapter {

    private ArrayList<double[]> tagExpense;

    public ReportTagAdapter(ArrayList<double[]> tagExpense) {
        this.tagExpense = tagExpense;
    }

    @Override
    public int getCount() {
        return tagExpense.size() - 1;
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

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_tag, null);

        ImageView icon = (ImageView)convertView.findViewById(R.id.icon);
        TextView name = (TextView)convertView.findViewById(R.id.tag_name);
        TextView expense = (TextView)convertView.findViewById(R.id.tag_expense);
        TextView records = (TextView)convertView.findViewById(R.id.tag_sum);

        name.setTypeface(CoCoinUtil.getInstance().GetTypeface());
        expense.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        records.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);

        icon.setImageDrawable(CoCoinUtil.getInstance().GetTagIconDrawable((int)tagExpense.get(position - 1)[2]));
        name.setText(CoCoinUtil.getInstance().GetTagName((int)tagExpense.get(position - 1)[2]));
        expense.setText(CoCoinUtil.getInstance().GetInMoney((int)tagExpense.get(position - 1)[0]));
        records.setText((int)tagExpense.get(position - 1)[3] + "'s");

        return null;
    }
}
