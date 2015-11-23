package com.nightonke.saver.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.CircleView;
import com.balysv.materialripple.MaterialRippleLayout;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nightonke.saver.R;
import com.nightonke.saver.model.Logo;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.adapter.TodayViewFragmentAdapter;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.model.User;
import com.nightonke.saver.ui.RiseNumberTextView;
import com.nightonke.saver.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountBookTodayViewActivity extends AppCompatActivity {

    private MaterialViewPager mViewPager;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;

    private TodayViewFragmentAdapter todayModeAdapter = null;

    private Context mContext;

    private MaterialRippleLayout custom;
    private MaterialRippleLayout tags;
    private MaterialRippleLayout months;
    private MaterialRippleLayout list;
    private MaterialRippleLayout report;
    private MaterialRippleLayout sync;
    private MaterialRippleLayout settings;
    private MaterialRippleLayout help;

    private TextView userName;
    private TextView userEmail;

    private TextView title;

    private TextView monthExpenseTip;
    private RiseNumberTextView monthExpense;

    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_book_today_view);

        mContext = this;

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        userName = (TextView)findViewById(R.id.user_name);
        userEmail = (TextView)findViewById(R.id.user_email);
        userName.setTypeface(Util.typefaceLatoRegular);
        userEmail.setTypeface(Util.typefaceLatoLight);
        User user = BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
        if (user != null) {
            userName.setText(user.getUsername());
            userEmail.setText(user.getEmail());
        }

        setFonts();

        View view = mViewPager.getRootView();
        title = (TextView)view.findViewById(R.id.logo_white);
        title.setTypeface(Util.typefaceLatoLight);
        title.setText(SettingManager.getInstance().getAccountBookName());

        mViewPager.getPagerTitleStrip().setTypeface(Util.GetTypeface(), Typeface.NORMAL);

        setTitle("");

        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        custom = (MaterialRippleLayout)mDrawer.findViewById(R.id.custom_layout);
        tags = (MaterialRippleLayout)mDrawer.findViewById(R.id.tag_layout);
        months = (MaterialRippleLayout)mDrawer.findViewById(R.id.month_layout);
        list = (MaterialRippleLayout)mDrawer.findViewById(R.id.list_layout);
        report = (MaterialRippleLayout)mDrawer.findViewById(R.id.report_layout);
        sync = (MaterialRippleLayout)mDrawer.findViewById(R.id.sync_layout);
        settings = (MaterialRippleLayout)mDrawer.findViewById(R.id.settings_layout);
        help = (MaterialRippleLayout)mDrawer.findViewById(R.id.help_layout);
        monthExpenseTip = (TextView)mDrawer.findViewById(R.id.month_expense_tip);
        monthExpenseTip.setTypeface(Util.GetTypeface());
        monthExpense = (RiseNumberTextView)mDrawer.findViewById(R.id.month_expense);
        monthExpense.setTypeface(Util.typefaceLatoLight);

        if (SettingManager.getInstance().getIsMonthLimit()) {
            monthExpenseTip.setVisibility(View.VISIBLE);
            monthExpense.setText("0");
        } else {
            monthExpenseTip.setVisibility(View.INVISIBLE);
            monthExpense.setVisibility(View.INVISIBLE);
        }

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

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                monthExpense.setText("0");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                monthExpense.withNumber(
                        RecordManager.getCurrentMonthExpense()).setDuration(500).start();
            }
        };
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

        todayModeAdapter = new TodayViewFragmentAdapter(getSupportFragmentManager());
        mViewPager.getViewPager().setOffscreenPageLimit(todayModeAdapter.getCount());
        mViewPager.getViewPager().setAdapter(todayModeAdapter);
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                return HeaderDesign.fromColorAndDrawable(
                        Util.GetTagColor(page - 2),
                        Util.GetTagDrawable(-3)
                );
            }
        });

        setListeners();

        profileImage= (CircleImageView)mDrawer.findViewById(R.id.profile_image);

        loadLogo();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MaterialViewPagerHelper.unregister(this);
    }

    private void loadRangeMode() {

        Log.d("Saver", "RANGE_MODE");

        Intent intent = new Intent(mContext, AccountBookCustomViewActivity.class);
        startActivity(intent);

    }

    private void loadTagMode() {

        Log.d("Saver", "TAG_MODE");

        Intent intent = new Intent(mContext, AccountBookTagViewActivity.class);
        startActivity(intent);

    }

    private void loadMonthMode() {

        Log.d("Saver", "MONTH_MODE");

        Intent intent = new Intent(mContext, AccountBookMonthViewActivity.class);
        startActivity(intent);

    }

    private void loadListMode() {

        Log.d("Saver", "LIST_MODE");

        Intent intent = new Intent(mContext, AccountBookListViewActivity.class);
        startActivity(intent);

    }

    private void loadSettings() {

        Log.d("Saver", "SETTINGS");

        Intent intent = new Intent(mContext, AccountBookSettingActivity.class);
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (SettingManager.getInstance().getTodayViewPieShouldChange()) {
            todayModeAdapter.notifyDataSetChanged();
            SettingManager.getInstance().setTodayViewPieShouldChange(Boolean.FALSE);
        }

        if (SettingManager.getInstance().getTodayViewTitleShouldChange()) {
            title.setText(SettingManager.getInstance().getAccountBookName());
            SettingManager.getInstance().setTodayViewTitleShouldChange(false);
        }

        if (SettingManager.getInstance().getRecordIsUpdated()) {
            todayModeAdapter.notifyDataSetChanged();
            SettingManager.getInstance().setRecordIsUpdated(false);
        }

        if (SettingManager.getInstance().getTodayViewMonthExpenseShouldChange()) {
            if (SettingManager.getInstance().getIsMonthLimit()) {
                monthExpenseTip.setVisibility(View.VISIBLE);
                monthExpense.withNumber(
                        RecordManager.getCurrentMonthExpense()).setDuration(500).start();
            } else {
                monthExpenseTip.setVisibility(View.INVISIBLE);
                monthExpense.setVisibility(View.INVISIBLE);
            }
        }

        if (SettingManager.getInstance().getTodayViewLogoShouldChange()) {
            loadLogo();
            SettingManager.getInstance().setTodayViewLogoShouldChange(false);
        }

        if (SettingManager.getInstance().getTodayViewInfoShouldChange()) {
            User user = BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
            if (user != null) {
                userName.setText(user.getUsername());
                userEmail.setText(user.getEmail());
                loadLogo();
            } else {
                userName.setText("");
                userEmail.setText("");
                loadLogo();
            }
            SettingManager.getInstance().setTodayViewInfoShouldChange(false);
        }
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

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    private void setFonts() {
        userName.setTypeface(Util.typefaceLatoRegular);
        userEmail.setTypeface(Util.typefaceLatoLight);
        ((TextView)findViewById(R.id.custom_text)).setTypeface(Util.GetTypeface());
        ((TextView)findViewById(R.id.tag_text)).setTypeface(Util.GetTypeface());
        ((TextView)findViewById(R.id.month_text)).setTypeface(Util.GetTypeface());
        ((TextView)findViewById(R.id.list_text)).setTypeface(Util.GetTypeface());
        ((TextView)findViewById(R.id.report_text)).setTypeface(Util.GetTypeface());
        ((TextView)findViewById(R.id.sync_text)).setTypeface(Util.GetTypeface());
        ((TextView)findViewById(R.id.settings_text)).setTypeface(Util.GetTypeface());
        ((TextView)findViewById(R.id.help_text)).setTypeface(Util.GetTypeface());
    }

    private void setListeners() {
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRangeMode();
            }
        });
        tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTagMode();
            }
        });
        months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMonthMode();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSettings();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadListMode();
            }
        });
    }

    private void loadLogo() {
        User user = BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
        if (user != null) {
            try {
                File logoFile = new File(CoCoinApplication.getAppContext().getFilesDir() + Util.LOGO_NAME);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(logoFile));
                if (b == null) {
                    // the local logo file is missed
                    // try to get from the server
                    BmobQuery<Logo> bmobQuery = new BmobQuery();
                    bmobQuery.addWhereEqualTo("objectId", user.getLogoObjectId());
                    bmobQuery.findObjects(CoCoinApplication.getAppContext()
                            , new FindListener<Logo>() {
                        @Override
                        public void onSuccess(List<Logo> object) {
                            // there has been an old logo in the server/////////////////////////////////////////////////////////
                            Log.d("Saver", "There is an old logo");
                            String url = object.get(0).getFile().getUrl();
                            Ion.with(CoCoinApplication.getAppContext()).load(url)
                                    .write(new File(CoCoinApplication.getAppContext().getFilesDir()
                                            + Util.LOGO_NAME))
                                    .setCallback(new FutureCallback<File>() {
                                        @Override
                                        public void onCompleted(Exception e, File file) {
                                            profileImage.setImageBitmap(BitmapFactory.decodeFile(
                                                    CoCoinApplication.getAppContext().getFilesDir()
                                                            + Util.LOGO_NAME));
                                        }
                                    });
                        }
                        @Override
                        public void onError(int code, String msg) {
                            // the picture is lost
                            Log.d("Saver", "Can't find the old logo in server.");
                        }
                    });
                } else {
                    // the user logo is in the storage
                    profileImage.setImageBitmap(b);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // use the default logo
            profileImage.setImageResource(R.drawable.default_user_logo);
        }
    }
}
