package com.nightonke.saver;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {

    private List<List<Record>> contents;
    private float Sum;
    private int year;
    private int month;
    private int startYear;
    private int startMonth;
    private int endYear;
    private int endMonth;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public RecyclerViewAdapter(List<Record> records) {
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
            Log.d("Saver", "Every: " + record.toString());
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
        for (List<Record> list : contents) {
            Log.d("Saver", list.get(0).getCalendar().get(Calendar.YEAR) + " " + list.get(0).getCalendar().get(Calendar.MONTH));
            for (Record r : list) {
                Log.d("Saver", r.toString());
            }
        }
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
                holder.from.setText("From " + startYear + "-" + startMonth);
                holder.sum.setText(Sum + "");
                holder.to.setText("To " + endYear + "-" + endMonth);
                break;
            case TYPE_CELL:
                int year = contents.get(position - 1).get(0).getCalendar().get(Calendar.YEAR);
                int month = contents.get(position - 1).get(0).getCalendar().get(Calendar.MONTH) + 1;

                Calendar tempCal = new GregorianCalendar(year, month - 1, 1);
                int daysInMonth = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

                Log.d("Saver", "" + daysInMonth);

                float Sum = 0;
                int p = 0;
                int numSubcolumns = 1;
                int numColumns = daysInMonth;

                ColumnChartData data;
                List<Column> columns = new ArrayList<>();
                List<SubcolumnValue> values;

                for (int i = 0; i < numColumns; ++i) {
                    values = new ArrayList<>();
                    if (p < contents.get(position - 1).size()
                            && contents.get(position - 1).get(p).getCalendar().
                            get(Calendar.DAY_OF_MONTH) == i + 1) {
                        values.add(new SubcolumnValue((float)
                                contents.get(position - 1).get(p).getMoney(),
                                ChartUtils.pickColor()));
                        Log.d("Saver", "" + year + " " + month + " " + p + 1 + " " + contents.get(position - 1).get(p).getMoney());
                        Sum += contents.get(position - 1).get(p).getMoney();
                        p++;
                    } else {
                        values.add(new SubcolumnValue((float)0,
                                ChartUtils.pickColor()));
                    }
                    Column column = new Column(values);
                    column.setHasLabels(false);
                    column.setHasLabelsOnlyForSelected(false);
                    columns.add(column);
                }

                data = new ColumnChartData(columns);

                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);

                holder.chart.setColumnChartData(data);
                holder.chart.setZoomEnabled(false);

                holder.date.setText(year + "-" + month + ": " + (int)Sum);

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
}
