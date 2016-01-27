package com.nightonke.saver.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.github.johnpersano.supertoasts.SuperToast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nightonke.saver.BuildConfig;
import com.nightonke.saver.R;
import com.nightonke.saver.adapter.TodayViewFragmentAdapter;
import com.nightonke.saver.model.CoCoin;
import com.nightonke.saver.model.CoCoinRecord;
import com.nightonke.saver.model.Logo;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.model.TaskManager;
import com.nightonke.saver.model.UploadInfo;
import com.nightonke.saver.model.User;
import com.nightonke.saver.ui.CustomSliderView;
import com.nightonke.saver.ui.MyQuery;
import com.nightonke.saver.ui.RiseNumberTextView;
import com.nightonke.saver.util.CoCoinUtil;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
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

    private MaterialIconView syncIcon;

    private TextView userName;
    private TextView userEmail;

    private TextView title;

    private TextView monthExpenseTip;
    private RiseNumberTextView monthExpense;

    private CircleImageView profileImage;
    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_book_today_view);
        SuperToast.cancelAllSuperToasts();

        mContext = this;

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        userName = (TextView)findViewById(R.id.user_name);
        userEmail = (TextView)findViewById(R.id.user_email);
        userName.setTypeface(CoCoinUtil.typefaceLatoRegular);
        userEmail.setTypeface(CoCoinUtil.typefaceLatoLight);
        User user = BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
        if (user != null) {
            userName.setText(user.getUsername());
            userEmail.setText(user.getEmail());
        }

        setFonts();

        View view = mViewPager.getRootView();
        title = (TextView)view.findViewById(R.id.logo_white);
        title.setTypeface(CoCoinUtil.typefaceLatoLight);
        title.setText(SettingManager.getInstance().getAccountBookName());

        mViewPager.getPagerTitleStrip().setTypeface(CoCoinUtil.GetTypeface(), Typeface.NORMAL);

        setTitle("");

        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        custom = (MaterialRippleLayout)mDrawer.findViewById(R.id.custom_layout);
        tags = (MaterialRippleLayout)mDrawer.findViewById(R.id.tag_layout);
        months = (MaterialRippleLayout)mDrawer.findViewById(R.id.month_layout);
        list = (MaterialRippleLayout)mDrawer.findViewById(R.id.list_layout);
        report = (MaterialRippleLayout)mDrawer.findViewById(R.id.report_layout);
        report.setVisibility(View.GONE);
        sync = (MaterialRippleLayout)mDrawer.findViewById(R.id.sync_layout);
        settings = (MaterialRippleLayout)mDrawer.findViewById(R.id.settings_layout);
        help = (MaterialRippleLayout)mDrawer.findViewById(R.id.help_layout);
        syncIcon = (MaterialIconView)mDrawer.findViewById(R.id.sync_icon);
        setIconEnable(syncIcon, SettingManager.getInstance().getLoggenOn());
        monthExpenseTip = (TextView)mDrawer.findViewById(R.id.month_expense_tip);
        monthExpenseTip.setTypeface(CoCoinUtil.GetTypeface());
        monthExpense = (RiseNumberTextView)mDrawer.findViewById(R.id.month_expense);
        monthExpense.setTypeface(CoCoinUtil.typefaceLatoLight);

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
                        CoCoinUtil.GetTagColor(page - 2),
                        CoCoinUtil.GetTagDrawable(-3)
                );
            }
        });

        setListeners();

        profileImage= (CircleImageView)mDrawer.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SettingManager.getInstance().getLoggenOn()) {
                    CoCoinUtil.showToast(mContext, R.string.change_logo_tip);
                } else {
                    CoCoinUtil.showToast(mContext, R.string.login_tip);
                }
            }
        });

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String, Integer> urls = CoCoinUtil.GetDrawerTopUrl();

        for(String name : urls.keySet()){
            CustomSliderView customSliderView = new CustomSliderView(this);
            // initialize a SliderLayout
            customSliderView
                    .image(urls.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider.addSlider(customSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

        loadLogo();

    }

    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
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

    private int syncSuccessNumber = 0;
    private int syncFailedNumber = 0;
    private int cloudRecordNumber = 0;
    MaterialDialog syncQueryDialog;
    MaterialDialog syncChooseDialog;
    MaterialDialog syncProgressDialog;
    private void sync() {
        if (!SettingManager.getInstance().getLoggenOn()) {
            CoCoinUtil.showToast(mContext, R.string.login_tip);
        } else {
            syncSuccessNumber = 0;
            syncFailedNumber = 0;
            syncQueryDialog = new MaterialDialog.Builder(this)
                    .title(R.string.sync_querying_title)
                    .content(R.string.sync_querying_content)
                    .negativeText(R.string.cancel)
                    .progress(true, 0)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (which == DialogAction.NEGATIVE) {

                            }
                        }
                    })
                    .show();
            final User user = BmobUser
                    .getCurrentUser(CoCoinApplication.getAppContext(), User.class);
            final MyQuery myQuery = new MyQuery();
            myQuery.setTask(++TaskManager.QUERY_UPDATE_TASK);
            myQuery.query = new BmobQuery<>();
            myQuery.query.addWhereEqualTo("userId", user.getObjectId());
            myQuery.query.setLimit(1);
            myQuery.query.findObjects(this, new FindListener<UploadInfo>() {
                @Override
                public void onSuccess(List<UploadInfo> object) {
                    if (myQuery.getTask() != TaskManager.QUERY_UPDATE_TASK) return;
                    else {
                        syncQueryDialog.dismiss();
                        cloudRecordNumber = 0;
                        Calendar cal = null;
                        if (object.size() == 0) {

                        } else {
                            cloudRecordNumber = object.get(0).getRecordNumber();
                            BmobDate d = object.get(0).getTime();
                            cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                cal.setTime(sdf.parse(d.getDate()));
                            } catch (ParseException p) {

                            }
                        }
                        String content
                                = CoCoinUtil.GetString(mContext, R.string.sync_info_cloud_record_0)
                                + cloudRecordNumber
                                + CoCoinUtil.GetString(mContext, R.string.sync_info_cloud_record_1)
                                + (cal == null ? CoCoinUtil.GetString(mContext, R.string.sync_info_cloud_time_2) : CoCoinUtil.GetString(mContext, R.string.sync_info_cloud_time_0) + CoCoinUtil.GetCalendarString(mContext, cal) + CoCoinUtil.GetString(mContext, R.string.sync_info_cloud_time_1))
                                + CoCoinUtil.GetString(mContext, R.string.sync_info_mobile_record_0)
                                + RecordManager.getInstance(mContext).RECORDS.size()
                                + CoCoinUtil.GetString(mContext, R.string.sync_info_mobile_record_1)
                                + (SettingManager.getInstance().getRecentlySyncTime() == null ? CoCoinUtil.GetString(mContext, R.string.sync_info_mobile_time_2) : CoCoinUtil.GetString(mContext, R.string.sync_info_mobile_time_0) + CoCoinUtil.GetCalendarString(mContext, SettingManager.getInstance().getRecentlySyncTime()) + CoCoinUtil.GetString(mContext, R.string.sync_info_mobile_time_1))
                                + CoCoinUtil.GetString(mContext, R.string.sync_choose_content);
                        syncChooseDialog = new MaterialDialog.Builder(mContext)
                                .title(R.string.sync_choose_title)
                                .content(content)
                                .positiveText(R.string.sync_to_cloud)
                                .negativeText(R.string.sync_to_mobile)
                                .neutralText(R.string.cancel)
                                .onAny(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        syncChooseDialog.dismiss();
                                        if (which == DialogAction.POSITIVE) {
                                            // sync to cloud
                                            String subContent = "";
                                            if (RecordManager.getInstance(mContext).RECORDS.size() == 0) {
                                                subContent = CoCoinUtil.GetString(mContext, R.string.mobile_record_empty);
                                                new MaterialDialog.Builder(mContext)
                                                        .title(R.string.sync)
                                                        .content(subContent)
                                                        .positiveText(R.string.ok_1)
                                                        .show();
                                                return;
                                            } else {
                                                subContent
                                                        = CoCoinUtil.GetString(mContext, R.string.sure_to_cloud_0)
                                                        + RecordManager.getInstance(mContext).RECORDS.size()
                                                        + CoCoinUtil.GetString(mContext, R.string.sure_to_cloud_1);
                                            }
                                            new MaterialDialog.Builder(mContext)
                                                    .title(R.string.sync)
                                                    .content(subContent)
                                                    .positiveText(R.string.ok_1)
                                                    .negativeText(R.string.cancel)
                                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            if (which == DialogAction.POSITIVE) {
                                                                syncProgressDialog = new MaterialDialog.Builder(mContext)
                                                                        .title(R.string.syncing)
                                                                        .content(CoCoinUtil.GetString(mContext, R.string.uploading_0) + "1" + CoCoinUtil.GetString(mContext, R.string.uploading_1))
                                                                        .progress(false, RecordManager.getInstance(mContext).RECORDS.size(), true)
                                                                        .cancelable(false)
                                                                        .show();
                                                                BmobQuery<CoCoinRecord> query = new BmobQuery<>();
                                                                query.addWhereEqualTo("userId", user.getObjectId());
                                                                query.setLimit(Integer.MAX_VALUE);
                                                                query.findObjects(mContext, new FindListener<CoCoinRecord>() {
                                                                    @Override
                                                                    public void onSuccess(List<CoCoinRecord> object) {
                                                                        if (BuildConfig.DEBUG) Log.d("CoCoin", "Query " + object.size() + " records");
                                                                        for (CoCoinRecord record : object) {
                                                                            record.delete(mContext, new DeleteListener() {
                                                                                @Override
                                                                                public void onSuccess() {}

                                                                                @Override
                                                                                public void onFailure(int code, String msg) {}
                                                                            });
                                                                        }
                                                                        for (CoCoinRecord record : RecordManager.getInstance(mContext).RECORDS) {
                                                                            record.setUserId(user.getObjectId());
                                                                            record.save(mContext, new SaveListener() {
                                                                                @Override
                                                                                public void onSuccess() {
                                                                                    syncSuccessNumber++;
                                                                                    syncProgressDialog.incrementProgress(1);
                                                                                    if (syncSuccessNumber == RecordManager.getInstance(mContext).RECORDS.size()) {
                                                                                        syncProgressDialog.setContent(R.string.sync_completely_content);
                                                                                    } else {
                                                                                        syncProgressDialog.setContent(CoCoinUtil.GetString(mContext, R.string.uploading_0) + (syncSuccessNumber + 1) + CoCoinUtil.GetString(mContext, R.string.uploading_1));
                                                                                    }
                                                                                    if (syncSuccessNumber + syncFailedNumber == RecordManager.getInstance(mContext).RECORDS.size()) {
                                                                                        syncProgressDialog.dismiss();
                                                                                        new MaterialDialog.Builder(mContext)
                                                                                                .title(R.string.sync_completely_title)
                                                                                                .content(syncSuccessNumber + CoCoinUtil.GetString(mContext, R.string.uploading_fail_1))
                                                                                                .positiveText(R.string.ok_1)
                                                                                                .show();
                                                                                    }
                                                                                }
                                                                                @Override
                                                                                public void onFailure(int code, String arg0) {
                                                                                    syncFailedNumber++;
                                                                                    syncProgressDialog.incrementProgress(1);
                                                                                    if (syncSuccessNumber + syncFailedNumber == RecordManager.getInstance(mContext).RECORDS.size()) {
                                                                                        syncProgressDialog.dismiss();
                                                                                        new MaterialDialog.Builder(mContext)
                                                                                                .title(R.string.sync_completely_title)
                                                                                                .content(syncSuccessNumber + CoCoinUtil.GetString(mContext, R.string.uploading_fail_1))
                                                                                                .positiveText(R.string.ok_1)
                                                                                                .show();
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                    @Override
                                                                    public void onError(int code, String msg) {
                                                                        syncProgressDialog.dismiss();
                                                                        new MaterialDialog.Builder(mContext)
                                                                                .title(R.string.sync_failed)
                                                                                .content(CoCoinUtil.GetString(mContext, R.string.uploading_fail_0) + syncSuccessNumber + CoCoinUtil.GetString(mContext, R.string.uploading_fail_1))
                                                                                .positiveText(R.string.ok_1)
                                                                                .show();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    })
                                                    .show();
                                        } else if (which == DialogAction.NEGATIVE) {
                                            // sync to mobile
                                            String subContent = "";
                                            if (cloudRecordNumber == 0) {
                                                subContent = CoCoinUtil.GetString(mContext, R.string.cloud_record_empty);
                                                new MaterialDialog.Builder(mContext)
                                                        .title(R.string.sync)
                                                        .content(subContent)
                                                        .positiveText(R.string.ok_1)
                                                        .show();
                                                return;
                                            } else {
                                                subContent
                                                        = CoCoinUtil.GetString(mContext, R.string.sure_to_mobile_0)
                                                        + cloudRecordNumber
                                                        + CoCoinUtil.GetString(mContext, R.string.sure_to_mobile_1);
                                            }
                                            new MaterialDialog.Builder(mContext)
                                                    .title(R.string.sync)
                                                    .content(subContent)
                                                    .positiveText(R.string.ok_1)
                                                    .negativeText(R.string.cancel)
                                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            if (which == DialogAction.POSITIVE) {
                                                                syncProgressDialog = new MaterialDialog.Builder(mContext)
                                                                        .title(R.string.syncing)
                                                                        .content(CoCoinUtil.GetString(mContext, R.string.downloading_0) + "1" + CoCoinUtil.GetString(mContext, R.string.downloading_1))
                                                                        .progress(false, cloudRecordNumber, true)
                                                                        .cancelable(false)
                                                                        .show();
                                                                for (CoCoinRecord record : RecordManager.getInstance(mContext).RECORDS) {
                                                                    RecordManager.deleteRecord(record, true);
                                                                }
                                                                BmobQuery<CoCoinRecord> query = new BmobQuery<>();
                                                                query.addWhereEqualTo("userId", user.getObjectId());
                                                                query.setLimit(Integer.MAX_VALUE);
                                                                query.findObjects(mContext, new FindListener<CoCoinRecord>() {
                                                                    @Override
                                                                    public void onSuccess(List<CoCoinRecord> object) {
                                                                        Collections.sort(object, new Comparator<CoCoinRecord>() {
                                                                            @Override
                                                                            public int compare(CoCoinRecord lhs, CoCoinRecord rhs) {
                                                                                if (lhs.getId() < rhs.getId()) return -1;
                                                                                else if (lhs.getId() > rhs.getId()) return 1;
                                                                                else return 0;
                                                                            }
                                                                        });
                                                                        if (BuildConfig.DEBUG) Log.d("CoCoin", "Query " + object.size() + " records");
                                                                        for (CoCoinRecord record : object) {
                                                                            if (RecordManager.saveRecord(record) == -1) {
                                                                                syncFailedNumber++;
                                                                                syncProgressDialog.incrementProgress(1);
                                                                                if (syncSuccessNumber + syncFailedNumber == object.size()) {
                                                                                    syncProgressDialog.dismiss();
                                                                                    new MaterialDialog.Builder(mContext)
                                                                                            .title(R.string.sync_completely_title)
                                                                                            .content(syncSuccessNumber + CoCoinUtil.GetString(mContext, R.string.uploading_fail_1))
                                                                                            .positiveText(R.string.ok_1)
                                                                                            .show();
                                                                                }
                                                                            } else {
                                                                                syncSuccessNumber++;
                                                                                if (syncSuccessNumber == object.size()) {
                                                                                    syncProgressDialog.setContent(R.string.sync_completely_content);
                                                                                } else {
                                                                                    syncProgressDialog.setContent(CoCoinUtil.GetString(mContext, R.string.uploading_0) + (syncSuccessNumber + 1) + CoCoinUtil.GetString(mContext, R.string.uploading_1));
                                                                                }
                                                                                if (syncSuccessNumber + syncFailedNumber == object.size()) {
                                                                                    syncProgressDialog.dismiss();
                                                                                    new MaterialDialog.Builder(mContext)
                                                                                            .title(R.string.sync_completely_title)
                                                                                            .content(syncSuccessNumber + CoCoinUtil.GetString(mContext, R.string.uploading_fail_1))
                                                                                            .positiveText(R.string.ok_1)
                                                                                            .show();
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    @Override
                                                                    public void onError(int code, String msg) {
                                                                        syncProgressDialog.dismiss();
                                                                        new MaterialDialog.Builder(mContext)
                                                                                .title(R.string.sync_failed)
                                                                                .content(CoCoinUtil.GetString(mContext, R.string.downloading_fail_0) + syncSuccessNumber + CoCoinUtil.GetString(mContext, R.string.downloading_fail_1))
                                                                                .positiveText(R.string.ok_1)
                                                                                .show();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                        }
                                    }
                                })
                                .show();
                    }
                }
                @Override
                public void onError(int code, String msg) {
                    syncQueryDialog.dismiss();
                    if (BuildConfig.DEBUG) Log.d("CoCoin", "Query: " + msg);
                    if (syncQueryDialog != null) syncQueryDialog.dismiss();
                    new MaterialDialog.Builder(mContext)
                            .title(R.string.sync_querying_fail_title)
                            .content(R.string.sync_querying_fail_content)
                            .positiveText(R.string.ok_1)
                            .show();
                }
            });
        }
    }

    private void loadSettings() {

        Log.d("Saver", "SETTINGS");

        Intent intent = new Intent(mContext, AccountBookSettingActivity.class);
        startActivity(intent);

    }

    @Override
    public void onResume() {

        if (mDemoSlider != null) mDemoSlider.startAutoCycle();

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
            setIconEnable(syncIcon, SettingManager.getInstance().getLoggenOn());
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
        userName.setTypeface(CoCoinUtil.typefaceLatoRegular);
        userEmail.setTypeface(CoCoinUtil.typefaceLatoLight);
        ((TextView)findViewById(R.id.custom_text)).setTypeface(CoCoinUtil.GetTypeface());
        ((TextView)findViewById(R.id.tag_text)).setTypeface(CoCoinUtil.GetTypeface());
        ((TextView)findViewById(R.id.month_text)).setTypeface(CoCoinUtil.GetTypeface());
        ((TextView)findViewById(R.id.list_text)).setTypeface(CoCoinUtil.GetTypeface());
        ((TextView)findViewById(R.id.report_text)).setTypeface(CoCoinUtil.GetTypeface());
        ((TextView)findViewById(R.id.sync_text)).setTypeface(CoCoinUtil.GetTypeface());
        ((TextView)findViewById(R.id.settings_text)).setTypeface(CoCoinUtil.GetTypeface());
        ((TextView)findViewById(R.id.help_text)).setTypeface(CoCoinUtil.GetTypeface());
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
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sync();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, HelpActivity.class));
            }
        });
    }

    private void loadLogo() {
        User user = BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
        if (user != null) {
            try {
                File logoFile = new File(CoCoinApplication.getAppContext().getFilesDir() + CoCoinUtil.LOGO_NAME);
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
                                            + CoCoinUtil.LOGO_NAME))
                                    .setCallback(new FutureCallback<File>() {
                                        @Override
                                        public void onCompleted(Exception e, File file) {
                                            profileImage.setImageBitmap(BitmapFactory.decodeFile(
                                                    CoCoinApplication.getAppContext().getFilesDir()
                                                            + CoCoinUtil.LOGO_NAME));
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

    private void setIconEnable(MaterialIconView icon, boolean enable) {
        if (enable) icon.setColor(mContext.getResources().getColor(R.color.my_blue));
        else icon.setColor(mContext.getResources().getColor(R.color.my_gray));
    }
}
