package com.nightonke.saver;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

/**
 * Created by 伟平 on 2015/11/10.
 */

public class DrawerTagChooseAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    public DrawerTagChooseAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return RecordManager.TAGS.size();
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
            convertView = this.inflater.inflate(R.layout.tag_choose_item, null);
            holder.tagImage = (ImageView) convertView.findViewById(R.id.tag_image);
            holder.tagName = (TextView) convertView.findViewById(R.id.tag_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tagImage.setImageResource(
                Utils.GetTagIcon(RecordManager.TAGS.get(position).getName()));
        holder.tagName.setText(RecordManager.TAGS.get(position).getName());
        holder.tagName.setTypeface(Utils.GetTypeface());

        return convertView;
    }

    private class ViewHolder {
        ImageView tagImage;
        TextView tagName;
    }
}
