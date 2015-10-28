package com.nightonke.saver;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

/**
 * Created by 伟平 on 2015/10/27.
 */
public class tagFragment extends Fragment {

    private int fragmentPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tag_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentPosition = FragmentPagerItem.getPosition(getArguments());

        MyGridView myGridView = (MyGridView)view.findViewById(R.id.gridview);

        MyTagGridViewAdapter tagAdapter =
                new MyTagGridViewAdapter(getActivity(), fragmentPosition);

        myGridView.setAdapter(tagAdapter);

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.tagName.setText(
                        RecordManager.TAGS.get(fragmentPosition * 8 + position + 2));
                MainActivity.tagImage.setImageResource(
                        Utils.GetTagIcon(
                                RecordManager.TAGS.get(fragmentPosition * 8 + position + 2)));
            }
        });

    }

}
