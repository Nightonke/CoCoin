package com.nightonke.saver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 伟平 on 2015/10/16.
 */

public class TagChooseGridViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private int fragmentPosition;

    public TagChooseGridViewAdapter(Context context, int fragmentPosition) {
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.fragmentPosition = fragmentPosition;
    }

    @Override
    public int getCount() {
        return 8;
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
            convertView = this.inflater.inflate(R.layout.tag_choose_item, null);
            holder.tagName = (TextView)convertView.findViewById(R.id.tag_name);
            holder.tagImage = (ImageView)convertView.findViewById(R.id.tag_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tagName.setText(
                RecordManager.TAGS.
                        get(fragmentPosition * 8 + position + 2).getName());
        holder.tagName.setTypeface(Utils.typefaceLatoLight);
        holder.tagImage.setImageResource(
                Utils.GetTagIcon(RecordManager.TAGS.
                        get(fragmentPosition * 8 + position + 2).getName()));

        return convertView;
    }

    private class ViewHolder {
        TextView tagName;
        ImageView tagImage;
    }
}
