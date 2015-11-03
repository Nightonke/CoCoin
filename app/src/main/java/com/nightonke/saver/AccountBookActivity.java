package com.nightonke.saver;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class AccountBookActivity extends AppCompatActivity {

    private final int TAG_MODE = 0;
    private final int TODAY_MODE = 1;
    private final int MONTH_MODE = 2;

    private int MODE = -1;

    private TextView headerLogo;

    private MaterialViewPager mViewPager;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;

    private TagViewFragmentAdapter tagModeAdapter;
    private TodayViewFragmentAdapter todayModeAdapter;

    private Context mContext;

    private RefWatcher refWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_account_book);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        View view = mViewPager.getRootView();
        TextView title = (TextView)view.findViewById(R.id.logo_white);
        title.setTypeface(Utils.typefaceLatoLight);

        mViewPager.getPagerTitleStrip().setTypeface(Utils.typefaceLatoLight, Typeface.NORMAL);

        setTitle("");

        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);


        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                }
            });
        }

        Button button1 = (Button)mDrawer.findViewById(R.id.loadTagModeButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTagMode();
            }
        });

        Button button2 = (Button)mDrawer.findViewById(R.id.loadTodayModeButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTodayMode();
            }
        });

        loadTodayMode();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
    }

    private void loadTagMode() {

        Log.d("Saver", "TAGMODE");

        if (MODE == TAG_MODE) {
            return;
        }
        MODE = TAG_MODE;

        tagModeAdapter = new TagViewFragmentAdapter(getSupportFragmentManager());
        mViewPager.getViewPager().setOffscreenPageLimit(tagModeAdapter.getCount());
        mViewPager.getViewPager().setAdapter(tagModeAdapter);
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                return HeaderDesign.fromColorResAndDrawable(
                        Utils.GetTagColor(RecordManager.TAGS.get(page).getName()),
                        mContext.getResources().getDrawable(
                                Utils.GetTagDrawable(RecordManager.TAGS.get(page).getName())));
            }
        });
    }

    private void loadTodayMode() {

        Log.d("Saver", "TODAYMODE");

        if (MODE == TODAY_MODE) {
            return;
        }
        MODE = TODAY_MODE;

        todayModeAdapter = new TodayViewFragmentAdapter(getSupportFragmentManager());
        mViewPager.getViewPager().setOffscreenPageLimit(todayModeAdapter.getCount());
        mViewPager.getViewPager().setAdapter(todayModeAdapter);
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                return HeaderDesign.fromColorResAndDrawable(
                        Utils.GetTagColor(RecordManager.TAGS.get(page).getName()),
                        mContext.getResources().getDrawable(
                                Utils.GetTagDrawable(RecordManager.TAGS.get(page).getName())));
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

}
