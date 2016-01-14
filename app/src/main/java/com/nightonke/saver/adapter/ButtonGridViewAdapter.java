package com.nightonke.saver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.nightonke.saver.R;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.util.CoCoinUtil;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

/**
 * Created by 伟平 on 2015/10/16.
 */

public class ButtonGridViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;

    public ButtonGridViewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return CoCoinUtil.BUTTONS.length;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.button_gridview_item, null);
            holder.fl = (FrameLayout)convertView.findViewById(R.id.frame_layout);
            holder.iv = (MaterialIconView)convertView.findViewById(R.id.icon);
            holder.tv = (TextView) convertView.findViewById(R.id.textview);
            holder.ml = (MaterialRippleLayout)convertView.findViewById(R.id.material_ripple_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 11) {
            holder.tv.setTypeface(CoCoinUtil.typefaceLatoHairline);
            holder.tv.setVisibility(View.INVISIBLE);
            holder.iv.setIcon(MaterialDrawableBuilder.IconValue.CHECK);
            holder.ml.setRippleAlpha(50);
        } else if (position == 9) {
            holder.iv.setIcon(MaterialDrawableBuilder.IconValue.ERASER);
            holder.tv.setTypeface(CoCoinUtil.typefaceLatoHairline);
            holder.tv.setVisibility(View.INVISIBLE);
            holder.ml.setRippleAlpha(50);
        } else {
            holder.iv.setVisibility(View.INVISIBLE);
            holder.tv.setTypeface(CoCoinUtil.typefaceLatoHairline);
            holder.tv.setText(CoCoinUtil.BUTTONS[position]);
            holder.ml.setRippleDelayClick(false);
        }

        holder.ml.setRippleDuration(300);
        boolean shouldChange
                = SettingManager.getInstance().getIsMonthLimit()
                && SettingManager.getInstance().getIsColorRemind()
                && RecordManager.getCurrentMonthExpense()
                >= SettingManager.getInstance().getMonthWarning();
        if (shouldChange) {
            holder.fl.setBackgroundColor(
                    CoCoinUtil.getAlphaColor(SettingManager.getInstance().getRemindColor()));
            holder.ml.setRippleColor(SettingManager.getInstance().getRemindColor());
            holder.iv.setColor(SettingManager.getInstance().getRemindColor());
            holder.tv.setTextColor(SettingManager.getInstance().getRemindColor());
        } else {
            holder.fl.setBackgroundColor(CoCoinUtil.getAlphaColor(CoCoinUtil.MY_BLUE));
            holder.ml.setRippleColor(CoCoinUtil.MY_BLUE);
            holder.iv.setColor(CoCoinUtil.MY_BLUE);
            holder.tv.setTextColor(CoCoinUtil.MY_BLUE);
        }


        return convertView;
    }

    private class ViewHolder {
        FrameLayout fl;
        TextView tv;
        MaterialIconView iv;
        MaterialRippleLayout ml;
    }
}
