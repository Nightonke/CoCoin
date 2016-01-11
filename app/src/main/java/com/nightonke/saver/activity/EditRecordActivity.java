package com.nightonke.saver.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.nightonke.saver.R;
import com.nightonke.saver.adapter.ButtonGridViewAdapter;
import com.nightonke.saver.fragment.EditRecordFragment;
import com.nightonke.saver.fragment.TagChooseFragment;
import com.nightonke.saver.model.Record;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.ui.CoCoinViewPager;
import com.nightonke.saver.ui.MyGridView;
import com.nightonke.saver.util.Util;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import net.steamcrafted.materialiconlib.MaterialIconView;

public class EditRecordActivity extends AppCompatActivity
        implements TagChooseFragment.OnTagItemSelectedListener {

    private Context mContext;
    private boolean IS_CHANGED = false;
    private boolean FIRST_EDIT = true;
    private int position = -1;

    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;
    private FragmentPagerItemAdapter tagChoicePagerAdapter;

    private ViewPager editViewPager;
    private SmartTabLayout editSmartTabLayout;
    private FragmentPagerItemAdapter editPagerAdapter;

    private MyGridView myGridView;
    private ButtonGridViewAdapter myGridViewAdapter;

    private final int NO_TAG_TOAST = 0;
    private final int NO_MONEY_TOAST = 1;
    private final int SAVE_SUCCESSFULLY_TOAST = 4;
    private final int SAVE_FAILED_TOAST = 5;

    private SuperToast superToast;

    private MaterialIconView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        mContext = this;

        superToast = new SuperToast(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("POSITION");
        }

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

// edit viewpager///////////////////////////////////////////////////////////////////////////////////
        editViewPager = (CoCoinViewPager)findViewById(R.id.edit_pager);
        editSmartTabLayout = (SmartTabLayout)findViewById(R.id.edit_viewpager_tab);
        FragmentPagerItems editPagers = new FragmentPagerItems(this);
        for (int i = 0; i < 2; i++) {
            editPagers.add(FragmentPagerItem.of("1", EditRecordFragment.class));
        }

        editPagerAdapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), editPagers);
        editViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1) {
                    ((EditRecordFragment) editPagerAdapter.getPage(1)).editRequestFocus();
                } else {
                    ((EditRecordFragment) editPagerAdapter.getPage(0)).editRequestFocus();
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

        editSmartTabLayout.setViewPager(editViewPager);
        editSmartTabLayout.setVisibility(View.GONE);

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
                    }
                });

        Util.editRecordPosition = RecordManager.RECORDS.size() - 1 - position;

        back = (MaterialIconView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("IS_CHANGED", IS_CHANGED);
        intent.putExtra("POSITION", position);
        setResult(RESULT_OK, intent);

        Util.editRecordPosition = -1;

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
        if (IS_CHANGED) {
            return;
        }
        if (((EditRecordFragment) editPagerAdapter.getPage(0)).getNumberText().toString().equals("0")
                && !Util.ClickButtonCommit(position)) {
            if (Util.ClickButtonDelete(position)
                    || Util.ClickButtonIsZero(position)) {

            } else {
                ((EditRecordFragment) editPagerAdapter.getPage(0)).setNumberText(Util.BUTTONS[position]);
            }
        } else {
            if (Util.ClickButtonDelete(position)) {
                if (longClick) {
                    ((EditRecordFragment) editPagerAdapter.getPage(0)).setNumberText("0");
                    ((EditRecordFragment) editPagerAdapter.getPage(0)).setHelpText(" ");
                    ((EditRecordFragment) editPagerAdapter.getPage(0)).setHelpText(
                            Util.FLOATINGLABELS[((EditRecordFragment) editPagerAdapter.getPage(0))
                                    .getNumberText().toString().length()]);
                } else {
                    ((EditRecordFragment) editPagerAdapter.getPage(0)).setNumberText(
                            ((EditRecordFragment) editPagerAdapter.getPage(0)).getNumberText().toString()
                            .substring(0, ((EditRecordFragment) editPagerAdapter.getPage(0))
                                    .getNumberText().toString().length() - 1));
                    if (((EditRecordFragment) editPagerAdapter.getPage(0)).getNumberText().toString().length() == 0) {
                        ((EditRecordFragment) editPagerAdapter.getPage(0)).setNumberText("0");
                        ((EditRecordFragment) editPagerAdapter.getPage(0)).setHelpText(" ");
                    }
                }
            } else if (Util.ClickButtonCommit(position)) {
                commit();
            } else {
                if (FIRST_EDIT) {
                    ((EditRecordFragment) editPagerAdapter.getPage(0)).setNumberText(Util.BUTTONS[position]);
                    FIRST_EDIT = false;
                } else {
                    ((EditRecordFragment) editPagerAdapter.getPage(0))
                            .setNumberText(((EditRecordFragment) editPagerAdapter.getPage(0))
                                    .getNumberText().toString() + Util.BUTTONS[position]);
                }
            }
        }
        ((EditRecordFragment) editPagerAdapter.getPage(0)).setHelpText(Util.FLOATINGLABELS[
                ((EditRecordFragment) editPagerAdapter.getPage(0)).getNumberText().toString().length()]);
    }

    private void commit() {
        if (((EditRecordFragment)editPagerAdapter.getPage(0)).getTagId() == -1) {
            showToast(NO_TAG_TOAST);
        } else if (((EditRecordFragment)editPagerAdapter.getPage(0)).getNumberText().toString().equals("0")) {
            showToast(NO_MONEY_TOAST);
        } else  {
            Record record = new Record();
            record.set(RecordManager.RECORDS.get(RecordManager.RECORDS.size() - 1 - position));
            record.setMoney(Float.valueOf(((EditRecordFragment) editPagerAdapter.getPage(0)).getNumberText().toString()));
            record.setTag(((EditRecordFragment) editPagerAdapter.getPage(0)).getTagId());
            record.setRemark(((EditRecordFragment)editPagerAdapter.getPage(1)).getRemark());
            long updateId = RecordManager.updateRecord(record);
            if (updateId == -1) {
                if (!superToast.isShowing()) {
                    showToast(SAVE_FAILED_TOAST);
                }
            } else {
                IS_CHANGED = true;
                onBackPressed();
            }
        }
    }

    private void showToast(int toastType) {
        SuperToast.cancelAllSuperToasts();
        SuperActivityToast.cancelAllSuperActivityToasts();

        superToast.setAnimations(Util.TOAST_ANIMATION);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);

        switch (toastType) {
            case NO_MONEY_TOAST:

                superToast.setText(mContext.getResources().getString(R.string.toast_no_money));
                superToast.setBackground(SuperToast.Background.BLUE);
                superToast.getTextView().setTypeface(Util.typefaceLatoLight);

                break;
            case SAVE_SUCCESSFULLY_TOAST:

                superToast.setText(
                        mContext.getResources().getString(R.string.toast_save_successfully));
                superToast.setBackground(SuperToast.Background.GREEN);
                superToast.getTextView().setTypeface(Util.typefaceLatoLight);

                break;
            case SAVE_FAILED_TOAST:

                superToast.setText(mContext.getResources().getString(R.string.toast_save_failed));
                superToast.setBackground(SuperToast.Background.RED);
                superToast.getTextView().setTypeface(Util.typefaceLatoLight);

                break;
            default:

                break;
        }

        superToast.show();
    }

    @Override
    public void onTagItemPicked(int position) {
        ((EditRecordFragment)editPagerAdapter.getPage(0)).setTag(viewPager.getCurrentItem() * 8 + position + 2);
    }
}
