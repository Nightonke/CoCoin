package com.nightonke.saver.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.nightonke.saver.R;
import com.nightonke.saver.adapter.ButtonGridViewAdapter;
import com.nightonke.saver.fragment.EditFragment;
import com.nightonke.saver.fragment.TagChooseFragment;
import com.nightonke.saver.model.Record;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.model.User;
import com.nightonke.saver.ui.CoCoinViewPager;
import com.nightonke.saver.ui.DummyOperation;
import com.nightonke.saver.ui.MyGridView;
import com.nightonke.saver.ui.guillotine.animation.GuillotineAnimation;
import com.nightonke.saver.ui.guillotine.interfaces.GuillotineListener;
import com.nightonke.saver.util.CoCoinToast;
import com.nightonke.saver.util.CoCoinUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.rey.material.widget.RadioButton;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobUser;

public class MainActivity extends AppCompatActivity
        implements TagChooseFragment.OnTagItemSelectedListener {

    private final int SETTING_TAG = 0;

    private Context mContext;

    private View guillotineBackground;

    private TextView toolBarTitle;
    private TextView menuToolBarTitle;

    private TextView passwordTip;

    private SuperToast superToast;
    private SuperActivityToast superActivityToast;

    private MyGridView myGridView;
    private ButtonGridViewAdapter myGridViewAdapter;

    private LinearLayout transparentLy;
    private LinearLayout guillotineColorLy;

    private boolean isPassword = false;

    private long RIPPLE_DURATION = 250;

    private GuillotineAnimation animation;

    private String inputPassword = "";

    private float x1, y1, x2, y2;

    private RadioButton radioButton0;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;

    private MaterialMenuView statusButton;

    private LinearLayout radioButtonLy;

    private View guillotineMenu;

    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;

    private CoCoinViewPager editViewPager;
    private SmartTabLayout smartEditTabLayout;

    private boolean isLoading;

    private DummyOperation dummyOperation;

    private final int NO_TAG_TOAST = 0;
    private final int NO_MONEY_TOAST = 1;
    private final int PASSWORD_WRONG_TOAST = 2;
    private final int PASSWORD_CORRECT_TOAST = 3;
    private final int SAVE_SUCCESSFULLY_TOAST = 4;
    private final int SAVE_FAILED_TOAST = 5;
    private final int PRESS_AGAIN_TO_EXIT = 6;
    private final int WELCOME_BACK = 7;

    boolean doubleBackToExitPressedOnce = false;

    private Toolbar guillotineToolBar;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.root)
    FrameLayout root;
    @InjectView(R.id.content_hamburger)
    View contentHamburger;

    private FragmentPagerItemAdapter tagChoicePagerAdapter;
    private FragmentPagerItemAdapter editPagerAdapter;

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContext = this;

        CrashReport.initCrashReport(mContext, "900016815", false);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(listener, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

        superToast = new SuperToast(this);
        superActivityToast = new SuperActivityToast(this, SuperToast.Type.PROGRESS_HORIZONTAL);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        Log.d("Saver", "Version number: " + currentapiVersion);

        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.statusBarColor));
        } else{
            // do something for phones running an SDK before lollipop
        }

//        Bmob.initialize(CoCoinApplication.getAppContext(), "0f0f9d45a39068bd6eea8896af1facf3");

        CoCoinUtil.init(this.getApplicationContext());

        RecordManager recordManager = RecordManager.getInstance(this.getApplicationContext());

        User user = BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
        if (user != null) {
            SettingManager.getInstance().setLoggenOn(true);
            SettingManager.getInstance().setUserName(user.getUsername());
            SettingManager.getInstance().setUserEmail(user.getEmail());
            showToast(WELCOME_BACK);
            // 允许用户使用应用
        } else {
            SettingManager.getInstance().setLoggenOn(false);
            //缓存用户对象为空时， 可打开用户注册界面…
        }

        guillotineBackground = findViewById(R.id.guillotine_background);

        toolBarTitle = (TextView)findViewById(R.id.guillotine_title);
        toolBarTitle.setTypeface(CoCoinUtil.typefaceLatoLight);
        toolBarTitle.setText(SettingManager.getInstance().getAccountBookName());

// edit viewpager///////////////////////////////////////////////////////////////////////////////////
        editViewPager = (CoCoinViewPager)findViewById(R.id.edit_pager);
        smartEditTabLayout = (SmartTabLayout)findViewById(R.id.edit_viewpager_tab);
        FragmentPagerItems editPagers = new FragmentPagerItems(this);
        for (int i = 0; i < 2; i++) {
            editPagers.add(FragmentPagerItem.of("1", EditFragment.class));
        }

        editPagerAdapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), editPagers);
        editViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1) {
                    ((EditFragment)editPagerAdapter.getPage(1)).editRequestFocus();
                } else {
                    ((EditFragment)editPagerAdapter.getPage(0)).editRequestFocus();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        editViewPager.setOffscreenPageLimit(2);
        editViewPager.setAdapter(editPagerAdapter);

        smartEditTabLayout.setViewPager(editViewPager);
        smartEditTabLayout.setVisibility(View.GONE);

// tag viewpager////////////////////////////////////////////////////////////////////////////////////
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        smartTabLayout = (SmartTabLayout)findViewById(R.id.viewpagertab);

        FragmentPagerItems pages = new FragmentPagerItems(this);
        for (int i = 0; i < 4; i++) {
            pages.add(FragmentPagerItem.of("1", TagChooseFragment.class));
        }

        tagChoicePagerAdapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        viewPager.setOffscreenPageLimit(4);

        viewPager.setAdapter(tagChoicePagerAdapter);

        smartTabLayout.setViewPager(viewPager);
        smartTabLayout.setVisibility(View.GONE);

// button grid view/////////////////////////////////////////////////////////////////////////////////
        myGridView = (MyGridView)findViewById(R.id.gridview);
        myGridViewAdapter = new ButtonGridViewAdapter(this);
        myGridView.setAdapter(myGridViewAdapter);

        myGridView.setOnItemClickListener(gridViewClickListener);
        myGridView.setOnItemLongClickListener(gridViewLongClickListener);

        myGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        myGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        View lastChild = myGridView.getChildAt(myGridView.getChildCount() - 1);
                        myGridView.setLayoutParams(
                                new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.FILL_PARENT, lastChild.getBottom()));

                        ViewGroup.LayoutParams params = transparentLy.getLayoutParams();
                        params.height = myGridView.getMeasuredHeight();
                    }
                });

        ButterKnife.inject(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        toolbar.hideOverflowMenu();

        guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

        transparentLy = (LinearLayout)guillotineMenu.findViewById(R.id.transparent_ly);
        guillotineColorLy = (LinearLayout)guillotineMenu.findViewById(R.id.guillotine_color_ly);
        guillotineToolBar = (Toolbar)guillotineMenu.findViewById(R.id.toolbar);

        menuToolBarTitle = (TextView)guillotineMenu.findViewById(R.id.guillotine_title);
        menuToolBarTitle.setTypeface(CoCoinUtil.typefaceLatoLight);
        menuToolBarTitle.setText(SettingManager.getInstance().getAccountBookName());

        radioButton0 = (RadioButton)guillotineMenu.findViewById(R.id.radio_button_0);
        radioButton1 = (RadioButton)guillotineMenu.findViewById(R.id.radio_button_1);
        radioButton2 = (RadioButton)guillotineMenu.findViewById(R.id.radio_button_2);
        radioButton3 = (RadioButton)guillotineMenu.findViewById(R.id.radio_button_3);

        passwordTip = (TextView)guillotineMenu.findViewById(R.id.password_tip);
        passwordTip.setText(mContext.getResources().getString(R.string.password_tip));
        passwordTip.setTypeface(CoCoinUtil.typefaceLatoLight);

        radioButtonLy = (LinearLayout)guillotineMenu.findViewById(R.id.radio_button_ly);

        statusButton = (MaterialMenuView)guillotineMenu.findViewById(R.id.status_button);
        statusButton.setState(MaterialMenuDrawable.IconState.ARROW);

        statusButton.setOnClickListener(statusButtonOnClickListener);

        animation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu,
                        guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                        isPassword = true;
                    }

                    @Override
                    public void onGuillotineClosed() {
                        isPassword = false;
                        ((EditFragment)editPagerAdapter.
                                getPage(editViewPager.getCurrentItem())).editRequestFocus();
                        radioButton0.setChecked(false);
                        radioButton1.setChecked(false);
                        radioButton2.setChecked(false);
                        radioButton3.setChecked(false);
                        inputPassword = "";
                        statusButton.setState(MaterialMenuDrawable.IconState.ARROW);
                    }
                })
                .build();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.open();
            }
        });

        if (SettingManager.getInstance().getFirstTime()) {
            Intent intent = new Intent(mContext, SetPasswordActivity.class);
            startActivity(intent);
        }

        if (SettingManager.getInstance().getShowMainActivityGuide()) {
            boolean wrapInScrollView = true;
            new MaterialDialog.Builder(this)
                    .title(R.string.guide)
                    .typeface(CoCoinUtil.GetTypeface(), CoCoinUtil.GetTypeface())
                    .customView(R.layout.main_activity_guide, wrapInScrollView)
                    .positiveText(R.string.ok)
                    .show();
            SettingManager.getInstance().setShowMainActivityGuide(false);
        }
    }

    private AdapterView.OnItemLongClickListener gridViewLongClickListener
            = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (!isLoading) {
                buttonClickOperation(true, position);
            }
            return true;
        }
    };


    private void checkPassword() {
        if (inputPassword.length() != 4) {
            return;
        }
        if (SettingManager.getInstance().getPassword().equals(inputPassword)) {
            isLoading = true;
            YoYo.with(Techniques.Bounce).delay(0).duration(1000).playOn(radioButton3);
            statusButton.animateState(MaterialMenuDrawable.IconState.CHECK);
            statusButton.setClickable(false);
            showToast(PASSWORD_CORRECT_TOAST);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext, AccountBookTodayViewActivity.class);
                    startActivityForResult(intent, SETTING_TAG);
                    isLoading = false;
                }
            }, 1000);
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animation.close();
                }
            }, 3000);
        } else {
            showToast(PASSWORD_WRONG_TOAST);
            YoYo.with(Techniques.Shake).duration(700).playOn(radioButtonLy);
            radioButton0.setChecked(false);
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            inputPassword = "";
            statusButton.animateState(MaterialMenuDrawable.IconState.X);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTING_TAG:
                if (resultCode == RESULT_OK) {
                    if (data.getBooleanExtra("IS_CHANGED", false)) {
                        for (int i = 0; i < tagChoicePagerAdapter.getCount(); i++) {
                            ((TagChooseFragment)tagChoicePagerAdapter.
                                    getItem(i)).updateTags();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private View.OnClickListener statusButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            animation.close();
        }
    };

    private AdapterView.OnItemClickListener gridViewClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!isLoading) {
                buttonClickOperation(false, position);
            }
        }
    };

    private void buttonClickOperation(boolean longClick, int position) {
        if (editViewPager.getCurrentItem() == 1) return;
        if (!isPassword) {
            if (((EditFragment)editPagerAdapter.getPage(0)).getNumberText().toString().equals("0")
                    && !CoCoinUtil.ClickButtonCommit(position)) {
                if (CoCoinUtil.ClickButtonDelete(position)
                        || CoCoinUtil.ClickButtonIsZero(position)) {

                } else {
                    ((EditFragment)editPagerAdapter.getPage(0)).setNumberText(CoCoinUtil.BUTTONS[position]);
                }
            } else {
                if (CoCoinUtil.ClickButtonDelete(position)) {
                    if (longClick) {
                        ((EditFragment)editPagerAdapter.getPage(0)).setNumberText("0");
                        ((EditFragment)editPagerAdapter.getPage(0)).setHelpText(
                                CoCoinUtil.FLOATINGLABELS[((EditFragment) editPagerAdapter.getPage(0))
                                        .getNumberText().toString().length()]);
                    } else {
                        ((EditFragment)editPagerAdapter.getPage(0)).setNumberText(
                                ((EditFragment)editPagerAdapter.getPage(0)).getNumberText().toString()
                                .substring(0, ((EditFragment)editPagerAdapter.getPage(0))
                                        .getNumberText().toString().length() - 1));
                        if (((EditFragment)editPagerAdapter.getPage(0))
                                .getNumberText().toString().length() == 0) {
                            ((EditFragment)editPagerAdapter.getPage(0)).setNumberText("0");
                            ((EditFragment)editPagerAdapter.getPage(0)).setHelpText(" ");
                        }
                    }
                } else if (CoCoinUtil.ClickButtonCommit(position)) {
                    commit();
                } else {
                    ((EditFragment)editPagerAdapter.getPage(0)).setNumberText(
                            ((EditFragment)editPagerAdapter.getPage(0)).getNumberText().toString()
                                    + CoCoinUtil.BUTTONS[position]);
                }
            }
            ((EditFragment)editPagerAdapter.getPage(0))
                    .setHelpText(CoCoinUtil.FLOATINGLABELS[
                            ((EditFragment) editPagerAdapter.getPage(0)).getNumberText().toString().length()]);
        } else {
            if (CoCoinUtil.ClickButtonDelete(position)) {
                if (longClick) {
                    radioButton0.setChecked(false);
                    radioButton1.setChecked(false);
                    radioButton2.setChecked(false);
                    radioButton3.setChecked(false);
                    inputPassword = "";
                } else {
                    if (inputPassword.length() == 0) {
                        inputPassword = "";
                    } else {
                        if (inputPassword.length() == 1) {
                            radioButton0.setChecked(false);
                        } else if (inputPassword.length() == 2) {
                            radioButton1.setChecked(false);
                        } else if (inputPassword.length() == 3) {
                            radioButton2.setChecked(false);
                        } else {
                            radioButton3.setChecked(false);
                        }
                        inputPassword = inputPassword.substring(0, inputPassword.length() - 1);
                    }
                }
            } else if (CoCoinUtil.ClickButtonCommit(position)) {
            } else {
                if (statusButton.getState() == MaterialMenuDrawable.IconState.X) {
                    statusButton.animateState(MaterialMenuDrawable.IconState.ARROW);
                }
                if (inputPassword.length() == 0) {
                    radioButton0.setChecked(true);
                    YoYo.with(Techniques.Bounce).delay(0).duration(1000).playOn(radioButton0);
                } else if (inputPassword.length() == 1) {
                    radioButton1.setChecked(true);
                    YoYo.with(Techniques.Bounce).delay(0).duration(1000).playOn(radioButton1);
                } else if (inputPassword.length() == 2) {
                    radioButton2.setChecked(true);
                    YoYo.with(Techniques.Bounce).delay(0).duration(1000).playOn(radioButton2);
                } else if (inputPassword.length() == 3) {
                    radioButton3.setChecked(true);
                }
                if (inputPassword.length() < 4) {
                    inputPassword += CoCoinUtil.BUTTONS[position];
                }
            }
            checkPassword();
        }
    }

    private void commit() {
        if (((EditFragment)editPagerAdapter.getPage(0)).getTagId() == -1) {
            showToast(NO_TAG_TOAST);
        } else if (((EditFragment)editPagerAdapter.getPage(0)).getNumberText().toString().equals("0")) {
            showToast(NO_MONEY_TOAST);
        } else  {
            Calendar calendar = Calendar.getInstance();
            Record record = new Record(
                    -1,
                    Float.valueOf(((EditFragment)editPagerAdapter.getPage(0)).getNumberText().toString()),
                    "RMB",
                    ((EditFragment)editPagerAdapter.getPage(0)).getTagId(),
                    calendar);
            record.setRemark(((EditFragment)editPagerAdapter.getPage(1)).getRemark());
            long saveId = RecordManager.saveRecord(record);
            if (saveId == -1) {

            } else {
                if (!superToast.isShowing()) {
                    changeColor();
                }
                ((EditFragment)editPagerAdapter.getPage(0)).setTagImage(R.color.transparent);
                ((EditFragment)editPagerAdapter.getPage(0)).setTagName("");
            }
            ((EditFragment)editPagerAdapter.getPage(0)).setNumberText("0");
            ((EditFragment)editPagerAdapter.getPage(0)).setHelpText(" ");
        }
    }

    private void tagAnimation() {
        YoYo.with(Techniques.Shake).duration(1000).playOn(viewPager);
    }

    private void showToast(int toastType) {
        switch (toastType) {
            case NO_TAG_TOAST:
                CoCoinToast.getInstance().showToast(R.string.toast_no_tag, SuperToast.Background.RED);
                tagAnimation();
                break;
            case NO_MONEY_TOAST:
                CoCoinToast.getInstance().showToast(R.string.toast_no_money, SuperToast.Background.RED);
                break;
            case PASSWORD_WRONG_TOAST:
                CoCoinToast.getInstance().showToast(R.string.toast_password_wrong, SuperToast.Background.RED);
                break;
            case PASSWORD_CORRECT_TOAST:
                CoCoinToast.getInstance().showToast(R.string.toast_password_correct, SuperToast.Background.BLUE);
                break;
            case SAVE_SUCCESSFULLY_TOAST:
                break;
            case SAVE_FAILED_TOAST:
                break;
            case PRESS_AGAIN_TO_EXIT:
                CoCoinToast.getInstance().showToast(R.string.toast_press_again_to_exit, SuperToast.Background.BLUE);
                break;
            case WELCOME_BACK:
                CoCoinToast.getInstance().showToast(CoCoinApplication.getAppContext()
                        .getResources().getString(R.string.welcome_back)
                        + "\n" + SettingManager.getInstance().getUserName(), SuperToast.Background.BLUE);
            default:
                break;
        }
    }

    private void changeColor() {
        boolean shouldChange
                = SettingManager.getInstance().getIsMonthLimit()
                && SettingManager.getInstance().getIsColorRemind()
                && RecordManager.getCurrentMonthExpense()
                >= SettingManager.getInstance().getMonthWarning();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            if (shouldChange) {
                window.setStatusBarColor(
                        CoCoinUtil.getDeeperColor(SettingManager.getInstance().getRemindColor()));
            } else {
                window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.statusBarColor));
            }

        } else{
            // do something for phones running an SDK before lollipop
        }

        if (shouldChange) {
            root.setBackgroundColor(SettingManager.getInstance().getRemindColor());
            toolbar.setBackgroundColor(SettingManager.getInstance().getRemindColor());
            guillotineBackground.setBackgroundColor(SettingManager.getInstance().getRemindColor());
            guillotineColorLy.setBackgroundColor(SettingManager.getInstance().getRemindColor());
            guillotineToolBar.setBackgroundColor(SettingManager.getInstance().getRemindColor());
        } else {
            root.setBackgroundColor(CoCoinUtil.MY_BLUE);
            toolbar.setBackgroundColor(CoCoinUtil.MY_BLUE);
            guillotineBackground.setBackgroundColor(CoCoinUtil.MY_BLUE);
            guillotineColorLy.setBackgroundColor(CoCoinUtil.MY_BLUE);
            guillotineToolBar.setBackgroundColor(CoCoinUtil.MY_BLUE);
        }
        if (editPagerAdapter.getPage(0) != null)
            ((EditFragment)editPagerAdapter.getPage(0)).setEditColor(shouldChange);
        if (editPagerAdapter.getPage(1) != null)
            ((EditFragment)editPagerAdapter.getPage(1)).setEditColor(shouldChange);
        myGridViewAdapter.notifyDataSetInvalidated();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = ev.getX();
                y1 = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                x2 = ev.getX();
                y2 = ev.getY();
                if (Math.abs(y2 - y1) > Math.abs(x2 - x1)) {
                    if (y2 - y1 > 300) {
                        if (!isPassword) {
                            animation.open();
                        }
                    }
                    if (y1 - y2 > 300) {
                        if (isPassword) {
                            animation.close();
                        }
                    }
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            SuperToast.cancelAllSuperToasts();
            return;
        }

        showToast(PRESS_AGAIN_TO_EXIT);

        doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onResume() {
        super.onResume();

        // if the tags' order has been changed
        if (SettingManager.getInstance().getMainActivityTagShouldChange()) {
            // change the tag fragment
            for (int i = 0; i < tagChoicePagerAdapter.getCount(); i++) {
                ((TagChooseFragment)tagChoicePagerAdapter.
                        getItem(i)).updateTags();
            }
            // and tell others that main activity has changed
            SettingManager.getInstance().setMainActivityTagShouldChange(false);
        }

        // if the title should be changed
        if (SettingManager.getInstance().getMainViewTitleShouldChange()) {
            menuToolBarTitle.setText(SettingManager.getInstance().getAccountBookName());
            toolBarTitle.setText(SettingManager.getInstance().getAccountBookName());
            SettingManager.getInstance().setMainViewTitleShouldChange(false);
        }

        changeColor();

        radioButton0.setChecked(false);
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);
        radioButton3.setChecked(false);

        isLoading = false;
        inputPassword = "";
        System.gc();
    }

    @Override
    public void onDestroy() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(listener);
        }
        super.onDestroy();
    }

    @Override
    public void onTagItemPicked(int position) {
        ((EditFragment)editPagerAdapter.getPage(0)).setTag(viewPager.getCurrentItem() * 8 + position + 2);
    }

    private static final float SHAKE_ACCELERATED_SPEED = 15;
    private SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                if ((Math.abs(event.values[0]) > SHAKE_ACCELERATED_SPEED
                        || Math.abs(event.values[1]) > SHAKE_ACCELERATED_SPEED
                        || Math.abs(event.values[2]) > SHAKE_ACCELERATED_SPEED)) {
                    if (!isPassword) {
                        animation.open();
                    } else {
                        animation.close();
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
