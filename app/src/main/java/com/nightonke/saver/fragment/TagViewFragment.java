package com.nightonke.saver.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.nightonke.saver.R;
import com.nightonke.saver.activity.CoCoinApplication;
import com.nightonke.saver.adapter.TagViewRecyclerViewAdapter;
import com.nightonke.saver.model.CoCoinRecord;
import com.nightonke.saver.model.RecordManager;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class TagViewFragment extends Fragment {

    private int position;

    private List<CoCoinRecord> list = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private Context mContext;

    public static TagViewFragment newInstance(int position) {
        TagViewFragment fragment = new TagViewFragment();
        Bundle args = new Bundle();
        args.putInt("POSITION", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        position = getArguments() != null ? getArguments().getInt("POSITION") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_book_fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (position == 0) {
            for (CoCoinRecord coCoinRecord : RecordManager.RECORDS) {
                list.add(coCoinRecord);
            }
        }
        if (position == 1) {
            for (CoCoinRecord coCoinRecord : RecordManager.RECORDS) {
                list.add(coCoinRecord);
            }
        } else {
            for (CoCoinRecord coCoinRecord : RecordManager.RECORDS) {
                if (coCoinRecord.getTag() == RecordManager.TAGS.get(position).getId()) {
                    list.add(coCoinRecord);
                }
            }
        }

        mAdapter = new RecyclerViewMaterialAdapter(new TagViewRecyclerViewAdapter(list, mContext, position));
        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = CoCoinApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

}
