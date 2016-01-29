package com.nightonke.saver.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.nightonke.saver.R;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.util.CoCoinUtil;

import net.steamcrafted.materialiconlib.MaterialIconView;

/**
 * Created by 伟平 on 2015/11/10.
 */

public class DialogMonthSelectGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private int minYear = 2015;
    private int maxYear = 2020;

    public DialogMonthSelectGridViewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        mContext = context;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    public int getMinYear() {
        return minYear;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    @Override
    public int getCount() {
        return (maxYear - minYear + 1) * 16;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.item_month_select, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (((position - 1) % 16 == 0) || ((position - 2) % 16 == 0) || ((position - 3) % 16 == 0)) {
            holder.imageView.setVisibility(View.INVISIBLE);
        } else {
            if (position % 16 == 0) {
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .useFont(CoCoinUtil.getInstance().typefaceLatoLight)
                        .fontSize(45)
                        .endConfig()
                        .buildRoundRect("" + (position / 16 + minYear), CoCoinUtil.GetRandomColor(), CoCoinUtil.dpToPx(50));
                holder.imageView.setImageDrawable(drawable);
            } else {
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .useFont(CoCoinUtil.getInstance().typefaceLatoLight)
                        .fontSize(50)
                        .endConfig()
                        .buildRoundRect((position % 16 - 3) + "", CoCoinUtil.GetRandomColor(), CoCoinUtil.dpToPx(50));
                holder.imageView.setImageDrawable(drawable);
            }
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }
}
