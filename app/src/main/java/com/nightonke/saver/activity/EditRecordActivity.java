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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.nightonke.saver.R;
import com.nightonke.saver.adapter.ButtonGridViewAdapter;
import com.nightonke.saver.fragment.TagChooseFragment;
import com.nightonke.saver.model.Record;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.ui.MyGridView;
import com.nightonke.saver.util.Util;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.rengwuxian.materialedittext.MaterialEditText;

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

    private MyGridView myGridView;
    private ButtonGridViewAdapter myGridViewAdapter;

    private MaterialEditText editView;

    private final int NO_TAG_TOAST = 0;
    private final int NO_MONEY_TOAST = 1;
    private final int SAVE_SUCCESSFULLY_TOAST = 4;
    private final int SAVE_FAILED_TOAST = 5;

    public int tagId;
    public TextView tagName;
    public ImageView tagImage;

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

        editView = (MaterialEditText)findViewById(R.id.edit_view);
        editView.setTypeface(Util.typefaceLatoHairline);
        editView.setText("" + (int) RecordManager.RECORDS.get(
                RecordManager.RECORDS.size() - 1 - position).getMoney());
        editView.requestFocus();
        editView.setHelperText(" ");

        tagName = (TextView)findViewById(R.id.tag_name);
        tagName.setTypeface(Util.typefaceLatoLight);
        tagImage = (ImageView)findViewById(R.id.tag_image);

        tagId = RecordManager.RECORDS.get(
                RecordManager.RECORDS.size() - 1 - position).getTag();
        tagName.setText(Util.GetTagName(tagId));
        tagImage.setImageResource(Util.GetTagIcon(tagId));

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
        if (editView.getText().toString().equals("0")
                && !Util.ClickButtonCommit(position)) {
            if (Util.ClickButtonDelete(position)
                    || Util.ClickButtonIsZero(position)) {

            } else {
                editView.setText(Util.BUTTONS[position]);
            }
        } else {
            if (Util.ClickButtonDelete(position)) {
                if (longClick) {
                    editView.setText("0");
                    editView.setHelperText(" ");
                    editView.setHelperText(
                            Util.FLOATINGLABELS[editView.getText().toString().length()]);
                } else {
                    editView.setText(editView.getText().toString()
                            .substring(0, editView.getText().toString().length() - 1));
                    if (editView.getText().toString().length() == 0) {
                        editView.setText("0");
                        editView.setHelperText(" ");
                    }
                }
            } else if (Util.ClickButtonCommit(position)) {
                commit();
            } else {
                if (FIRST_EDIT) {
                    editView.setText(Util.BUTTONS[position]);
                    FIRST_EDIT = false;
                } else {
                    editView.setText(editView.getText().toString() + Util.BUTTONS[position]);
                }
            }
        }
        editView.setHelperText(Util.FLOATINGLABELS[editView.getText().toString().length()]);
    }

    private void commit() {
        if (tagName.getText().equals("")) {
            showToast(NO_TAG_TOAST);
        } else if (editView.getText().toString().equals("0")) {
            showToast(NO_MONEY_TOAST);
        } else  {
            Record record = new Record();
            record.set(RecordManager.RECORDS.get(RecordManager.RECORDS.size() - 1 - position));
            record.setMoney(Float.valueOf(editView.getText().toString()));
            record.setTag(tagId);
            long updateId = RecordManager.updateRecord(record);
            if (updateId == -1) {
                if (!superToast.isShowing()) {
                    showToast(SAVE_FAILED_TOAST);
                }
            } else {
                IS_CHANGED = true;
                if (!superToast.isShowing()) {
                    showToast(SAVE_SUCCESSFULLY_TOAST);
                }
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
        tagId = RecordManager.TAGS.
                get(viewPager.getCurrentItem() * 8 + position + 2).getId();
        tagName.setText(
                Util.GetTagName(
                        RecordManager.TAGS.get(
                                viewPager.getCurrentItem() * 8 + position + 2).getId()));
        tagImage.setImageResource(
                Util.GetTagIcon(
                        RecordManager.TAGS.
                                get(viewPager.getCurrentItem() * 8 + position + 2).getId()));
    }
}
