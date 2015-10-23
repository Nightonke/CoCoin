package com.nightonke.saver;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private Context mContext;

    private List<List<Record>> contents;
    private float Sum;
    private int year;
    private int month;
    private int startYear;
    private int startMonth;
    private int endYear;
    private int endMonth;

    private String tag;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public RecyclerViewAdapter(List<Record> records, Context context) {
        mContext = context;
        Sum = 0;
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record lhs, Record rhs) {
                return lhs.getCalendar().compareTo(rhs.getCalendar());
            }
        });
        contents = new ArrayList<>();
        year = records.get(0).getCalendar().get(Calendar.YEAR);
        month = records.get(0).getCalendar().get(Calendar.MONTH) + 1;
        startYear = year;
        startMonth = month;
        List<Record> anItem = new ArrayList<>();
        for (Record record : records) {
            Sum += record.getMoney();
            if (record.getCalendar().get(Calendar.YEAR) == year
                    && record.getCalendar().get(Calendar.MONTH) == month - 1) {
                anItem.add(record);
            } else {
                contents.add(anItem);
                anItem = new ArrayList<>();
                anItem.add(record);
                year = record.getCalendar().get(Calendar.YEAR);
                month = record.getCalendar().get(Calendar.MONTH) + 1;
            }
        }
        contents.add(anItem);
        tag = contents.get(0).get(0).getTag();
        endYear = year;
        endMonth = month;
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
        return null;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                holder.from.setText("From " + startYear + " " + Utils.MONTHS[startMonth]);
                holder.sum.setText((int)Sum + "");
                holder.to.setText("To " + endYear + " " + Utils.MONTHS[endMonth]);
                break;
            case TYPE_CELL:
                int year = contents.get(position - 1).get(0).getCalendar().get(Calendar.YEAR);
                int month = contents.get(position - 1).get(0).getCalendar().get(Calendar.MONTH) + 1;

                Calendar tempCal = new GregorianCalendar(year, month - 1, 1);
                int daysInMonth = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

                float Sum = 0;
                int p = 0;
                int numSubcolumns = 1;
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
                        Sum += contents.get(position - 1).get(p).getMoney();
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
                holder.expanse.setText("" + (int)Sum);

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
            int count = 0;
            for (Record record : contents.get(position)) {
                if (record.getCalendar().get(Calendar.DAY_OF_MONTH) == columnIndex + 1) {
                    if (count == subColumnIndex) {
                        String text = "";
                        text += "Spend " + (int)record.getMoney() +
                                " at " + record.getCalendarString() + ".\n";
                        text += record.getRemark() + ".";
                        Snackbar snackbar =
                                Snackbar
                                .with(mContext)
                                .type(SnackbarType.MULTI_LINE)
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .position(Snackbar.SnackbarPosition.BOTTOM)
                                .margin(15, 15)
                                .backgroundDrawable(Utils.GetSnackBarBackground(record.getTag()))
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

        @Override
        public void onValueDeselected() {

        }

    }
}
