package com.nightonke.saver.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightonke.saver.R;
import com.nightonke.saver.adapter.TagChooseGridViewAdapter;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.ui.MyGridView;
import com.nightonke.saver.util.Util;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by 伟平 on 2015/10/27.
 */

public class EditFragment extends Fragment {

    private int fragmentPosition;
    private int tagId = -1;

    public MaterialEditText editView;

    public ImageView tagImage;
    public TextView tagName;

    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_fragment, null);
        editView = (MaterialEditText)view.findViewById(R.id.edit_view_fuck);
        tagImage = (ImageView)view.findViewById(R.id.tag_image);
        tagName = (TextView)view.findViewById(R.id.tag_name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment, container, false);
        editView = (MaterialEditText)view.findViewById(R.id.edit_view_fuck);
        tagImage = (ImageView)view.findViewById(R.id.tag_image);
        tagName = (TextView)view.findViewById(R.id.tag_name);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentPosition = FragmentPagerItem.getPosition(getArguments());
        editView = (MaterialEditText)view.findViewById(R.id.edit_view_fuck);
        tagImage = (ImageView)view.findViewById(R.id.tag_image);
        tagName = (TextView)view.findViewById(R.id.tag_name);
        editView.setTypeface(Util.typefaceLatoHairline);
        editView.setText("0");
        editView.requestFocus();
        editView.setHelperText(" ");

    }

    public interface OnTagItemSelectedListener {
        void onTagItemPicked(int position);
    }

    public void updateTags() {

    }

    public int getTagId() {
        return tagId;
    }

    public void setTag(int p) {
        tagId = RecordManager.TAGS.get(p).getId();
        tagName.setText(Util.GetTagName(RecordManager.TAGS.get(p).getId()));
        tagImage.setImageResource(Util.GetTagIcon(RecordManager.TAGS.get(p).getId()));
    }

    public String getNumberText() {
        if (editView == null) return "";
        return editView.getText().toString();
    }

    public void setNumberText(String string) {
        if (editView == null) return;
        editView.setText(string);
    }

    public String getHelpText() {
        if (editView == null) return "";
        return editView.getHelperText();
    }

    public void setHelpText(String string) {
        if (editView == null) return;
        editView.setHelperText(string);
    }

    public void editRequestFocus() {
        if (editView == null) return;
        editView.requestFocus();
    }

    public void setEditColor(boolean shouldChange) {
        if (shouldChange) {
            editView.setTextColor(SettingManager.getInstance().getRemindColor());
            editView.setPrimaryColor(SettingManager.getInstance().getRemindColor());
            editView.setHelperTextColor(SettingManager.getInstance().getRemindColor());
        } else {
            editView.setTextColor(Util.MY_BLUE);
            editView.setPrimaryColor(Util.MY_BLUE);
            editView.setHelperTextColor(Util.MY_BLUE);
        }
    }

    public void setTagName(String name) {
        tagName.setText(name);
    }

    public void setTagImage(int resource) {
        tagImage.setImageResource(resource);
    }

}
