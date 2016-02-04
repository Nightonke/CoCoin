package com.nightonke.saver.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.sacot41.scviewpager.DotsView;
import com.dev.sacot41.scviewpager.SCPositionAnimation;
import com.dev.sacot41.scviewpager.SCViewAnimation;
import com.dev.sacot41.scviewpager.SCViewAnimationUtil;
import com.dev.sacot41.scviewpager.SCViewPager;
import com.dev.sacot41.scviewpager.SCViewPagerAdapter;
import com.github.johnpersano.supertoasts.SuperToast;
import com.nightonke.saver.R;
import com.nightonke.saver.adapter.PasswordChangeButtonGridViewAdapter;
import com.nightonke.saver.adapter.PasswordChangeFragmentAdapter;
import com.nightonke.saver.fragment.CoCoinFragmentManager;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.model.User;
import com.nightonke.saver.ui.MyGridView;
import com.nightonke.saver.util.CoCoinUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class ShowActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 5;

    private SCViewPager mViewPager;
    private SCViewPagerAdapter mPageAdapter;
    private DotsView mDotsView;

    private View toolbarLayout;

    private Context mContext;

    private MyGridView myGridView;
    private PasswordChangeButtonGridViewAdapter myGridViewAdapter;

    private static final int NEW_PASSWORD = 0;
    private static final int PASSWORD_AGAIN = 1;

    private int CURRENT_STATE = NEW_PASSWORD;

    private String newPassword = "";
    private String againPassword = "";

    private ViewPager viewPager;
    private PasswordChangeFragmentAdapter passwordAdapter;

    private TextView title;

    private SuperToast superToast;

    private float x1, y1, x2, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mContext = this;

        title = (TextView)findViewById(R.id.title);
        CoCoinUtil.init(mContext);
        title.setTypeface(CoCoinUtil.typefaceLatoLight);
        title.setText(mContext.getResources().getString(R.string.app_name));

        mViewPager = (SCViewPager) findViewById(R.id.viewpager_main_activity);
        mDotsView = (DotsView) findViewById(R.id.dotsview_main);
        mDotsView.setDotRessource(R.drawable.dot_selected, R.drawable.dot_unselected);
        mDotsView.setNumberOfPage(NUM_PAGES);

        mPageAdapter = new SCViewPagerAdapter(getSupportFragmentManager());
        mPageAdapter.setNumberOfPage(NUM_PAGES);
        mPageAdapter.setFragmentBackgroundColor(R.color.my_blue);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mDotsView.selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final Point size = SCViewAnimationUtil.getDisplaySize(this);

        int iconOffsetX = CoCoinUtil.getInstance().dpToPx(28);
        int iconOffsetY = CoCoinUtil.getInstance().dpToPx(28);

        SCViewAnimation sc0 = new SCViewAnimation(findViewById(R.id.icon_4));
        sc0.startToPosition(size.x / 4 - iconOffsetX, size.y * 2 / 7 - iconOffsetY);
        sc0.addPageAnimation(new SCPositionAnimation(this, 0, 0, size.y));
        mViewPager.addAnimation(sc0);

        SCViewAnimation sc1 = new SCViewAnimation(findViewById(R.id.icon_11));
        sc1.startToPosition(size.x * 3 / 4 - iconOffsetX, size.y * 3 / 7 - iconOffsetY);
        sc1.addPageAnimation(new SCPositionAnimation(this, 0, -size.x, 0));
        mViewPager.addAnimation(sc1);

        SCViewAnimation sc2 = new SCViewAnimation(findViewById(R.id.icon_12));
        sc2.startToPosition(size.x / 4 - iconOffsetX, size.y * 4 / 7 - iconOffsetY);
        sc2.addPageAnimation(new SCPositionAnimation(this, 0, size.x, 0));
        mViewPager.addAnimation(sc2);

        SCViewAnimation sc3 = new SCViewAnimation(findViewById(R.id.icon_19));
        sc3.startToPosition(size.x * 3 / 4 - iconOffsetX, size.y * 5 / 7 - iconOffsetY);
        sc3.addPageAnimation(new SCPositionAnimation(this, 0, 0, -size.y));
        mViewPager.addAnimation(sc3);

        ((TextView)findViewById(R.id.text_0)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        SCViewAnimation sc4 = new SCViewAnimation(findViewById(R.id.text_0));
        sc4.addPageAnimation(new SCPositionAnimation(this, 0, -size.x, 0));
        mViewPager.addAnimation(sc4);

        PieChartView pie = (PieChartView)findViewById(R.id.pie);
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < 5; ++i) {
            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ContextCompat.getColor(CoCoinApplication.getAppContext(), R.color.white));
            values.add(sliceValue);
        }
        PieChartData pieData = new PieChartData(values);
        pieData.setHasLabels(false);
        pieData.setHasLabelsOnlyForSelected(false);
        pieData.setHasLabelsOutside(false);
        pieData.setHasCenterCircle(true);
        pie.setPieChartData(pieData);
        pie.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        SCViewAnimation sc5 = new SCViewAnimation(pie);
        sc5.startToPosition(size.x / 2, size.y / 9 - size.y);
        sc5.addPageAnimation(new SCPositionAnimation(this, 0, 0, size.y));
        sc5.addPageAnimation(new SCPositionAnimation(this, 1, 0, size.y));
        mViewPager.addAnimation(sc5);

        LineChartView line = (LineChartView)findViewById(R.id.line);
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < 1; ++i) {
            List<PointValue> pointValues = new ArrayList<PointValue>();

            pointValues.add(new PointValue(0, 50));
            pointValues.add(new PointValue(1, 100));
            pointValues.add(new PointValue(2, 20));
            pointValues.add(new PointValue(3, 0));
            pointValues.add(new PointValue(4, 10));
            pointValues.add(new PointValue(5, 15));
            pointValues.add(new PointValue(6, 40));
            pointValues.add(new PointValue(7, 60));
            pointValues.add(new PointValue(8, 100));

            Line aLine = new Line(pointValues);
            aLine.setColor(ContextCompat.getColor(CoCoinApplication.getAppContext(), R.color.white));
            aLine.setShape(ValueShape.CIRCLE);
            aLine.setCubic(false);
            aLine.setFilled(false);
            aLine.setHasLabels(false);
            aLine.setHasLabelsOnlyForSelected(false);
            aLine.setHasLines(true);
            aLine.setHasPoints(true);
            lines.add(aLine);
        }
        LineChartData linedata = new LineChartData(lines);
        linedata.setBaseValue(Float.NEGATIVE_INFINITY);
        line.setLineChartData(linedata);
        line.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        SCViewAnimation sc6 = new SCViewAnimation(line);
        sc6.startToPosition(-size.x, null);
        sc6.addPageAnimation(new SCPositionAnimation(this, 0, size.x, 0));
        sc6.addPageAnimation(new SCPositionAnimation(this, 1, size.x, 0));
        mViewPager.addAnimation(sc6);

        ColumnChartView histogram = (ColumnChartView)findViewById(R.id.histogram);
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> subcolumnValues;
        for (int i = 0; i < 5; ++i) {
            subcolumnValues = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < 1; ++j) {
                subcolumnValues.add(new SubcolumnValue((float) Math.random() * 50f + 5, ContextCompat.getColor(CoCoinApplication.getAppContext(), R.color.white)));
            }
            Column column = new Column(subcolumnValues);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }
        ColumnChartData histogramData = new ColumnChartData(columns);
        histogram.setColumnChartData(histogramData);
        histogram.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        SCViewAnimation sc7 = new SCViewAnimation(histogram);
        sc7.startToPosition(size.x / 2 - CoCoinUtil.getInstance().dpToPx(140), size.y * 8 / 9 - CoCoinUtil.getInstance().dpToPx(140) + size.y);
        sc7.addPageAnimation(new SCPositionAnimation(this, 0, 0, -size.y));
        sc7.addPageAnimation(new SCPositionAnimation(this, 1, 0, size.y));
        mViewPager.addAnimation(sc7);

        ((TextView)findViewById(R.id.text_1)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        SCViewAnimation sc8 = new SCViewAnimation(findViewById(R.id.text_1));
        sc8.startToPosition(size.x, null);
        sc8.addPageAnimation(new SCPositionAnimation(this, 0, -size.x, 0));
        sc8.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        mViewPager.addAnimation(sc8);

        SCViewAnimation sc9 = new SCViewAnimation(findViewById(R.id.cloud));
        sc9.startToPosition(size.x / 2 - CoCoinUtil.getInstance().dpToPx(100) + size.x, size.y / 7);
        sc9.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        sc9.addPageAnimation(new SCPositionAnimation(this, 2, 0, size.y));
        mViewPager.addAnimation(sc9);

        SCViewAnimation sc10 = new SCViewAnimation(findViewById(R.id.mobile));
        sc10.startToPosition(size.x / 2 - size.x, size.y * 6 / 7 - CoCoinUtil.getInstance().dpToPx(100));
        sc10.addPageAnimation(new SCPositionAnimation(this, 1, size.x, 0));
        sc10.addPageAnimation(new SCPositionAnimation(this, 2, 0, -size.y));
        mViewPager.addAnimation(sc10);

        ((TextView)findViewById(R.id.text_2)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        SCViewAnimation sc11 = new SCViewAnimation(findViewById(R.id.text_2));
        sc11.startToPosition(size.x, null);
        sc11.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        sc11.addPageAnimation(new SCPositionAnimation(this, 2, -size.x, 0));
        mViewPager.addAnimation(sc11);

        ImageView remind1 = (ImageView)findViewById(R.id.remind_1);
        remind1.getLayoutParams().width = size.x / 3;
        remind1.getLayoutParams().height = size.x / 3 * 653 / 320;
        SCViewAnimation sc12 = new SCViewAnimation(findViewById(R.id.remind_1));
        sc12.startToPosition(size.x / 2 - size.x, size.y / 11);
        sc12.addPageAnimation(new SCPositionAnimation(this, 2, size.x, 0));
        sc12.addPageAnimation(new SCPositionAnimation(this, 3, size.x, 0));
        mViewPager.addAnimation(sc12);

        ImageView remind2 = (ImageView)findViewById(R.id.remind_2);
        remind2.getLayoutParams().width = size.x / 3;
        remind2.getLayoutParams().height = size.x / 3 * 653 / 320;
        SCViewAnimation sc13 = new SCViewAnimation(findViewById(R.id.remind_2));
        sc13.startToPosition(size.x / 2 + size.x - size.x / 3, size.y * 10 / 11 - remind1.getLayoutParams().height);
        sc13.addPageAnimation(new SCPositionAnimation(this, 2, -size.x, 0));
        sc13.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(sc13);

        ((TextView)findViewById(R.id.text_3)).setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        SCViewAnimation sc14 = new SCViewAnimation(findViewById(R.id.text_3));
        sc14.startToPosition(size.x, null);
        sc14.addPageAnimation(new SCPositionAnimation(this, 2, -size.x, 0));
        sc14.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(sc14);

        View statusBar = findViewById(R.id.status_bar);
        statusBar.setLayoutParams(new RelativeLayout.LayoutParams(statusBar.getLayoutParams().width, getStatusBarHeight()));
        SCViewAnimation statusBarAnimation = new SCViewAnimation(statusBar);
        statusBarAnimation.startToPosition(null, -getStatusBarHeight());
        statusBarAnimation.addPageAnimation(new SCPositionAnimation(this, 3, 0, getStatusBarHeight()));
        mViewPager.addAnimation(statusBarAnimation);

        toolbarLayout = findViewById(R.id.toolbar_layout);
        SCViewAnimation toolbarLayoutAnimation = new SCViewAnimation(toolbarLayout);
        toolbarLayoutAnimation.startToPosition(null, -size.y / 2);
        toolbarLayoutAnimation.addPageAnimation(new SCPositionAnimation(this, 3, 0, size.y / 2));
        mViewPager.addAnimation(toolbarLayoutAnimation);

        passwordAdapter = new PasswordChangeFragmentAdapter(
                getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setScrollBarFadeDuration(700);

        viewPager.setAdapter(passwordAdapter);

        myGridView = (MyGridView)findViewById(R.id.gridview);
        myGridViewAdapter = new PasswordChangeButtonGridViewAdapter(this);
        myGridView.setAdapter(myGridViewAdapter);

        myGridView.setOnItemClickListener(gridViewClickListener);
        myGridView.setOnItemLongClickListener(gridViewLongClickListener);

        myGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        myGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        View lastChild = myGridView.getChildAt(myGridView.getChildCount() - 1);
                        RelativeLayout.LayoutParams relativeLayout = new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT, lastChild.getBottom());
                        relativeLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        myGridView.setLayoutParams(relativeLayout);

                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int height = displaymetrics.heightPixels;
                        RelativeLayout.LayoutParams viewPagerLayoutParams
                                = new RelativeLayout.LayoutParams(viewPager.getLayoutParams().width,
                                800);
                        viewPagerLayoutParams.topMargin
                                = getStatusBarHeight() + CoCoinUtil.getToolBarHeight(mContext) / 2;
                        viewPager.setLayoutParams(viewPagerLayoutParams);
                    }
                });

        superToast = new SuperToast(this);

        SCViewAnimation gridViewAnimation = new SCViewAnimation(myGridView);
        gridViewAnimation.startToPosition(null, size.y);
        gridViewAnimation.addPageAnimation(new SCPositionAnimation(this, 3, 0, -size.y));
        mViewPager.addAnimation(gridViewAnimation);

        SCViewAnimation viewpagerAnimation = new SCViewAnimation(viewPager);
        viewpagerAnimation.startToPosition(null, -size.y);
        viewpagerAnimation.addPageAnimation(new SCPositionAnimation(this, 3, 0, size.y));
        mViewPager.addAnimation(viewpagerAnimation);

        View background = findViewById(R.id.background);
        SCViewAnimation backgroundAnimation = new SCViewAnimation(background);
        backgroundAnimation.startToPosition(null, -size.y - 100);
        backgroundAnimation.addPageAnimation(new SCPositionAnimation(this, 3, 0, size.y + 100));
        mViewPager.addAnimation(backgroundAnimation);

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void finish() {
        SuperToast.cancelAllSuperToasts();
        super.finish();
    }

    private AdapterView.OnItemClickListener gridViewClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            buttonClickOperation(false, position);
        }
    };

    private AdapterView.OnItemLongClickListener gridViewLongClickListener
            = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            buttonClickOperation(true, position);
            return true;
        }
    };

    private void buttonClickOperation(boolean longClick, int position) {
        switch (CURRENT_STATE) {
            case NEW_PASSWORD:
                if (CoCoinUtil.ClickButtonDelete(position)) {
                    if (longClick) {
                        CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE].init();
                        newPassword = "";
                    } else {
                        CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE]
                                .clear(newPassword.length() - 1);
                        if (newPassword.length() != 0)
                            newPassword = newPassword.substring(0, newPassword.length() - 1);
                    }
                } else if (CoCoinUtil.ClickButtonCommit(position)) {

                } else {
                    CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE]
                            .set(newPassword.length());
                    newPassword += CoCoinUtil.BUTTONS[position];
                    if (newPassword.length() == 4) {
                        // finish the new password input
                        CURRENT_STATE = PASSWORD_AGAIN;
                        viewPager.setCurrentItem(PASSWORD_AGAIN, true);
                    }
                }
                break;
            case PASSWORD_AGAIN:
                if (CoCoinUtil.ClickButtonDelete(position)) {
                    if (longClick) {
                        CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE].init();
                        againPassword = "";
                    } else {
                        CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE]
                                .clear(againPassword.length() - 1);
                        if (againPassword.length() != 0)
                            againPassword = againPassword.substring(0, againPassword.length() - 1);
                    }
                } else if (CoCoinUtil.ClickButtonCommit(position)) {

                } else {
                    CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE]
                            .set(againPassword.length());
                    againPassword += CoCoinUtil.BUTTONS[position];
                    if (againPassword.length() == 4) {
                        // if the password again is equal to the new password
                        if (againPassword.equals(newPassword)) {
                            CURRENT_STATE = -1;
                            showToast(2);
                            SettingManager.getInstance().setPassword(newPassword);
                            SettingManager.getInstance().setFirstTime(false);
                            if (SettingManager.getInstance().getLoggenOn()) {
                                User currentUser = BmobUser.getCurrentUser(
                                        CoCoinApplication.getAppContext(), User.class);
                                currentUser.setAccountBookPassword(newPassword);
                                currentUser.update(CoCoinApplication.getAppContext(),
                                        currentUser.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void onSuccess() {
                                                Log.d("Saver", "Set password successfully.");
                                            }

                                            @Override
                                            public void onFailure(int code, String msg) {
                                                Log.d("Saver", "Set password failed.");
                                            }
                                        });
                            }
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);
                        } else {
                            CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE].clear(4);
                            CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE - 1].init();
                            CURRENT_STATE = NEW_PASSWORD;
                            viewPager.setCurrentItem(NEW_PASSWORD, true);
                            newPassword = "";
                            againPassword = "";
                            showToast(1);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showToast(int toastType) {
        SuperToast.cancelAllSuperToasts();

        superToast.setAnimations(CoCoinUtil.TOAST_ANIMATION);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);

        switch (toastType) {
            // old password wrong
            case 0:

                superToast.setText(
                        mContext.getResources().getString(R.string.toast_password_wrong));
                superToast.setBackground(SuperToast.Background.RED);
                superToast.getTextView().setTypeface(CoCoinUtil.typefaceLatoLight);

                break;
            // password is different
            case 1:

                superToast.setText(
                        mContext.getResources().getString(R.string.different_password));
                superToast.setBackground(SuperToast.Background.RED);
                superToast.getTextView().setTypeface(CoCoinUtil.typefaceLatoLight);

                break;
            // success
            case 2:

                superToast.setText(
                        mContext.getResources().getString(R.string.set_password_successfully));
                superToast.setBackground(SuperToast.Background.GREEN);
                superToast.getTextView().setTypeface(CoCoinUtil.typefaceLatoLight);

                break;
            default:
                break;
        }

        superToast.show();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                x1 = ev.getX();
//                y1 = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                x2 = ev.getX();
//                y2 = ev.getY();
//                if (Math.abs(x1 - x2) > 20) {
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                x2 = ev.getX();
//                y2 = ev.getY();
//                if (Math.abs(x1 - x2) > 20) {
//                    return true;
//                }
//                break;
//            default:
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    protected void onDestroy() {
        for (int i = 0; i < 3; i++) {
            CoCoinFragmentManager.passwordChangeFragment[i].onDestroy();
            CoCoinFragmentManager.passwordChangeFragment[i] = null;
        }
        super.onDestroy();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
