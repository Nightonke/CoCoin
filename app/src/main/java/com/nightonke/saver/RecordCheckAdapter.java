package com.nightonke.saver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 伟平 on 2015/11/1.
 */
public class RecordCheckAdapter extends RecyclerView.Adapter<RecordCheckAdapter.viewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<Record> records;

    public RecordCheckAdapter(Context context, List<Record> list) {
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
                Utils.GetTagIcon(records.get(position).getTag()));
        holder.date.setText(records.get(position).getCalendarString());
        holder.money.setText(String.valueOf((int) records.get(position).getMoney()));
        holder.date.setTypeface(Utils.GetTypeface());
        holder.money.setTypeface(Utils.typefaceLatoLight);
        holder.money.setTextColor(
                Utils.GetTagColor(RecordManager.TAGS.get(records.get(position).getTag()).getName()));
        holder.index.setText((position + 1) + "");
        holder.index.setTypeface(Utils.typefaceLatoLight);

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