package com.nightonke.saver;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
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
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class TodayViewRecyclerViewAdapter extends RecyclerView.Adapter<TodayViewRecyclerViewAdapter.viewHolder> {

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

    final List<SliceValue> sliceValues;
    private Map<String, Double> TagExpanse;
    private Map<String, List<Record>> Expanse;
    private PieChartData pieChartData;

    private boolean ISEMPTY;

    private double Sum;
    private int selectedPosition = 0;
    private String dateString;
    private String dateShownString;
    private String dialogTitle;

    public TodayViewRecyclerViewAdapter(List<Record> records, Context context, int position) {
        list = records;
        mContext = context;
        fragmentPosition = position;

        ISEMPTY = records.isEmpty();

        setDateString();

        sliceValues = new ArrayList<>();
        TagExpanse = new TreeMap<>();
        Expanse = new HashMap<>();

        if (!ISEMPTY) {
            for (int j = 2; j < RecordManager.TAGS.size(); j++) {
                TagExpanse.put(RecordManager.TAGS.get(j), Double.valueOf(0));
                Expanse.put(RecordManager.TAGS.get(j), new ArrayList<Record>());
            }

            Sum = 0;

            for (Record record : records) {
                TagExpanse.put(record.getTag(),
                        TagExpanse.get(record.getTag())
                                + Double.valueOf(record.getMoney()));
                Expanse.get(record.getTag()).add(record);
                Sum += record.getMoney();
            }

            TagExpanse = Utils.SortTreeMapByValues(TagExpanse);

            for (Map.Entry<String, Double> entry : TagExpanse.entrySet()) {
                if (entry.getValue() >= 1) {
                    SliceValue sliceValue = new SliceValue(
                            (float)(double)entry.getValue(),
                            mContext.getResources().
                                    getColor(Utils.GetTagColor(entry.getKey())));
                    sliceValue.setLabel(entry.getKey());
                    sliceValues.add(sliceValue);
                }
            }
        }

        pieChartData = new PieChartData(sliceValues);
        pieChartData.setHasLabels(false);
        pieChartData.setHasLabelsOnlyForSelected(false);
        pieChartData.setHasLabelsOutside(false);
        pieChartData.setHasCenterCircle(false);

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
        return list.size() + 1;
    }

    @Override
    public TodayViewRecyclerViewAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

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
    public void onBindViewHolder(final viewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case TYPE_HEADER:

                holder.date.setText(dateString);
                holder.expanseSum.setText(String.valueOf((int) Sum));

                holder.date.setTypeface(Utils.typefaceLatoLight);
                holder.expanseSum.setTypeface(Utils.typefaceLatoLight);

                if (ISEMPTY) {
                    holder.emptyTip.setVisibility(View.VISIBLE);
                    holder.emptyTip.setTypeface(Utils.typefaceLatoLight);
                } else {
                    holder.emptyTip.setVisibility(View.INVISIBLE);
                }

                holder.pie.setPieChartData(pieChartData);
                holder.pie.setOnValueTouchListener(new PieValueTouchListener());
                holder.pie.setChartRotationEnabled(false);

                if (!ISEMPTY) {
                    holder.iconRight.setVisibility(View.VISIBLE);
                    holder.iconRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedPosition = (selectedPosition + 1) % sliceValues.size();
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
                            selectedPosition
                                    = (selectedPosition - 1 + sliceValues.size()) % sliceValues.size();
                            SelectedValue selectedValue =
                                    new SelectedValue(
                                            selectedPosition,
                                            0,
                                            SelectedValue.SelectedValueType.NONE);
                            holder.pie.selectValue(selectedValue);
                        }
                    });
                } else {
                    holder.iconLeft.setVisibility(View.GONE);
                    holder.iconRight.setVisibility(View.GONE);
                }

                break;

            case TYPE_CELL:

                holder.imageView.setImageResource(Utils.GetTagIcon(list.get(position - 1).getTag()));

                holder.money.setText((int) list.get(position - 1).getMoney() + "");
                holder.money.setTextColor(Utils.GetTagColor(list.get(position - 1).getTag()));
                holder.money.setTypeface(Utils.typefaceLatoLight);

                holder.cellDate.setText(list.get(position - 1).getCalendarString());
                holder.cellDate.setTypeface(Utils.typefaceLatoLight);

                holder.index.setText(position + "");
                holder.index.setTypeface(Utils.typefaceLatoLight);

                break;
        }
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        @Optional
        @InjectView(R.id.date)
        TextView date;
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
        @InjectView(R.id.icon_left)
        MaterialIconView iconLeft;
        @Optional
        @InjectView(R.id.icon_right)
        MaterialIconView iconRight;
        @Optional
        @InjectView(R.id.image_view)
        ImageView imageView;
        @Optional
        @InjectView(R.id.cell_date)
        TextView cellDate;
        @Optional
        @InjectView(R.id.money)
        TextView money;
        @Optional
        @InjectView(R.id.index)
        TextView index;

        viewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private class PieValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int i, SliceValue sliceValue) {
            String text = "";
            final String tag = String.valueOf(sliceValue.getLabelAsChars());
            Double percent = sliceValue.getValue() / Sum * 100;
            text += "Spend " + (int)sliceValue.getValue()
                    + " (takes " + String.format("%.2f", percent) + "%)\n"
                    + " in " + tag + ".\n";
            dialogTitle = "Spend " + (int)sliceValue.getValue() + dateShownString + "\n" +
                    " in " + tag;
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
                            .actionListener(new ActionClickListener() {
                                @Override
                                public void onActionClicked(Snackbar snackbar) {
                                    List<Record> shownRecords = Expanse.get(tag);
                                    ((FragmentActivity)mContext).getSupportFragmentManager()
                                            .beginTransaction()
                                            .add(new RecordCheckDialog(
                                                    mContext, shownRecords, dialogTitle), "MyDialog")
                                            .commit();
                                }
                            });
            SnackbarManager.show(snackbar);
        }

        @Override
        public void onValueDeselected() {

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
                dateString = basicYesterdayDateString.substring(6, basicTodayDateString.length());
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
