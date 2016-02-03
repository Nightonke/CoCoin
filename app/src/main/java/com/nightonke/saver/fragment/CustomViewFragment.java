package com.nightonke.saver.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.melnykov.fab.FloatingActionButton;
import com.nightonke.saver.R;
import com.nightonke.saver.activity.CoCoinApplication;
import com.nightonke.saver.model.CoCoinRecord;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.util.CoCoinUtil;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.squareup.leakcanary.RefWatcher;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class CustomViewFragment extends Fragment {

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private Context mContext;

    private FloatingActionButton button;
    private ObservableScrollView mScrollView;

    private int fromYear;
    private int fromMonth;
    private int fromDay;

    private boolean isFrom;

    private TextView fromDate;
    private TextView expense;
    private TextView emptyTip;

    private Calendar from = Calendar.getInstance();
    private Calendar to = Calendar.getInstance();

    private SuperToast superToast;

    private int start = -1;
    private int end = -1;
    private int Sum = 0;

    private PieChartView pie;

    private MaterialIconView iconRight;
    private MaterialIconView iconLeft;
    private MaterialIconView all;

    private Calendar startDayCalendar;

    private boolean IS_EMPTY = false;

    // store the sum of expenses of each tag
    private Map<Integer, Double> TagExpanse;
    // store the records of each tag
    private Map<Integer, List<CoCoinRecord>> Expanse;
    // the original target value of the whole pie
    private float[] originalTargets;

    // the selected position of one part of the pie
    private int pieSelectedPosition = 0;
    // the last selected position of one part of the pie
    private int lastPieSelectedPosition = -1;

    // the date string on the footer and header
    private String dateString;
    // the date string shown in the dialog
    private String dateShownString;
    // the string shown in the dialog
    private String dialogTitle;

    // the selected tag in pie
    private int tagId = -1;

    public static CustomViewFragment newInstance() {
        CustomViewFragment fragment = new CustomViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        superToast = new SuperToast(mContext);
        superToast.setAnimations(SuperToast.Animations.POPUP);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);
        superToast.setBackground(SuperToast.Background.RED);
        superToast.getTextView().setTypeface(CoCoinUtil.typefaceLatoLight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IS_EMPTY = RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.isEmpty();

        mScrollView = (ObservableScrollView) view.findViewById(R.id.scrollView);

        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);

        fromDate = (TextView)view.findViewById(R.id.from_date);
        fromDate.setTypeface(CoCoinUtil.GetTypeface());

        expense = (TextView)view.findViewById(R.id.expense);
        expense.setTypeface(CoCoinUtil.typefaceLatoLight);
        expense.setText(CoCoinUtil.GetInMoney(0));

        pie = (PieChartView)view.findViewById(R.id.chart_pie);
        pie.setVisibility(View.INVISIBLE);

        iconRight = (MaterialIconView)view.findViewById(R.id.icon_right);
        iconLeft = (MaterialIconView)view.findViewById(R.id.icon_left);
        iconRight.setVisibility(View.INVISIBLE);
        iconLeft.setVisibility(View.INVISIBLE);

        all = (MaterialIconView)view.findViewById(R.id.all);
        all.setVisibility(View.INVISIBLE);

        emptyTip = (TextView)view.findViewById(R.id.empty_tip);
        emptyTip.setTypeface(CoCoinUtil.GetTypeface());

        if (IS_EMPTY) {
            emptyTip.setVisibility(View.GONE);
        }

        isFrom = true;

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                if (isFrom) {
                    fromYear = year;
                    fromMonth = monthOfYear + 1;
                    fromDay = dayOfMonth;
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            onDateSetListener,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setTitle(mContext.getResources().getString(R.string.set_right_calendar));
                    dpd.show(((Activity)mContext).getFragmentManager(), "Datepickerdialog");
                    isFrom = false;
                } else {
                    from.set(fromYear, fromMonth - 1, fromDay, 0, 0, 0);
                    from.add(Calendar.SECOND, 0);

                    to.set(year, monthOfYear, dayOfMonth, 23, 59, 59);
                    to.add(Calendar.SECOND, 0);

                    if (to.before(from)) {
                        superToast.setText(
                                mContext.getResources().getString(R.string.from_invalid));
                        superToast.setText(
                                mContext.getResources().getString(R.string.to_invalid));
                        SuperToast.cancelAllSuperToasts();
                        superToast.show();
                    } else {
                        fromDate.setText(" ● " +
                                mContext.getResources().getString(R.string.from) + " " +
                                CoCoinUtil.GetMonthShort(from.get(Calendar.MONTH) + 1)
                                + " " + from.get(Calendar.DAY_OF_MONTH) + CoCoinUtil.GetWhetherFuck() +
                                from.get(Calendar.YEAR) + " " +
                                mContext.getResources().getString(R.string.to) + " " +
                                CoCoinUtil.GetMonthShort(to.get(Calendar.MONTH) + 1)
                                + " " + to.get(Calendar.DAY_OF_MONTH)  + CoCoinUtil.GetWhetherFuck() +
                                to.get(Calendar.YEAR));
                        select();
                    }
                }
            }
        };

        button = (FloatingActionButton) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        onDateSetListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setTitle(mContext.getResources().getString(R.string.set_left_calendar));
                dpd.show(((Activity)mContext).getFragmentManager(), "Datepickerdialog");
                isFrom = true;
            }
        });

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        RefWatcher refWatcher = CoCoinApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    private void select() {

        if (RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS == null
                || RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.size() == 0) {
            return;
        }

        start = -1;
        end = 0;
        Sum = 0;
        lastPieSelectedPosition = -1;

        if (from.after(RecordManager.RECORDS.get(RecordManager.RECORDS.size() - 1).getCalendar())) {
            return;
        }
        if (to.before(RecordManager.RECORDS.get(0).getCalendar())) {
            return;
        }

        for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
            if (RecordManager.RECORDS.get(i).getCalendar().before(from)) {
                end = i + 1;
                break;
            } else if (RecordManager.RECORDS.get(i).getCalendar().before(to)) {
                if (start == -1) {
                    start = i;
                }
            }
        }

        startDayCalendar = (Calendar)from.clone();
        startDayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startDayCalendar.set(Calendar.MINUTE, 0);
        startDayCalendar.set(Calendar.SECOND, 0);
        final long startDay = TimeUnit.MILLISECONDS.toDays(startDayCalendar.getTimeInMillis());
        final long days = TimeUnit.MILLISECONDS.toDays(to.getTimeInMillis()) - startDay + 1;

        TagExpanse = new TreeMap<>();
        Expanse = new HashMap<>();
        originalTargets = new float[(int)days];

        int size = RecordManager.TAGS.size();
        for (int j = 2; j < size; j++) {
            TagExpanse.put(RecordManager.TAGS.get(j).getId(), Double.valueOf(0));
            Expanse.put(RecordManager.TAGS.get(j).getId(), new ArrayList<CoCoinRecord>());
        }

        for (int i = start; i >= end; i--) {
            CoCoinRecord coCoinRecord = RecordManager.RECORDS.get(i);
            TagExpanse.put(coCoinRecord.getTag(),
                    TagExpanse.get(coCoinRecord.getTag()) + Double.valueOf(coCoinRecord.getMoney()));
            Expanse.get(coCoinRecord.getTag()).add(coCoinRecord);
            Sum += coCoinRecord.getMoney();
            originalTargets[(int)(TimeUnit.MILLISECONDS.toDays(
                    coCoinRecord.getCalendar().getTimeInMillis()) - startDay)] += coCoinRecord.getMoney();
        }

        expense.setText(CoCoinUtil.GetInMoney(Sum));
        emptyTip.setVisibility(View.GONE);

        TagExpanse = CoCoinUtil.SortTreeMapByValues(TagExpanse);

        final ArrayList<SliceValue> sliceValues = new ArrayList<>();

        for (Map.Entry<Integer, Double> entry : TagExpanse.entrySet()) {
            if (entry.getValue() >= 1) {
                SliceValue sliceValue = new SliceValue(
                        (float)(double)entry.getValue(),
                        CoCoinUtil.GetTagColor(entry.getKey()));
                sliceValue.setLabel(String.valueOf(entry.getKey()));
                sliceValues.add(sliceValue);
            }
        }

        final PieChartData pieChartData = new PieChartData(sliceValues);

        pieChartData.setHasLabels(false);
        pieChartData.setHasLabelsOnlyForSelected(false);
        pieChartData.setHasLabelsOutside(false);
        pieChartData.setHasCenterCircle(SettingManager.getInstance().getIsHollow());

        pie.setPieChartData(pieChartData);
        pie.setChartRotationEnabled(false);

        pie.setVisibility(View.VISIBLE);

        iconRight.setVisibility(View.VISIBLE);
        iconRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPieSelectedPosition != -1) {
                    pieSelectedPosition = lastPieSelectedPosition;
                }
                pieSelectedPosition
                        = (pieSelectedPosition - 1 + sliceValues.size())
                        % sliceValues.size();
                SelectedValue selectedValue =
                        new SelectedValue(
                                pieSelectedPosition,
                                0,
                                SelectedValue.SelectedValueType.NONE);
                pie.selectValue(selectedValue);
            }
        });
        iconLeft.setVisibility(View.VISIBLE);
        iconLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPieSelectedPosition != -1) {
                    pieSelectedPosition = lastPieSelectedPosition;
                }
                pieSelectedPosition
                        = (pieSelectedPosition + 1)
                        % sliceValues.size();
                SelectedValue selectedValue =
                        new SelectedValue(
                                pieSelectedPosition,
                                0,
                                SelectedValue.SelectedValueType.NONE);
                pie.selectValue(selectedValue);
            }
        });

// set value touch listener of pie//////////////////////////////////////////////////////////////////

        dateShownString = mContext.getResources().getString(R.string.from) + " " +
                CoCoinUtil.GetMonthShort(from.get(Calendar.MONTH) + 1) + " " +
                from.get(Calendar.DAY_OF_MONTH) + " " +
                from.get(Calendar.YEAR) + " " +
                mContext.getResources().getString(R.string.to) + " " +
                CoCoinUtil.GetMonthShort(to.get(Calendar.MONTH) + 1) + " " +
                to.get(Calendar.DAY_OF_MONTH) + " " +
                to.get(Calendar.YEAR);

        pie.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int p, SliceValue sliceValue) {
                // snack bar
                String text;
                tagId = Integer.valueOf(String.valueOf(sliceValue.getLabelAsChars()));
                double percent = sliceValue.getValue() / Sum * 100;
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    text = CoCoinUtil.GetSpendString((int) sliceValue.getValue()) +
                            CoCoinUtil.GetPercentString(percent) + "\n" +
                            "于" + CoCoinUtil.GetTagName(tagId);
                } else {
                    text = CoCoinUtil.GetSpendString((int) sliceValue.getValue())
                            + " (takes " + String.format("%.2f", percent) + "%)\n"
                            + "in " + CoCoinUtil.GetTagName(tagId);
                }
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    dialogTitle = dateShownString + "\n" +
                            CoCoinUtil.GetSpendString((int) sliceValue.getValue()) + " " +
                            "于" + CoCoinUtil.GetTagName(tagId);
                } else {
                    dialogTitle = CoCoinUtil.GetSpendString((int) sliceValue.getValue()) + " " +
                            mContext.getResources().getString(R.string.from) + " " +
                            CoCoinUtil.GetMonthShort(from.get(Calendar.MONTH) + 1) + " " +
                            from.get(Calendar.DAY_OF_MONTH) + " " +
                            from.get(Calendar.YEAR) + "\n" +
                            mContext.getResources().getString(R.string.to) + " " +
                            CoCoinUtil.GetMonthShort(to.get(Calendar.MONTH) + 1) + " " +
                            to.get(Calendar.DAY_OF_MONTH) + " " +
                            to.get(Calendar.YEAR) + " " +
                            "in " + CoCoinUtil.GetTagName(tagId);
                }
                Snackbar snackbar =
                        Snackbar
                                .with(mContext)
                                .type(SnackbarType.MULTI_LINE)
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .position(Snackbar.SnackbarPosition.BOTTOM)
                                .margin(15, 15)
                                .backgroundDrawable(CoCoinUtil.GetSnackBarBackground(-3))
                                .text(text)
                                .textTypeface(CoCoinUtil.GetTypeface())
                                .textColor(Color.WHITE)
                                .actionLabelTypeface(CoCoinUtil.GetTypeface())
                                .actionLabel(mContext.getResources()
                                        .getString(R.string.check))
                                .actionColor(Color.WHITE)
                                .actionListener(new mActionClickListenerForPie());
                SnackbarManager.show(snackbar);

                if (p == lastPieSelectedPosition) {
                    return;
                } else {
                    lastPieSelectedPosition = p;
                }
            }

            @Override
            public void onValueDeselected() {

            }
        });

        all.setVisibility(View.VISIBLE);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CoCoinRecord> data = new LinkedList<CoCoinRecord>();
                for (int i = start; i >= end; i--) data.add(RecordManager.RECORDS.get(i));
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    dialogTitle = dateShownString + "\n" +
                            CoCoinUtil.GetSpendString(Sum) +
                            "于" + CoCoinUtil.GetTagName(tagId);
                } else {
                    dialogTitle = CoCoinUtil.GetSpendString(Sum) + " "
                            + mContext.getResources().getString(R.string.from) + " " +
                            CoCoinUtil.GetMonthShort(from.get(Calendar.MONTH) + 1) + " " +
                            from.get(Calendar.DAY_OF_MONTH) + " " +
                            from.get(Calendar.YEAR) + "\n" +
                            mContext.getResources().getString(R.string.to) + " " +
                            CoCoinUtil.GetMonthShort(to.get(Calendar.MONTH) + 1) + " " +
                            to.get(Calendar.DAY_OF_MONTH) + " " +
                            to.get(Calendar.YEAR) + " " +
                            "in " + CoCoinUtil.GetTagName(tagId);
                }
                ((FragmentActivity)mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .add(new RecordCheckDialogFragment(
                                mContext, data, dialogTitle), "MyDialog")
                        .commit();
            }
        });
    }

    private class mActionClickListenerForPie implements ActionClickListener {
        @Override
        public void onActionClicked(Snackbar snackbar) {
            List<CoCoinRecord> shownCoCoinRecords = Expanse.get(tagId);
            ((FragmentActivity)mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .add(new RecordCheckDialogFragment(
                            mContext, shownCoCoinRecords, dialogTitle), "MyDialog")
                    .commit();
        }
    }
}
