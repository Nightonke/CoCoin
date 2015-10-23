package com.nightonke.saver;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.Calendar;

public class AccountBook extends AppCompatActivity {

    private final int TAG_MODE = 0;
    private final int YEAR_MODE = 1;
    private final int MONTH_MODE = 2;

    private int MODE = TAG_MODE;

    private MaterialViewPager mViewPager;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private MyFragmentAdapter myAdapter;

    private Context mContext;

    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_account_book);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

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

//        loadMode();

        myAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mViewPager.getViewPager().setOffscreenPageLimit(myAdapter.getCount());
        mViewPager.getViewPager().setAdapter(myAdapter);
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                return HeaderDesign.fromColorResAndDrawable(
                        Utils.GetTagColor(RecordManager.TAGS.get(page)),
                        Utils.GetTagDrawable(RecordManager.TAGS.get(page), mContext));
            }
        });

        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                }
            });
        }

        Button button = (Button)mDrawer.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMode(TAG_MODE);
            }
        });

    }

    private void loadMode(int mode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Saver", "Start run");
                loadTagMode();
                Log.d("Saver", "Finish run");
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private Handler handler = new Handler() {
      public void handleMessage(Message msg) {
          if (msg.what == 1) {
              Log.d("Saver", "Start UI");
              myAdapter.notifyDataSetChanged();
              Log.d("Saver", "Finish UI");
          }
      }
    };

    private void loadTagMode() {

        Log.d("Saver", "TAGMODE");

        myAdapter = new MyFragmentAdapter(getSupportFragmentManager());

//        mViewPager.getViewPager().setAdapter(myAdapter);
//
//        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
//            @Override
//            public HeaderDesign getHeaderDesign(int page) {
//                return HeaderDesign.fromColorResAndDrawable(
//                        Utils.GetTagColor(RecordManager.TAGS.get(page)),
//                        Utils.GetTagDrawable(RecordManager.TAGS.get(page), mContext));
//            }
//        });
//
//        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
//        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
//
//        mViewPager.getViewPager().getCurrentItem();
//
//        View logo = findViewById(R.id.logo_white);
//        if (logo != null) {
//            logo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mViewPager.notifyHeaderChanged();
//                }
//            });
//        }
    }

}
