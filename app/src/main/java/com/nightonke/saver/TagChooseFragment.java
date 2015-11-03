package com.nightonke.saver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

/**
 * Created by 伟平 on 2015/10/27.
 */
public class TagChooseFragment extends Fragment {

    private int fragmentPosition;
    public MyGridView myGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tag_choose_fragment, null);
        myGridView = (MyGridView)view.findViewById(R.id.gridview);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tag_choose_fragment, container, false);
        myGridView = (MyGridView)view.findViewById(R.id.gridview);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentPosition = FragmentPagerItem.getPosition(getArguments());

        TagChooseGridViewAdapter tagAdapter =
                new TagChooseGridViewAdapter(getActivity(), fragmentPosition);

        myGridView.setAdapter(tagAdapter);

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.tagId = RecordManager.TAGS.
                        get(fragmentPosition * 8 + position + 2).getId();
                MainActivity.tagName.setText(
                        RecordManager.TAGS.
                                get(fragmentPosition * 8 + position + 2).getName());
                MainActivity.tagImage.setImageResource(
                        Utils.GetTagIcon(
                                RecordManager.TAGS.
                                        get(fragmentPosition * 8 + position + 2).getName()));
            }
        });

    }

}
