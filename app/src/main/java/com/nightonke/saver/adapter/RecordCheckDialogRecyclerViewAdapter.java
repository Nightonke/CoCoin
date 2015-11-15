package com.nightonke.saver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightonke.saver.R;
import com.nightonke.saver.model.Record;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 伟平 on 2015/11/1.
 */
public class RecordCheckDialogRecyclerViewAdapter extends RecyclerView.Adapter<RecordCheckDialogRecyclerViewAdapter.viewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<Record> records;

    public RecordCheckDialogRecyclerViewAdapter(Context context, List<Record> list) {
        records = new ArrayList<>();
        records = list;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(mLayoutInflater.inflate(R.layout.record_check_item, parent, false));
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.imageView.setImageResource(
                Util.GetTagIcon(records.get(position).getTag()));
        holder.date.setText(records.get(position).getCalendarString());
        holder.money.setText(String.valueOf((int) records.get(position).getMoney()));
        holder.date.setTypeface(Util.GetTypeface());
        holder.money.setTypeface(Util.typefaceLatoLight);
        holder.money.setTextColor(
                Util.GetTagColorResource(RecordManager.TAGS.get(records.get(position).getTag()).getId()));
        holder.index.setText((position + 1) + "");
        holder.index.setTypeface(Util.typefaceLatoLight);

    }

    @Override
    public int getItemCount() {
        if (records == null) {
            return 0;
        }
        return records.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.image_view)
        ImageView imageView;
        @InjectView(R.id.date)
        TextView date;
        @InjectView(R.id.money)
        TextView money;
        @InjectView(R.id.index)
        TextView index;

        viewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}