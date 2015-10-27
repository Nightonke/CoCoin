package com.nightonke.saver;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.Map;

/**
 * Created by 伟平 on 2015/10/16.
 */
public class MyGridViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;

    public MyGridViewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return Utils.BUTTONS.length;
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
            convertView = this.inflater.inflate(R.layout.gridview_item, null);
            holder.iv = (MaterialIconView)convertView.findViewById(R.id.icon);
            holder.tv = (TextView) convertView.findViewById(R.id.textview);
            holder.ml = (MaterialRippleLayout)convertView.findViewById(R.id.material_ripple_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 11) {
            holder.tv.setTypeface(Utils.typefaceBernhardFashion);
            holder.tv.setVisibility(View.INVISIBLE);
            holder.iv.setIcon(MaterialDrawableBuilder.IconValue.CHECK);
            holder.ml.setRippleColor(Color.parseColor("#0000ff"));
            holder.ml.setRippleAlpha(50);
        } else if (position == 9) {
            holder.iv.setIcon(MaterialDrawableBuilder.IconValue.ERASER);
            holder.tv.setTypeface(Utils.typefaceBernhardFashion);
            holder.tv.setVisibility(View.INVISIBLE);
            holder.ml.setRippleColor(Color.parseColor("#0000ff"));
            holder.ml.setRippleAlpha(50);
        } else {
            holder.iv.setVisibility(View.INVISIBLE);
            holder.tv.setTypeface(Utils.typefaceBernhardFashion);
            holder.tv.setText(Utils.BUTTONS[position]);
            holder.ml.setRippleDelayClick(false);
        }

        holder.ml.setRippleDuration(300);

        return convertView;
    }

    private class ViewHolder {
        TextView tv;
        MaterialIconView iv;
        MaterialRippleLayout ml;
    }
}
