package com.nightonke.saver;

import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jungly.gridpasswordview.Util;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
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
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private Context mContext;

    private List<List<Record>> contents;
    private List<Integer> type;
    private List<Double> SumList;
    private List<Map<String, Double>> AllTagExpanse;
    private float Sum;
    private int year;
    private int month;
    private int startYear;
    private int startMonth;
    private int endYear;
    private int endMonth;

    private String tag;

    private int chartType;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    static final Integer SHOW_IN_YEAR = 0;
    static final Integer SHOW_IN_MONTH = 1;

    static final int PIE = 0;
    static final int HISTOGRAM = 1;

    private int fragmentPosition;

    public RecyclerViewAdapter(List<Record> records, Context context, int position) {
        mContext = context;
        fragmentPosition = position;
        if (position == 0) {
            chartType = PIE;
        } else {
            chartType = HISTOGRAM;
        }

        Sum = 0;
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record lhs, Record rhs) {
                return lhs.getCalendar().compareTo(rhs.getCalendar());
            }
        });
        contents = new ArrayList<>();
        SumList = new ArrayList<>();
        type = new ArrayList<>();
        year = records.get(0).getCalendar().get(Calendar.YEAR);
        month = records.get(0).getCalendar().get(Calendar.MONTH) + 1;
        startYear = year;
        startMonth = month;
        int yearPosition = 0;
        double monthSum = 0;
        double yearSum = 0;
        List<Record> yearSet = new ArrayList<>();
        List<Record> monthSet = new ArrayList<>();
        for (Record record : records) {
            Sum += record.getMoney();
            if (record.getCalendar().get(Calendar.YEAR) == year) {
                yearSet.add(record);
                yearSum += record.getMoney();
                if (record.getCalendar().get(Calendar.MONTH) == month - 1) {
                    monthSet.add(record);
                    monthSum += record.getMoney();
                } else {
                    contents.add(monthSet);
                    SumList.add(monthSum);
                    monthSum = record.getMoney();
                    type.add(SHOW_IN_MONTH);
                    monthSet = new ArrayList<>();
                    monthSet.add(record);
                    month = record.getCalendar().get(Calendar.MONTH) + 1;
                }
            } else {
                contents.add(monthSet);
                SumList.add(monthSum);
                monthSum = record.getMoney();
                type.add(SHOW_IN_MONTH);
                monthSet = new ArrayList<>();
                monthSet.add(record);
                month = record.getCalendar().get(Calendar.MONTH) + 1;

                contents.add(yearPosition, yearSet);
                SumList.add(yearPosition, yearSum);
                yearSum = record.getMoney();
                type.add(yearPosition, SHOW_IN_YEAR);
                yearPosition = contents.size();
                yearSet = new ArrayList<>();
                yearSet.add(record);
                year = record.getCalendar().get(Calendar.YEAR);
                monthSet = new ArrayList<>();
                monthSet.add(record);
                month = record.getCalendar().get(Calendar.MONTH) + 1;
            }
        }
        contents.add(monthSet);
        SumList.add(monthSum);
        type.add(SHOW_IN_MONTH);
        contents.add(yearPosition, yearSet);
        SumList.add(yearPosition, yearSum);
        type.add(yearPosition, SHOW_IN_YEAR);

        tag = contents.get(0).get(0).getTag();
        endYear = year;
        endMonth = month;

        AllTagExpanse = new ArrayList<>();
        for (int i = 0; i < contents.size(); i++) {
            Map<String, Double> tagExpanse = new TreeMap<>();

            for (int j = 2; j < RecordManager.TAGS.size(); j++) {
                tagExpanse.put(RecordManager.TAGS.get(j), Double.valueOf(0));
            }

            for (Record record : contents.get(i)) {
                tagExpanse.put(record.getTag(),
                        tagExpanse.get(record.getTag())
                                + Double.valueOf(record.getMoney()));
            }

            AllTagExpanse.add(tagExpanse);
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
        return contents.size() + 1;
    }

    @Override
    public RecyclerViewAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (chartType) {
            case PIE: {
                switch (viewType) {
                    case TYPE_HEADER: {
                        view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_item_card_big, parent, false);
                        return new viewHolder(view) {
                        };
                    }
                    case TYPE_CELL: {
                        view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_item_card_pie_small, parent, false);
                        return new viewHolder(view) {
                        };
                    }
                }
            }
            case HISTOGRAM: {
                switch (viewType) {
                    case TYPE_HEADER: {
                        view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_item_card_big, parent, false);
                        return new viewHolder(view) {
                        };
                    }
                    case TYPE_CELL: {
                        view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_item_card_small, parent, false);
                        return new viewHolder(view) {
                        };
                    }
                }
            }
        }


        return null;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {

        switch (chartType) {

            case PIE:

                switch (getItemViewType(position)) {

                    case TYPE_HEADER:
                        holder.from.setText("From " + startYear + " " + Utils.MONTHS[startMonth]);
                        holder.sum.setText((int)Sum + "");
                        holder.to.setText("To " + endYear + " " + Utils.MONTHS[endMonth]);
                        break;

                    case TYPE_CELL:

                        List<SliceValue> values = new ArrayList<>();
                        for (int i = 2; i < RecordManager.TAGS.size(); i++) {
                            SliceValue sliceValue = new SliceValue(
                                    (float)(double)AllTagExpanse.get(position - 1)
                                            .get(RecordManager.TAGS.get(i)),
                                    mContext.getResources().
                                            getColor(Utils.GetTagColor(RecordManager.TAGS.get(i))));
                            sliceValue.setLabel(RecordManager.TAGS.get(i));
                            values.add(sliceValue);
                        }

                        Collections.sort(values, new Comparator<SliceValue>() {
                            @Override
                            public int compare(SliceValue lhs, SliceValue rhs) {
                                return Float.compare(lhs.getValue(), rhs.getValue());
                            }
                        });

                        PieChartData data = new PieChartData(values);
                        data.setHasLabels(false);
                        data.setHasLabelsOnlyForSelected(false);
                        data.setHasLabelsOutside(false);
                        data.setHasCenterCircle(false);

                        holder.pie.setPieChartData(data);

                        int year = contents.get(position - 1).get(0).getCalendar().get(Calendar.YEAR);

                        if (type.get(position - 1).equals(SHOW_IN_MONTH)) {
                            int month = contents.get(position - 1).get(0).getCalendar().get(Calendar.MONTH);
                            holder.date.setText(year + " " + Utils.MONTHS[month + 1]);
                        } else {
                            holder.date.setText(year + " ");
                        }

                        holder.pie.setOnValueTouchListener(new PieValueTouchListener(position - 1));
                        holder.pie.setChartRotationEnabled(false);

                        holder.expanse.setText("" + (int) (double) SumList.get(position - 1));

                        break;
                }

                break;

            case HISTOGRAM:

                switch (getItemViewType(position)) {

                    case TYPE_HEADER:
                        holder.from.setText("From " + startYear + " " + Utils.MONTHS[startMonth]);
                        holder.sum.setText((int)Sum + "");
                        holder.to.setText("To " + endYear + " " + Utils.MONTHS[endMonth]);
                        break;

                    case TYPE_CELL:

                        if (type.get(position - 1).equals(SHOW_IN_YEAR)) {
                            int year = contents.get(position - 1).get(0).getCalendar().get(Calendar.YEAR);
                            float monthSum = 0;
                            int p = 0;
                            int numColumns = 12;

                            ColumnChartData data;
                            List<Column> columns = new ArrayList<>();
                            List<SubcolumnValue> values;
                            for (int i = 0; i < numColumns; i++) {
                                while (p < contents.get(position - 1).size()
                                        && contents.get(position - 1).get(p).getCalendar().
                                        get(Calendar.MONTH) == i) {
                                    monthSum += contents.get(position - 1).get(p).getMoney();
                                    p++;
                                }
                                values = new ArrayList<>();
                                SubcolumnValue value = new SubcolumnValue(monthSum,
                                        Utils.GetRandomColor());
                                values.add(value);
                                monthSum = 0;
                                Column column = new Column(values);
                                column.setHasLabels(false);
                                column.setHasLabelsOnlyForSelected(false);
                                columns.add(column);
                            }

                            data = new ColumnChartData(columns);

                            Axis axisX = new Axis();
                            List<AxisValue> axisValueList = new ArrayList<>();
                            for (int i = 0; i < numColumns; i++) {
                                axisValueList.add(new AxisValue(i).setLabel(Utils.MONTHS_SHORT[i + 1]));
                            }
                            axisX.setValues(axisValueList);
                            Axis axisY = new Axis().setHasLines(true);
                            data.setAxisXBottom(axisX);
                            data.setAxisYLeft(axisY);
                            data.setStacked(true);

                            holder.chart.setColumnChartData(data);
                            holder.chart.setZoomEnabled(false);
                            holder.chart.setOnValueTouchListener(new ValueTouchListener(position - 1));

                            holder.date.setText(year + "");
                            holder.expanse.setText("" + (int)(double)SumList.get(position - 1));

                        }
                        if (type.get(position - 1).equals(SHOW_IN_MONTH)) {
                            int year = contents.get(position - 1).get(0).getCalendar().get(Calendar.YEAR);
                            int month = contents.get(position - 1).get(0).getCalendar().get(Calendar.MONTH) + 1;

                            Calendar tempCal = new GregorianCalendar(year, month - 1, 1);
                            int daysInMonth = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

                            int p = 0;
                            int numColumns = daysInMonth;

                            ColumnChartData data;
                            List<Column> columns = new ArrayList<>();
                            List<SubcolumnValue> values;

                            for (int i = 0; i < numColumns; ++i) {
                                values = new ArrayList<>();
                                while (p < contents.get(position - 1).size()
                                        && contents.get(position - 1).get(p).getCalendar().
                                        get(Calendar.DAY_OF_MONTH) == i + 1) {
                                    SubcolumnValue value = new SubcolumnValue((float)
                                            contents.get(position - 1).get(p).getMoney(),
                                            Utils.GetRandomColor());
                                    values.add(value);
                                    p++;
                                }
                                Column column = new Column(values);
                                column.setHasLabels(false);
                                column.setHasLabelsOnlyForSelected(false);
                                columns.add(column);
                            }

                            data = new ColumnChartData(columns);

                            Axis axisX = new Axis();
                            List<AxisValue> axisValueList = new ArrayList<>();
                            for (int i = 0; i < daysInMonth; i++) {
                                axisValueList.add(new AxisValue(i).setLabel(i + 1 + ""));
                            }
                            axisX.setValues(axisValueList);
                            Axis axisY = new Axis().setHasLines(true);
                            data.setAxisXBottom(axisX);
                            data.setAxisYLeft(axisY);
                            data.setStacked(true);

                            holder.chart.setColumnChartData(data);
                            holder.chart.setZoomEnabled(false);
                            holder.chart.setOnValueTouchListener(new ValueTouchListener(position - 1));

                            holder.date.setText(year + " " + Utils.MONTHS[month]);
                            holder.expanse.setText("" + (int)(double)SumList.get(position - 1));
                        }

                        break;
                }

                break;
        }
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        @Optional
        @InjectView(R.id.from)
        TextView from;
        @Optional
        @InjectView(R.id.sum)
        TextView sum;
        @Optional
        @InjectView(R.id.to)
        TextView to;
        @Optional
        @InjectView(R.id.chart_pie)
        PieChartView pie;
        @Optional
        @InjectView(R.id.chart)
        ColumnChartView chart;
        @Optional
        @InjectView(R.id.date)
        TextView date;
        @Optional
        @InjectView(R.id.expanse)
        TextView expanse;

        viewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        private int position;

        public ValueTouchListener(int position) {
            this.position = position;
        }

        @Override
        public void onValueSelected(int columnIndex, int subColumnIndex, SubcolumnValue value) {
            if (type.get(position).equals(SHOW_IN_MONTH)) {
                int count = 0;
                for (Record record : contents.get(position)) {
                    if (record.getCalendar().get(Calendar.DAY_OF_MONTH) == columnIndex + 1) {
                        if (count == subColumnIndex) {
                            String text = "";
                            text += "Spend " + (int) record.getMoney() +
                                    " at " + record.getCalendarString() + ".\n";
                            text += record.getRemark() + ".";
                            Snackbar snackbar =
                                    Snackbar
                                            .with(mContext)
                                            .type(SnackbarType.MULTI_LINE)
                                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                            .position(Snackbar.SnackbarPosition.BOTTOM)
                                            .margin(15, 15)
                                            .backgroundDrawable(Utils.GetSnackBarBackground(fragmentPosition))
                                            .text(text)
                                            .textColor(Color.WHITE)
                                            .actionLabel("Edit")
                                            .actionColor(Color.WHITE)
                                            .actionListener(new ActionClickListener() {
                                                @Override
                                                public void onActionClicked(Snackbar snackbar) {
                                                    Toast.makeText(mContext, "To be continue...", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                            SnackbarManager.show(snackbar);
                            return;
                        }
                        count++;
                    }
                }
            }
            if (type.get(position).equals(SHOW_IN_YEAR)) {
                String text = "";
                text += "Spend " + (int)value.getValue() +
                        " at " + Utils.MONTHS_SHORT[columnIndex + 1] + " " +
                        contents.get(position).get(0).getCalendar().get(Calendar.YEAR) + ".\n";
                Snackbar snackbar =
                        Snackbar
                                .with(mContext)
                                .type(SnackbarType.SINGLE_LINE)
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .position(Snackbar.SnackbarPosition.BOTTOM)
                                .margin(15, 15)
                                .backgroundDrawable(Utils.GetSnackBarBackground(fragmentPosition))
                                .text(text)
                                .textColor(Color.WHITE);
                SnackbarManager.show(snackbar);
                return;
            }
        }

        @Override
        public void onValueDeselected() {

        }

    }

    private class PieValueTouchListener implements PieChartOnValueSelectListener {

        private int position;

        public PieValueTouchListener(int position) {
            this.position = position;
        }

        @Override
        public void onValueSelected(int i, SliceValue sliceValue) {
            String text = "";
            String text2 = "";
            Double percent = sliceValue.getValue() / SumList.get(position) * 100;
            text += "Spend " + (int)sliceValue.getValue()
                    + "(takes " + String.format("%.2f", percent) + "%)\n"
                    + " in " + String.valueOf(sliceValue.getLabelAsChars()) + ".\n";

            Snackbar snackbar =
                    Snackbar
                            .with(mContext)
                            .type(SnackbarType.MULTI_LINE)
                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                            .position(Snackbar.SnackbarPosition.BOTTOM)
                            .margin(15, 15)
                            .backgroundDrawable(Utils.GetSnackBarBackground(fragmentPosition))
                            .text(text)
                            .textColor(Color.WHITE)
                            .actionLabel("Check")
                            .actionColor(Color.WHITE)
                            .actionListener(new ActionClickListener() {
                                @Override
                                public void onActionClicked(Snackbar snackbar) {
                                    Toast.makeText(mContext, "To be continue...", Toast.LENGTH_SHORT).show();
                                }
                            });
            SnackbarManager.show(snackbar);
            return;
        }

        @Override
        public void onValueDeselected() {

        }
    }

}
