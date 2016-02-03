package com.nightonke.saver.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.nightonke.saver.R;
import com.nightonke.saver.util.CoCoinUtil;

/**
 * Created by Weiping on 2016/2/2.
 */
public class HelpAboutFragment extends Fragment {

    private ObservableScrollView mScrollView;

    public static HelpAboutFragment newInstance() {
        HelpAboutFragment fragment = new HelpAboutFragment();
        return fragment;
    }

    private Activity activity;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity = (Activity)context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help_about_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mScrollView = (ObservableScrollView) view.findViewById(R.id.scrollView);
        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);

        ((TextView)view.findViewById(R.id.content_0)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_1)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_2)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_3)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_4)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_5)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_6)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_7)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_8)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_9)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_10)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_11)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_12)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_13)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_14)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_15)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_16)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_17)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_18)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_19)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_20)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_21)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_22)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_23)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_24)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_25)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_26)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_27)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_28)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_29)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_30)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_31)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_32)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_33)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);

        ((MaterialRippleLayout)view.findViewById(R.id.layout_2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Nightonke/CoCoin")));
            }
        });
        ((MaterialRippleLayout)view.findViewById(R.id.layout_3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/u012925008")));
            }
        });
        ((MaterialRippleLayout)view.findViewById(R.id.layout_4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoCoinUtil.getInstance().copyToClipboard("Nightonke@outlook.com", mContext);
                CoCoinUtil.getInstance().showToast(mContext, mContext.getResources().getString(R.string.copy_to_clipboard));
            }
        });
    }

}
