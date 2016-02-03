package com.nightonke.saver.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.nightonke.saver.R;
import com.nightonke.saver.adapter.ReportViewFragmentAdapter;
import com.nightonke.saver.fragment.CoCoinFragmentManager;
import com.nightonke.saver.fragment.ReportViewFragment;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.util.CoCoinUtil;

public class AccountBookReportViewActivity extends AppCompatActivity
        implements
        ReportViewFragment.OnTitleChangedListener {

    private MaterialViewPager mViewPager;

    private Toolbar toolbar;

    private ReportViewFragmentAdapter reportViewFragmentAdapter = null;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_account_book_report_view);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        View view = mViewPager.getRootView();
        TextView title = (TextView)view.findViewById(R.id.logo_white);
        title.setTypeface(CoCoinUtil.typefaceLatoLight);
        title.setText(SettingManager.getInstance().getAccountBookName());

        mViewPager.getPagerTitleStrip().setTypeface(CoCoinUtil.getInstance().typefaceLatoLight, Typeface.NORMAL);
        mViewPager.getPagerTitleStrip().setTextSize(45);
        mViewPager.getPagerTitleStrip().setUnderlineColor(Color.parseColor("#00000000"));
        mViewPager.getPagerTitleStrip().setIndicatorColor(Color.parseColor("#00000000"));
        mViewPager.getPagerTitleStrip().setUnderlineHeight(0);
        mViewPager.getPagerTitleStrip().setIndicatorHeight(0);

        mViewPager.getPagerTitleStrip().setOnTabReselectedListener(new PagerSlidingTabStrip.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
//                if (CoCoinFragmentManager.reportViewFragment != null)
//                    CoCoinFragmentManager.reportViewFragment.showDataDialog();
            }
        });

        setTitle("");

        toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                }
            });
        }

        reportViewFragmentAdapter = new ReportViewFragmentAdapter(getSupportFragmentManager());
        mViewPager.getViewPager().setOffscreenPageLimit(1);
        mViewPager.getViewPager().setAdapter(reportViewFragmentAdapter);
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.getPagerTitleStrip().invalidate();

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                return HeaderDesign.fromColorAndDrawable(
                        CoCoinUtil.GetTagColor(-3),
                        CoCoinUtil.GetTagDrawable(-3)
                );
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (CoCoinFragmentManager.reportViewFragment != null)
//            CoCoinFragmentManager.reportViewFragment.onDestroy();
//        CoCoinFragmentManager.reportViewFragment = null;
        MaterialViewPagerHelper.unregister(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onTitleChanged() {
        mViewPager.getPagerTitleStrip().notifyDataSetChanged();
        mViewPager.getPagerTitleStrip().invalidate();
    }
}
