package com.nightonke.saver;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

// Todo add a dialog of histogram columns

/**
 * Created by 伟平 on 2015/10/20.
 */

public class TodayViewRecyclerViewAdapter
        extends RecyclerView.Adapter<TodayViewRecyclerViewAdapter.viewHolder> {

    private Context mContext;

    private List<Record> list;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    static final int TODAY = 0;
    static final int YESTERDAY = 1;
    static final int THIS_WEEK = 2;
    static final int LAST_WEEK = 3;
    static final int THIS_MONTH = 4;
    static final int LAST_MONTH = 5;
    static final int THIS_YEAR = 6;
    static final int LAST_YEAR = 7;

    private int fragmentPosition;

    private Map<Integer, Double> TagExpanse;
    private Map<Integer, List<Record>> Expanse;
    private float[] originalTargets;

    private boolean IS_EMPTY;

    private double Sum;
    private int selectedPosition = 0;
    private String dateString;
    private String dateShownString;
    private String dialogTitle;

    private int tagId = -1;
    private int lastSelectedPosition = -1;
    private int columnNumber;
    private int axis_date;

    private int timeIndex;
    private int lastHistogramSelectedPosition = -1;

    public TodayViewRecyclerViewAdapter(int start, int end, Context context, int position) {

        mContext = context;
        fragmentPosition = position;
        Sum = 0;

        RecordManager recordManager = RecordManager.getInstance(mContext.getApplicationContext());

        list = new ArrayList<>();
        if (start != -1) {
            Log.d("Saver", start + " " + end);
            for (int i = start; i >= end; i--) {
                list.add(recordManager.RECORDS.get(i));
            }
        }

        IS_EMPTY = list.isEmpty();

        setDateString();

        if (!IS_EMPTY) {
            if (fragmentPosition == TODAY || fragmentPosition == YESTERDAY) {
                columnNumber = 24;
                axis_date = Calendar.HOUR_OF_DAY;
            }
            if (fragmentPosition == THIS_WEEK || fragmentPosition == LAST_WEEK) {
                columnNumber = 7;
                axis_date = Calendar.DAY_OF_WEEK;
            }
            if (fragmentPosition == THIS_MONTH || fragmentPosition == LAST_MONTH) {
                columnNumber = list.get(0).getCalendar().getActualMaximum(Calendar.DAY_OF_MONTH);
                axis_date = Calendar.DAY_OF_MONTH;
            }
            if (fragmentPosition == THIS_YEAR || fragmentPosition == LAST_YEAR) {
                columnNumber = 12;
                axis_date = Calendar.MONTH;
            }

            TagExpanse = new TreeMap<>();
            Expanse = new HashMap<>();
            originalTargets = new float[columnNumber];
            for (int i = 0; i < columnNumber; i++) originalTargets[i] = 0;

            for (int j = 2; j < recordManager.TAGS.size(); j++) {
                TagExpanse.put(recordManager.TAGS.get(j).getId(), Double.valueOf(0));
                Expanse.put(recordManager.TAGS.get(j).getId(), new ArrayList<Record>());
            }

            for (Record record : list) {
                TagExpanse.put(record.getTag(),
                        TagExpanse.get(record.getTag()) + Double.valueOf(record.getMoney()));
                Expanse.get(record.getTag()).add(record);
                Sum += record.getMoney();
                if (axis_date == Calendar.DAY_OF_WEEK) {
                    originalTargets[record.getCalendar().get(axis_date) - 1]
                            += record.getMoney();
                } else {
                    originalTargets[record.getCalendar().get(axis_date)]
                            += record.getMoney();
                }
            }

            TagExpanse = Utils.SortTreeMapByValues(TagExpanse);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public TodayViewRecyclerViewAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.today_list_view_head, parent, false);
                return new viewHolder(view) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.today_list_view_body, parent, false);
                return new viewHolder(view) {
                };
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        switch (getItemViewType(position)) {
            case TYPE_HEADER:

                holder.date.setText(dateString);
                holder.dateBottom.setText(dateString);
                holder.expanseSum.setText(String.valueOf((int) Sum));

                holder.date.setTypeface(Utils.typefaceLatoLight);
                holder.dateBottom.setTypeface(Utils.GetTypeface());
                holder.expanseSum.setTypeface(Utils.typefaceLatoLight);

                if (IS_EMPTY) {
                    holder.emptyTip.setVisibility(View.VISIBLE);
                    holder.emptyTip.setTypeface(Utils.typefaceLatoLight);

                    holder.pie.setVisibility(View.GONE);
                    holder.iconLeft.setVisibility(View.GONE);
                    holder.iconRight.setVisibility(View.GONE);

                    holder.histogram.setVisibility(View.GONE);
                    holder.histogram_icon_left.setVisibility(View.GONE);
                    holder.histogram_icon_right.setVisibility(View.GONE);
                } else {
                    holder.emptyTip.setVisibility(View.INVISIBLE);

                    final ArrayList<SliceValue> sliceValues = new ArrayList<>();

                    for (Map.Entry<Integer, Double> entry : TagExpanse.entrySet()) {
                        if (entry.getValue() >= 1) {
                            SliceValue sliceValue = new SliceValue(
                                    (float)(double)entry.getValue(),
                                    mContext.getApplicationContext().getResources().
                                            getColor(Utils.GetTagColor(entry.getKey())));
                            sliceValue.setLabel(String.valueOf(entry.getKey()));
                            sliceValues.add(sliceValue);
                        }
                    }

                    PieChartData pieChartData = new PieChartData(sliceValues);

                    pieChartData.setHasLabels(false);
                    pieChartData.setHasLabelsOnlyForSelected(false);
                    pieChartData.setHasLabelsOutside(false);
                    pieChartData.setHasCenterCircle(false);

                    holder.pie.setPieChartData(pieChartData);
                    holder.pie.setChartRotationEnabled(false);

                    holder.iconRight.setVisibility(View.VISIBLE);
                    holder.iconRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (lastSelectedPosition != -1) {
                                selectedPosition = lastSelectedPosition;
                            }
                            selectedPosition
                                    = (selectedPosition - 1 + sliceValues.size())
                                    % sliceValues.size();
                            SelectedValue selectedValue =
                                    new SelectedValue(
                                            selectedPosition,
                                            0,
                                            SelectedValue.SelectedValueType.NONE);
                            holder.pie.selectValue(selectedValue);
                        }
                    });
                    holder.iconLeft.setVisibility(View.VISIBLE);
                    holder.iconLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (lastSelectedPosition != -1) {
                                selectedPosition = lastSelectedPosition;
                            }
                            selectedPosition
                                    = (selectedPosition + 1)
                                    % sliceValues.size();
                            SelectedValue selectedValue =
                                    new SelectedValue(
                                            selectedPosition,
                                            0,
                                            SelectedValue.SelectedValueType.NONE);
                            holder.pie.selectValue(selectedValue);
                        }
                    });

                    // numColumns of zeros of the histogram
                    final List<Column> columns = new ArrayList<>();
                    for (int i = 0; i < columnNumber; i++) {
                        if (lastHistogramSelectedPosition == -1 && originalTargets[i] == 0) {
                            lastHistogramSelectedPosition = i;
                        }
                        SubcolumnValue value = new SubcolumnValue(
                                originalTargets[i], Utils.GetRandomColor());
                        List<SubcolumnValue> subcolumnValues = new ArrayList<>();
                        subcolumnValues.add(value);
                        Column column = new Column(subcolumnValues);
                        column.setHasLabels(false);
                        column.setHasLabelsOnlyForSelected(false);
                        columns.add(column);
                    }

                    Axis axisX = new Axis();
                    List<AxisValue> axisValueList = new ArrayList<>();

                    for (int i = 0; i < columnNumber; i++) {
                        axisValueList.add(
                                new AxisValue(i).setLabel(Utils.GetAxisDateName(axis_date, i)));
                    }

                    axisX.setValues(axisValueList);
                    Axis axisY = new Axis().setHasLines(true);

                    final ColumnChartData columnChartData = new ColumnChartData(columns);

                    columnChartData.setAxisXBottom(axisX);
                    columnChartData.setAxisYLeft(axisY);
                    columnChartData.setStacked(true);

                    holder.histogram.setColumnChartData(columnChartData);
                    holder.histogram.setZoomEnabled(false);

                    holder.histogram_icon_left.setVisibility(View.VISIBLE);
                    holder.histogram_icon_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            do {
                                lastHistogramSelectedPosition
                                        = (lastHistogramSelectedPosition - 1 + columnNumber)
                                        % columnNumber;
                            } while (columnChartData.getColumns()
                                    .get(lastHistogramSelectedPosition)
                                    .getValues().get(0).getValue() == 0);
                            SelectedValue selectedValue =
                                    new SelectedValue(
                                            lastHistogramSelectedPosition,
                                            0,
                                            SelectedValue.SelectedValueType.NONE);
                            holder.histogram.selectValue(selectedValue);
                        }
                    });
                    holder.histogram_icon_right.setVisibility(View.VISIBLE);
                    holder.histogram_icon_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            do {
                                lastHistogramSelectedPosition
                                        = (lastHistogramSelectedPosition + 1)
                                        % columnNumber;
                            } while (columnChartData.getColumns()
                                    .get(lastHistogramSelectedPosition)
                                    .getValues().get(0).getValue() == 0);
                            SelectedValue selectedValue =
                                    new SelectedValue(
                                            lastHistogramSelectedPosition,
                                            0,
                                            SelectedValue.SelectedValueType.NONE);
                            holder.histogram.selectValue(selectedValue);
                        }
                    });

                    // on click listener
                    holder.pie.setOnValueTouchListener(new PieChartOnValueSelectListener() {
                        @Override
                        public void onValueSelected(int p, SliceValue sliceValue) {
                            // snack bar
                            RecordManager recordManager
                                    = RecordManager.getInstance(mContext.getApplicationContext());
                            String text = "";
                            tagId = Integer.valueOf(String.valueOf(sliceValue.getLabelAsChars()));
                            Double percent = sliceValue.getValue() / Sum * 100;
                            text += "Spend " + (int)sliceValue.getValue()
                                    + " (takes " + String.format("%.2f", percent) + "%)\n"
                                    + "in " + recordManager.TAG_NAMES.get(tagId) + ".\n";
                            dialogTitle = "Spend " + (int)sliceValue.getValue() + dateShownString + "\n" +
                                    "in " + recordManager.TAG_NAMES.get(tagId);
                            Snackbar snackbar =
                                    Snackbar
                                            .with(mContext)
                                            .type(SnackbarType.MULTI_LINE)
                                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                            .position(Snackbar.SnackbarPosition.BOTTOM)
                                            .margin(15, 15)
                                            .backgroundDrawable(Utils.GetSnackBarBackground(fragmentPosition))
                                            .text(text)
                                            .textTypeface(Utils.typefaceLatoLight)
                                            .textColor(Color.WHITE)
                                            .actionLabelTypeface(Utils.typefaceLatoLight)
                                            .actionLabel("Check")
                                            .actionColor(Color.WHITE)
                                            .actionListener(new mActionClickListener());
                            SnackbarManager.show(snackbar);

                            if (p == lastSelectedPosition) {
                                return;
                            } else {
                                lastSelectedPosition = p;
                            }

                            // histogram data
                            float[] targets = new float[columnNumber];
                            for (int i = 0; i < columnNumber; i++) targets[i] = 0;

                            for (int i = Expanse.get(tagId).size() - 1; i >= 0; i--) {
                                Record record = Expanse.get(tagId).get(i);
                                if (axis_date == Calendar.DAY_OF_WEEK) {
                                    targets[record.getCalendar().get(axis_date) - 1]
                                            += record.getMoney();
                                } else {
                                    targets[record.getCalendar().get(axis_date)]
                                            += record.getMoney();
                                }
                            }

                            lastHistogramSelectedPosition = -1;
                            for (int i = 0; i < columnNumber; i++) {
                                if (lastHistogramSelectedPosition == -1 && targets[i] != 0) {
                                    lastHistogramSelectedPosition = i;
                                }
                                columnChartData.getColumns().
                                        get(i).getValues().get(0).setTarget(targets[i]);
                            }
                            holder.histogram.startDataAnimation();
                        }

                        @Override
                        public void onValueDeselected() {

                        }
                    });

                    holder.histogram.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
                        @Override
                        public void onValueSelected(int columnIndex,
                                                    int subcolumnIndex, SubcolumnValue value) {
                            lastHistogramSelectedPosition = columnIndex;
                            timeIndex = columnIndex;
                            // snack bar
                            RecordManager recordManager
                                    = RecordManager.getInstance(mContext.getApplicationContext());
                            String text = "Spend " + (int)value.getValue() + " ";
                            if (tagId != -1) {
                                // belongs a tag
                                text += getSnackBarDateString() + "\n"
                                        + "in " + recordManager.TAG_NAMES.get(tagId) + ".\n";
                            } else {
                                text += "\n" + getSnackBarDateString();
                            }

                            dialogTitle = text;
                            Snackbar snackbar =
                                    Snackbar
                                            .with(mContext)
                                            .type(SnackbarType.MULTI_LINE)
                                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                            .position(Snackbar.SnackbarPosition.BOTTOM)
                                            .margin(15, 15)
                                            .backgroundDrawable(Utils.GetSnackBarBackground(fragmentPosition))
                                            .text(text)
                                            .textTypeface(Utils.typefaceLatoLight)
                                            .textColor(Color.WHITE)
                                            .actionLabelTypeface(Utils.typefaceLatoLight)
                                            .actionLabel("Check")
                                            .actionColor(Color.WHITE)
                                            .actionListener(new mActionClickListenerForHistogram());
                            SnackbarManager.show(snackbar);
                        }

                        @Override
                        public void onValueDeselected() {

                        }
                    });

                    holder.reset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tagId = -1;
                            lastHistogramSelectedPosition = -1;
                            for (int i = 0; i < columnNumber; i++) {
                                if (lastHistogramSelectedPosition == -1
                                        && originalTargets[i] != 0) {
                                    lastHistogramSelectedPosition = i;
                                }
                                columnChartData.getColumns().
                                        get(i).getValues().get(0).setTarget(originalTargets[i]);
                            }
                            holder.histogram.startDataAnimation();
                        }
                    });

                }

                break;

            case TYPE_CELL:
                break;
        }
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        @Optional
        @InjectView(R.id.date)
        TextView date;
        @Optional
        @InjectView(R.id.date_bottom)
        TextView dateBottom;
        @Optional
        @InjectView(R.id.expanse)
        TextView expanseSum;
        @Optional
        @InjectView(R.id.empty_tip)
        TextView emptyTip;
        @Optional
        @InjectView(R.id.chart_pie)
        PieChartView pie;
        @Optional
        @InjectView(R.id.histogram)
        ColumnChartView histogram;
        @Optional
        @InjectView(R.id.icon_left)
        MaterialIconView iconLeft;
        @Optional
        @InjectView(R.id.icon_right)
        MaterialIconView iconRight;
        @Optional
        @InjectView(R.id.histogram_icon_left)
        MaterialIconView histogram_icon_left;
        @Optional
        @InjectView(R.id.histogram_icon_right)
        MaterialIconView histogram_icon_right;
        @Optional
        @InjectView(R.id.icon_reset)
        MaterialIconView reset;

        viewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private class mActionClickListener implements ActionClickListener {
        @Override
        public void onActionClicked(Snackbar snackbar) {
            List<Record> shownRecords = Expanse.get(tagId);
            ((FragmentActivity)mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .add(new RecordCheckDialog(
                            mContext, shownRecords, dialogTitle), "MyDialog")
                    .commit();
        }
    }

    private class mActionClickListenerForHistogram implements ActionClickListener {
        @Override
        public void onActionClicked(Snackbar snackbar) {
            List<Record> shownRecords = Expanse.get(tagId);
            ((FragmentActivity)mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .add(new RecordCheckDialog(
                            mContext, shownRecords, dialogTitle), "MyDialog")
                    .commit();
        }
    }

    private String getSnackBarDateString() {
        switch (fragmentPosition) {
            case TODAY:
                return "at " + timeIndex + " o'clock today";
            case YESTERDAY:
                return "at " + timeIndex + " o'clock yesterday";
            case THIS_WEEK:
                return "on " + Utils.GetWeekDay(timeIndex);
            case LAST_WEEK:
                return "last " + Utils.GetWeekDay(timeIndex);
            case THIS_MONTH:
                return "on " + (timeIndex + 1) + " " + dateString.substring(0, 3);
            case LAST_MONTH:
                return "on " + (timeIndex + 1) + " " + dateString.substring(0, 3);
            case THIS_YEAR:
                return "in " + Utils.MONTHS_SHORT[timeIndex + 1] + " this year";
            case LAST_YEAR:
                return "in " + Utils.MONTHS_SHORT[timeIndex + 1] + " last year";
            default:
                return "";
        }
    }

    private void setDateString() {
        String basicTodayDateString;
        String basicYesterdayDateString;
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Utils.GetYesterdayLeftRange(today);
        basicTodayDateString = "--:-- ";
        basicTodayDateString += Utils.MONTHS_SHORT[today.get(Calendar.MONTH) + 1] + " " +
                today.get(Calendar.DAY_OF_MONTH) + " " +
                today.get(Calendar.YEAR);
        basicYesterdayDateString = "--:-- ";
        basicYesterdayDateString += Utils.MONTHS_SHORT[yesterday.get(Calendar.MONTH) + 1] + " " +
                yesterday.get(Calendar.DAY_OF_MONTH) + " " +
                yesterday.get(Calendar.YEAR);
        switch (fragmentPosition) {
            case TODAY:
                dateString = basicTodayDateString.substring(6, basicTodayDateString.length());
                dateShownString = " today";
                break;
            case YESTERDAY:
                dateString = basicYesterdayDateString.substring(6, basicYesterdayDateString.length());
                dateShownString = " yesterday";
                break;
            case THIS_WEEK:
                Calendar leftWeekRange = Utils.GetThisWeekLeftRange(today);
                Calendar rightWeekRange = Utils.GetThisWeekRightShownRange(today);
                dateString = Utils.MONTHS_SHORT[leftWeekRange.get(Calendar.MONTH) + 1] + " " +
                        leftWeekRange.get(Calendar.DAY_OF_MONTH) + " " +
                        leftWeekRange.get(Calendar.YEAR) + " - " +
                        Utils.MONTHS_SHORT[rightWeekRange.get(Calendar.MONTH) + 1] + " " +
                        rightWeekRange.get(Calendar.DAY_OF_MONTH) + " " +
                        rightWeekRange.get(Calendar.YEAR);
                dateShownString = " this week";
                break;
            case LAST_WEEK:
                Calendar leftLastWeekRange = Utils.GetLastWeekLeftRange(today);
                Calendar rightLastWeekRange = Utils.GetLastWeekRightShownRange(today);
                dateString = Utils.MONTHS_SHORT[leftLastWeekRange.get(Calendar.MONTH) + 1] + " " +
                        leftLastWeekRange.get(Calendar.DAY_OF_MONTH) + " " +
                        leftLastWeekRange.get(Calendar.YEAR) + " - " +
                        Utils.MONTHS_SHORT[rightLastWeekRange.get(Calendar.MONTH) + 1] + " " +
                        rightLastWeekRange.get(Calendar.DAY_OF_MONTH) + " " +
                        rightLastWeekRange.get(Calendar.YEAR);
                dateShownString = " last week";
                break;
            case THIS_MONTH:
                dateString = Utils.MONTHS_SHORT[today.get(Calendar.MONTH) + 1] + " " +
                        today.get(Calendar.YEAR);
                dateShownString = " this month";
                break;
            case LAST_MONTH:
                Calendar lastMonthCalendar = Utils.GetLastMonthLeftRange(today);
                dateString = Utils.MONTHS_SHORT[lastMonthCalendar.get(Calendar.MONTH) + 1] + " " +
                        lastMonthCalendar.get(Calendar.YEAR);
                dateShownString = " last month";
                break;
            case THIS_YEAR:
                dateString = today.get(Calendar.YEAR) + "";
                dateShownString = " this year";
                break;
            case LAST_YEAR:
                Calendar lastYearCalendar = Utils.GetLastYearLeftRange(today);
                dateString = lastYearCalendar.get(Calendar.YEAR) + "";
                dateShownString = " last year";
                break;
        }
    }

}
