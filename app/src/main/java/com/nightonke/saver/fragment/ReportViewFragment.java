package com.nightonke.saver.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.melnykov.fab.FloatingActionButton;
import com.nightonke.saver.R;
import com.nightonke.saver.activity.CoCoinApplication;
import com.nightonke.saver.adapter.DialogSelectListDataAdapter;
import com.nightonke.saver.adapter.ReportDayAdapter;
import com.nightonke.saver.adapter.ReportMonthAdapter;
import com.nightonke.saver.adapter.ReportTagAdapter;
import com.nightonke.saver.model.CoCoinRecord;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.ui.ExpandedListView;
import com.nightonke.saver.ui.MyGridView;
import com.nightonke.saver.util.CoCoinUtil;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.squareup.leakcanary.RefWatcher;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by 伟平 on 2015/10/20.
 *
 * report is to show the expense of some time for user
 *
 * if the user select a year
 * we show
 * expense and sum(compare with last year)
 * the number of tags used in(compare with last year)
 * a pie for all expense on diff tags
 * the highest expense on some tags(and percent), click for more
 * the lowest expense on some tags(and percent), click for more
 * the percent used in food, clothes, house and traffic
 * the highest expense on some tags except the four kinds of tags above
 * a line chart of every month expense
 * the highest expense of some months(and percent), click for more
 * the lowest expense of some months(and percent), click for more
 * the average value of expense of a month
 * the highest expense of some days, click for more(@param MAX_DAY_EXPENSE days)
 * the lowest expense of some days, click for more(@param MAX_DAY_EXPENSE days)
 * the average value of expense of a day
 *
 * if the user select a month
 * we show
 * expense and sum(compare with last month)
 * the number of tags used in(compare with last month)
 * a pie for all expense on diff tags
 * the highest expense on some tags(and percent), click for more
 * the lowest expense on some tags(and percent), click for more
 * the percent used in food, clothes, house and traffic
 * the highest expense on some tags except the four kinds of tags above
 * a line chart of every day expense
 * the highest expense of some days(and percent), click for more
 * the lowest expense of some days(and percent), click for more
 * the average value of expense of a day
 */

public class ReportViewFragment extends Fragment
        implements
        View.OnClickListener,
        AdapterView.OnItemClickListener {

    public static String REPORT_TITLE = "";

    public static final int MAX_TAG_EXPENSE = 8;
    public static final int MAX_DAY_EXPENSE = 10;

    private Context mContext;

    private FloatingActionButton button;
    private ObservableScrollView mScrollView;

    private TextView fromDate;
    private TextView expenseTV;
    private boolean isEmpty = false;
    private TextView emptyTip;
    private TextView tagsTV;

    private Calendar from = Calendar.getInstance();
    private Calendar to = Calendar.getInstance();

    private SuperToast superToast;

    private boolean IS_EMPTY = false;

    // store the sum of expenses of each tag
    private Map<Integer, Double> TagExpanse;
    // store the records of each tag
    private Map<Integer, List<CoCoinRecord>> Expanse;
    // the original target value of the whole pie
    private float[] originalTargets;

    // the date string on the footer and header
    private String dateString;
    // the date string shown in the dialog
    private String dateShownString;
    // the string shown in the dialog
    private String dialogTitle;

    // the selected tag in pie
    private int tagId = -1;

    // select list
    // year, month(-1 means the whole year), records, expenses
    private ArrayList<double[]> selectListData = null;

    // title
    private TextView title;

    // pie
    private LinearLayout pieLayout;
    private TextView pieTitle;
    private PieChartView pie;
    private int pieSelectedPosition = 0;  // the selected position of one part of the pie
    private int lastPieSelectedPosition = -1;  // the last selected position of one part of the pie
    private MaterialIconView iconRight;
    private MaterialIconView iconLeft;

    // highest tag list
    private LinearLayout highestTagLayout;
    private TextView highestTagTitle;
    private LinearLayout highestFirst;
    private ImageView highestTagIcon;
    private TextView highestTagText;
    private TextView highestTagExpenseTV;
    private TextView highestTagRecord;
    private ExpandedListView highestTags;
    private ReportTagAdapter highestTagsAdapter;
    private ExpandableRelativeLayout highestTagsLayout;
    private LinearLayout highestTagMore;
    private TextView highestTagMoreText;

    // lowest tag list
    private LinearLayout lowestTagLayout;
    private TextView lowestTagTitle;
    private LinearLayout lowestFirst;
    private ImageView lowestTagIcon;
    private TextView lowestTagText;
    private TextView lowestTagExpenseTV;
    private TextView lowestTagRecord;
    private ExpandedListView lowestTags;
    private ReportTagAdapter lowestTagsAdapter;
    private ExpandableRelativeLayout lowestTagsLayout;
    private LinearLayout lowestTagMore;
    private TextView lowestTagMoreText;

    // line
    private LinearLayout lineLayout;
    private TextView lineTitle;
    private LineChartView line;
    private int lineSelectedPosition = 0;  // the selected position of one part of the line
    private int lastLineSelectedPosition = -1;  // the last selected position of one part of the line
    private MaterialIconView iconRightLine;
    private MaterialIconView iconLeftLine;
    
    // month
    private LinearLayout highestMonthLayout;
    private TextView monthTitle;
    private LinearLayout highestFirstMonth;
    private TextView highestFirstIcon;
    private TextView highestFirstText;
    private TextView highestFirstExpenseTV;
    private TextView highestFirstRecord;
    private ExpandedListView highestMonths;
    private ReportMonthAdapter highestMonthsAdapter;
    private ExpandableRelativeLayout highestMonthsLayout;
    private LinearLayout highestLast;
    private TextView highestLastIcon;
    private TextView highestLastText;
    private TextView highestLastExpenseTV;
    private TextView highestLastRecord;
    private LinearLayout highestMonthMore;
    private TextView highestMonthMoreText;

    // average month
    private TextView averageMonthText;
    private TextView averageMonthExpenseTV;
    private TextView averageMonthRecordTV;
    
    // highest day
    private LinearLayout highestDayLayout;
    private TextView highestDayTitle;
    private LinearLayout highestFirstDay;
    private TextView highestDayIcon;
    private TextView highestDayText;
    private TextView highestDayExpenseTV;
    private TextView highestDayRecord;
    private ExpandedListView highestDays;
    private ReportDayAdapter highestDaysAdapter;
    private ExpandableRelativeLayout highestDaysLayout;
    private LinearLayout highestDayMore;
    private TextView highestDayMoreText;
    
    // lowest day
    private LinearLayout lowestDayLayout;
    private TextView lowestDayTitle;
    private LinearLayout lowestFirstDay;
    private TextView lowestDayIcon;
    private TextView lowestDayText;
    private TextView lowestDayExpenseTV;
    private TextView lowestDayRecord;
    private ExpandedListView lowestDays;
    private ReportDayAdapter lowestDaysAdapter;
    private ExpandableRelativeLayout lowestDaysLayout;
    private LinearLayout lowestDayMore;
    private TextView lowestDayMoreText;

    // average day
    private TextView averageDayText;
    private TextView averageDayExpenseTV;
    private TextView averageDayRecordTV;

    // foot
    private TextView foot;

    public static ReportViewFragment newInstance() {
        ReportViewFragment fragment = new ReportViewFragment();
        return fragment;
    }

    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity = (Activity)context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CoCoinFragmentManager.reportViewFragment = this;
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
        return inflater.inflate(R.layout.fragment_report_view, container, false);
    }

    private MaterialDialog dialog;
    private View dialogView;
    private MyGridView myGridView;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IS_EMPTY = RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.isEmpty();

        mScrollView = (ObservableScrollView) view.findViewById(R.id.scrollView);
        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);

        expenseTV = (TextView)view.findViewById(R.id.expense);
        expenseTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        expenseTV.setText(CoCoinUtil.GetInMoney(0));
        tagsTV = (TextView)view.findViewById(R.id.tags);
        tagsTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        tagsTV.setText("");

        title = (TextView)view.findViewById(R.id.title);
        title.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);

        pieLayout = (LinearLayout)view.findViewById(R.id.pie_layout);
        pieLayout.setVisibility(View.GONE);
        pieTitle = (TextView)view.findViewById(R.id.pie_title);
        pieTitle.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        pie = (PieChartView)view.findViewById(R.id.chart_pie);
        pie.setChartRotationEnabled(false);
        pie.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int p, SliceValue sliceValue) {
                // snack bar
                String text;
                tagId = Integer.valueOf(String.valueOf(sliceValue.getLabelAsChars()));
                double percent = sliceValue.getValue() / expense * 100;
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
                    if (selectYear) {
                        dialogTitle = from.get(Calendar.YEAR) + "年" + "\n" +
                                CoCoinUtil.GetSpendString((int) sliceValue.getValue()) +
                                "于" + CoCoinUtil.GetTagName(tagId);
                    } else {
                        dialogTitle = from.get(Calendar.YEAR) + "年" + (from.get(Calendar.MONTH) + 1) + "月" + "\n" +
                                CoCoinUtil.GetSpendString((int) sliceValue.getValue()) +
                                "于" + CoCoinUtil.GetTagName(tagId);
                    }
                } else {
                    if (selectYear) {
                        dialogTitle = CoCoinUtil.GetSpendString((int) sliceValue.getValue()) + " in " + from.get(Calendar.YEAR) + "\n" +
                                "on " + CoCoinUtil.GetTagName(tagId);
                    } else {
                        dialogTitle = CoCoinUtil.GetSpendString((int) sliceValue.getValue()) + " in " + CoCoinUtil.GetMonthShort(from.get(Calendar.MONTH) + 1) + " " + from.get(Calendar.YEAR) + "\n" +
                                "on " + CoCoinUtil.GetTagName(tagId);
                    }
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
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        new GetData(from, to, tagId, dialogTitle).execute();
                                    }
                                });
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
        iconRight = (MaterialIconView)view.findViewById(R.id.icon_right);
        iconRight.setOnClickListener(this);
        iconLeft = (MaterialIconView)view.findViewById(R.id.icon_left);
        iconLeft.setOnClickListener(this);

        emptyTip = (TextView)view.findViewById(R.id.empty_tip);
        emptyTip.setTypeface(CoCoinUtil.GetTypeface());
        if (RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.size() != 0) {
            emptyTip.setText(mContext.getResources().getString(R.string.report_view_please_select_data));
        } else {
            emptyTip.setText(mContext.getResources().getString(R.string.report_view_no_data));
            isEmpty = true;
        }

        highestTagLayout = (LinearLayout)view.findViewById(R.id.highest_tag_layout);
        highestTagLayout.setVisibility(View.GONE);
        highestTagTitle = (TextView)view.findViewById(R.id.highest_tag_title);
        highestTagTitle.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestFirst = (LinearLayout)view.findViewById(R.id.highest_first);
        highestFirst.setOnClickListener(this);
        highestTagIcon = (ImageView)view.findViewById(R.id.highest_tag_icon);
        highestTagText = (TextView)view.findViewById(R.id.highest_tag_text);
        highestTagText.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestTagExpenseTV = (TextView)view.findViewById(R.id.highest_tag_expense);
        highestTagExpenseTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestTagRecord = (TextView)view.findViewById(R.id.highest_tag_sum);
        highestTagRecord.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestTags = (ExpandedListView)view.findViewById(R.id.highest_tags);
        highestTagsLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expand_highest_tag);
        highestTagMore = (LinearLayout)view.findViewById(R.id.highest_tag_more);
        highestTagMore.setOnClickListener(this);
        highestTagMoreText = (TextView)view.findViewById(R.id.highest_tag_more_text);
        highestTagMoreText.setTypeface(CoCoinUtil.getInstance().GetTypeface());
        highestTags.setOnItemClickListener(this);

        lowestTagLayout = (LinearLayout)view.findViewById(R.id.lowest_tag_layout);
        lowestTagLayout.setVisibility(View.GONE);
        lowestTagTitle = (TextView)view.findViewById(R.id.lowest_tag_title);
        lowestTagTitle.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        lowestFirst = (LinearLayout)view.findViewById(R.id.lowest_first);
        lowestFirst.setOnClickListener(this);
        lowestTagIcon = (ImageView)view.findViewById(R.id.lowest_tag_icon);
        lowestTagText = (TextView)view.findViewById(R.id.lowest_tag_text);
        lowestTagText.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        lowestTagExpenseTV = (TextView)view.findViewById(R.id.lowest_tag_expense);
        lowestTagExpenseTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        lowestTagRecord = (TextView)view.findViewById(R.id.lowest_tag_sum);
        lowestTagRecord.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        lowestTags = (ExpandedListView)view.findViewById(R.id.lowest_tags);
        lowestTagsLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expand_lowest_tag);
        lowestTagMore = (LinearLayout)view.findViewById(R.id.lowest_tag_more);
        lowestTagMore.setOnClickListener(this);
        lowestTagMoreText = (TextView)view.findViewById(R.id.lowest_tag_more_text);
        lowestTagMoreText.setTypeface(CoCoinUtil.getInstance().GetTypeface());
        lowestTags.setOnItemClickListener(this);

        lineLayout = (LinearLayout)view.findViewById(R.id.line_layout);
        lineLayout.setVisibility(View.GONE);
        lineTitle = (TextView)view.findViewById(R.id.line_title);
        lineTitle.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        line = (LineChartView) view.findViewById(R.id.chart_line);
        line.setZoomEnabled(false);
        line.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                // snack bar
                String text;
                double percent = value.getY() / expense * 100;
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    if (selectYear) {
                        text = "在" + reportYear + " " + CoCoinUtil.getInstance().GetMonthShort((int)value.getX() + 1) + "\n" +
                                CoCoinUtil.GetSpendString((int) value.getY()) +
                                CoCoinUtil.GetPercentString(percent);
                    } else {
                        text = "在" + CoCoinUtil.getInstance().GetMonthShort(reportMonth) + " " + ((int)value.getX() + 1) + CoCoinUtil.getInstance().GetWhetherFuck() + "\n" +
                                CoCoinUtil.GetSpendString((int) value.getY()) +
                                CoCoinUtil.GetPercentString(percent);
                    }
                } else {
                    if (selectYear) {
                        text = CoCoinUtil.GetSpendString((int) value.getY()) +
                                CoCoinUtil.GetPercentString(percent) + "\n" +
                                "in " + reportYear + " " + CoCoinUtil.getInstance().GetMonthShort((int)value.getX() + 1);
                    } else {
                        text = CoCoinUtil.GetSpendString((int) value.getY()) +
                                CoCoinUtil.GetPercentString(percent) + "\n" +
                                "on " + CoCoinUtil.getInstance().GetMonthShort(reportMonth) + " " + ((int)value.getX() + 1) + CoCoinUtil.getInstance().GetWhetherFuck();
                    }
                }
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    if (selectYear) {
                        dialogTitle = "在" + reportYear + " " + CoCoinUtil.getInstance().GetMonthShort((int)value.getX() + 1) + "\n" +
                                CoCoinUtil.GetSpendString((int) value.getY()) +
                                CoCoinUtil.GetPercentString(percent);
                    } else {
                        dialogTitle = "在" + CoCoinUtil.getInstance().GetMonthShort(reportMonth) + " " + ((int)value.getX() + 1) + CoCoinUtil.getInstance().GetWhetherFuck() + "\n" +
                                CoCoinUtil.GetSpendString((int) value.getY()) +
                                CoCoinUtil.GetPercentString(percent);
                    }
                } else {
                    if (selectYear) {
                        dialogTitle = CoCoinUtil.GetSpendString((int) value.getY()) +
                                CoCoinUtil.GetPercentString(percent) + "\n" +
                                "in " + reportYear + " " + CoCoinUtil.getInstance().GetMonthShort((int)value.getX() + 1);
                    } else {
                        dialogTitle = CoCoinUtil.GetSpendString((int) value.getY()) +
                                CoCoinUtil.GetPercentString(percent) + "\n" +
                                "on " + CoCoinUtil.getInstance().GetMonthShort(reportMonth) + " " + ((int)value.getX() + 1) + CoCoinUtil.getInstance().GetWhetherFuck();
                    }
                }
                final Calendar tempFrom = Calendar.getInstance();
                final Calendar tempTo = Calendar.getInstance();
                if (selectYear) {
                    tempFrom.set(reportYear, (int)value.getX(), 1, 0, 0, 0);
                    tempFrom.add(Calendar.SECOND, 0);
                    tempTo.set(reportYear, (int)value.getX(), tempFrom.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
                    tempTo.add(Calendar.SECOND, 0);
                } else {
                    tempFrom.set(reportYear, reportMonth - 1, (int)value.getX() + 1, 0, 0, 0);
                    tempFrom.add(Calendar.SECOND, 0);
                    tempTo.set(reportYear, reportMonth - 1, (int)value.getX() + 1, 23, 59, 59);
                    tempTo.add(Calendar.SECOND, 0);
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
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        new GetData(tempFrom, tempTo, Integer.MIN_VALUE, dialogTitle).execute();
                                    }
                                });
                SnackbarManager.show(snackbar);

                if (pointIndex == lastLineSelectedPosition) {
                    return;
                } else {
                    lastLineSelectedPosition = pointIndex;
                }
            }

            @Override
            public void onValueDeselected() {

            }
        });
        iconRightLine = (MaterialIconView)view.findViewById(R.id.icon_right_line);
        iconRightLine.setOnClickListener(this);
        iconLeftLine = (MaterialIconView)view.findViewById(R.id.icon_left_line);
        iconLeftLine.setOnClickListener(this);

        highestMonthLayout = (LinearLayout) view.findViewById(R.id.highest_month_layout);
        highestMonthLayout.setVisibility(View.GONE);
        monthTitle = (TextView)view.findViewById(R.id.month_title);
        monthTitle.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestFirstMonth = (LinearLayout)view.findViewById(R.id.highest_first_month);
        highestFirstMonth.setOnClickListener(this);
        highestFirstIcon = (TextView) view.findViewById(R.id.highest_month_icon);
        highestFirstIcon.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestFirstText = (TextView)view.findViewById(R.id.highest_month_text);
        highestFirstText.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestFirstExpenseTV = (TextView)view.findViewById(R.id.highest_month_expense);
        highestFirstExpenseTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestFirstRecord = (TextView)view.findViewById(R.id.highest_month_sum);
        highestFirstRecord.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestMonths = (ExpandedListView)view.findViewById(R.id.highest_month);
        highestMonths.setOnItemClickListener(this);
        highestMonthsLayout = (ExpandableRelativeLayout)view.findViewById(R.id.expand_highest_month);
        highestLast = (LinearLayout)view.findViewById(R.id.highest_last_month);
        highestLast.setOnClickListener(this);
        highestLastIcon = (TextView) view.findViewById(R.id.lowest_month_icon);
        highestLastIcon.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestLastText = (TextView)view.findViewById(R.id.lowest_month_text);
        highestLastText.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestLastExpenseTV = (TextView)view.findViewById(R.id.lowest_month_expense);
        highestLastExpenseTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestLastRecord = (TextView)view.findViewById(R.id.lowest_month_sum);
        highestLastRecord.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestMonthMore = (LinearLayout)view.findViewById(R.id.highest_month_more);
        highestMonthMore.setOnClickListener(this);
        highestMonthMoreText = (TextView)view.findViewById(R.id.highest_month_more_text);
        highestMonthMoreText.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);

        averageMonthText = (TextView)view.findViewById(R.id.average_month_text);
        averageMonthText.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        averageMonthExpenseTV = (TextView)view.findViewById(R.id.average_month_expense);
        averageMonthExpenseTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        averageMonthRecordTV = (TextView)view.findViewById(R.id.average_month_sum);
        averageMonthRecordTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        
        highestDayLayout = (LinearLayout)view.findViewById(R.id.highest_day_layout);
        highestDayLayout.setVisibility(View.GONE);
        highestDayTitle = (TextView)view.findViewById(R.id.highest_day_title);
        highestDayTitle.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestFirstDay = (LinearLayout)view.findViewById(R.id.highest_first_day);
        highestFirstDay.setOnClickListener(this);
        highestDayIcon = (TextView) view.findViewById(R.id.highest_day_icon);
        highestDayIcon.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestDayText = (TextView)view.findViewById(R.id.highest_day_text);
        highestDayText.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestDayExpenseTV = (TextView)view.findViewById(R.id.highest_day_expense);
        highestDayExpenseTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestDayRecord = (TextView)view.findViewById(R.id.highest_day_sum);
        highestDayRecord.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        highestDays = (ExpandedListView)view.findViewById(R.id.highest_days);
        highestDaysLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expand_highest_day);
        highestDayMore = (LinearLayout)view.findViewById(R.id.highest_day_more);
        highestDayMore.setOnClickListener(this);
        highestDayMoreText = (TextView)view.findViewById(R.id.highest_day_more_text);
        highestDayMoreText.setTypeface(CoCoinUtil.getInstance().GetTypeface());
        highestDays.setOnItemClickListener(this);

        lowestDayLayout = (LinearLayout)view.findViewById(R.id.lowest_day_layout);
        lowestDayLayout.setVisibility(View.GONE);
        lowestDayTitle = (TextView)view.findViewById(R.id.lowest_day_title);
        lowestDayTitle.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        lowestFirstDay = (LinearLayout)view.findViewById(R.id.lowest_first_day);
        lowestFirstDay.setOnClickListener(this);
        lowestDayIcon = (TextView) view.findViewById(R.id.lowest_day_icon);
        lowestDayIcon.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        lowestDayText = (TextView)view.findViewById(R.id.lowest_day_text);
        lowestDayText.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        lowestDayExpenseTV = (TextView)view.findViewById(R.id.lowest_day_expense);
        lowestDayExpenseTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        lowestDayRecord = (TextView)view.findViewById(R.id.lowest_day_sum);
        lowestDayRecord.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        lowestDays = (ExpandedListView)view.findViewById(R.id.lowest_days);
        lowestDaysLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expand_lowest_day);
        lowestDayMore = (LinearLayout)view.findViewById(R.id.lowest_day_more);
        lowestDayMore.setOnClickListener(this);
        lowestDayMoreText = (TextView)view.findViewById(R.id.lowest_day_more_text);
        lowestDayMoreText.setTypeface(CoCoinUtil.getInstance().GetTypeface());
        lowestDays.setOnItemClickListener(this);

        averageDayText = (TextView)view.findViewById(R.id.average_day_text);
        averageDayText.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        averageDayExpenseTV = (TextView)view.findViewById(R.id.average_day_expense);
        averageDayExpenseTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        averageDayRecordTV = (TextView)view.findViewById(R.id.average_day_sum);
        averageDayRecordTV.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);

        foot = (TextView)view.findViewById(R.id.foot);
        foot.setTypeface(CoCoinUtil.getInstance().typefaceLatoLight);
        foot.setVisibility(View.GONE);

        if (IS_EMPTY) {
            emptyTip.setVisibility(View.GONE);
        }

        button = (FloatingActionButton) view.findViewById(R.id.button);
        button.setOnClickListener(this);

        new GetSelectListData(false).execute();

    }

    public void showDataDialog() {
        if (selectListData == null) new GetSelectListData(true).execute();
        else showSelectListDataDialog();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        RefWatcher refWatcher = CoCoinApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onClick(View v) {
        SelectedValue selectedValue = null;
        switch (v.getId()) {
            case R.id.icon_right:
                if (lastPieSelectedPosition != -1) {
                    pieSelectedPosition = lastPieSelectedPosition;
                }
                pieSelectedPosition
                        = (pieSelectedPosition - 1 + pieChartData.getValues().size())
                        % pieChartData.getValues().size();
                selectedValue =
                        new SelectedValue(
                                pieSelectedPosition,
                                0,
                                SelectedValue.SelectedValueType.NONE);
                pie.selectValue(selectedValue);
                break;
            case R.id.icon_left:
                if (lastPieSelectedPosition != -1) {
                    pieSelectedPosition = lastPieSelectedPosition;
                }
                pieSelectedPosition
                        = (pieSelectedPosition + 1)
                        % pieChartData.getValues().size();
                selectedValue =
                        new SelectedValue(
                                pieSelectedPosition,
                                0,
                                SelectedValue.SelectedValueType.NONE);
                pie.selectValue(selectedValue);
                break;
            case R.id.highest_first:
                onItemClick(highestTags, highestTags.getChildAt(0), -1, -1);
                break;
            case R.id.highest_tag_more:
                if (highestTagsLayout != null) {
                    if (highestTagsLayout.isExpanded()) {
                        highestTagsLayout.collapse();
                        highestTagMoreText.setText(CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_highest_tag_show_more));
                    } else {
                        highestTagsLayout.expand();
                        highestTagMoreText.setText(CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_highest_tag_show_less));
                    }
                }
                break;
            case R.id.lowest_first:
                onItemClick(lowestTags, lowestTags.getChildAt(0), -1, -1);
                break;
            case R.id.lowest_tag_more:
                if (lowestTagsLayout != null) {
                    if (lowestTagsLayout.isExpanded()) {
                        lowestTagsLayout.collapse();
                        lowestTagMoreText.setText(CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_lowest_tag_show_more));
                    } else {
                        lowestTagsLayout.expand();
                        lowestTagMoreText.setText(CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_lowest_tag_show_less));
                    }
                }
                break;
            case R.id.icon_left_line:
                if (lastLineSelectedPosition != -1) {
                    lineSelectedPosition = lastLineSelectedPosition;
                }
                lineSelectedPosition
                        = (lineSelectedPosition - 1 + lineChartData.getLines().get(0).getValues().size())
                        % lineChartData.getLines().get(0).getValues().size();
                selectedValue =
                        new SelectedValue(
                                0,
                                lineSelectedPosition,
                                SelectedValue.SelectedValueType.NONE);
                line.selectValue(selectedValue);
                break;
            case R.id.icon_right_line:
                if (lastLineSelectedPosition != -1) {
                    lineSelectedPosition = lastLineSelectedPosition;
                }
                lineSelectedPosition
                        = (lineSelectedPosition + 1)
                        % lineChartData.getLines().get(0).getValues().size();
                selectedValue =
                        new SelectedValue(
                                0,
                                lineSelectedPosition,
                                SelectedValue.SelectedValueType.NONE);
                line.selectValue(selectedValue);
                break;
            case R.id.highest_first_month:
                onItemClick(highestMonths, highestMonths.getChildAt(0), -1, -1);
                break;
            case R.id.highest_last_month:
                onItemClick(highestMonths, highestMonths.getChildAt(0), 10, -1);
                break;
            case R.id.highest_month_more:
                if (highestMonthsLayout != null) {
                    if (highestMonthsLayout.isExpanded()) {
                        highestMonthsLayout.collapse();
                        highestMonthMoreText.setText(CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_lowest_tag_show_more));
                    } else {
                        highestMonthsLayout.expand();
                        highestMonthMoreText.setText(CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_lowest_tag_show_less));
                    }
                }
                break;
            case R.id.highest_first_day:
                onItemClick(highestDays, highestDays.getChildAt(0), -1, -1);
                break;
            case R.id.highest_day_more:
                if (highestDaysLayout != null) {
                    if (highestDaysLayout.isExpanded()) {
                        highestDaysLayout.collapse();
                        highestDayMoreText.setText(CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_lowest_tag_show_more));
                    } else {
                        highestDaysLayout.expand();
                        highestDayMoreText.setText(CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_lowest_tag_show_less));
                    }
                }
                break;
            case R.id.lowest_first_day:
                onItemClick(lowestDays, lowestDays.getChildAt(0), -1, -1);
                break;
            case R.id.lowest_day_more:
                if (lowestDaysLayout != null) {
                    if (lowestDaysLayout.isExpanded()) {
                        lowestDaysLayout.collapse();
                        lowestDayMoreText.setText(CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_lowest_tag_show_more));
                    } else {
                        lowestDaysLayout.expand();
                        lowestDayMoreText.setText(CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_lowest_tag_show_less));
                    }
                }
                break;
            case R.id.button:
                if (!isEmpty) showSelectListDataDialog();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String dialogTitle = "";
        int expense;
        int tagId;
        int month;
        int day;
        Calendar tempFrom = Calendar.getInstance();
        Calendar tempTo = Calendar.getInstance();
        switch (parent.getId()) {
            case R.id.highest_tags:
                if (gettingData) return;
                tagId = (int)highestTagExpense.get(position + 1)[2];
                expense = (int)highestTagExpense.get(position + 1)[0];
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    if (selectYear) {
                        dialogTitle = from.get(Calendar.YEAR) + "年" + "\n" +
                                CoCoinUtil.GetSpendString(expense) +
                                "于" + CoCoinUtil.GetTagName(tagId);
                    } else {
                        dialogTitle = from.get(Calendar.YEAR) + "年" + (from.get(Calendar.MONTH) + 1) + "月" + "\n" +
                                CoCoinUtil.GetSpendString(expense) +
                                "于" + CoCoinUtil.GetTagName(tagId);
                    }
                } else {
                    if (selectYear) {
                        dialogTitle = CoCoinUtil.GetSpendString(expense) + " in " + from.get(Calendar.YEAR) + "\n" +
                                "on " + CoCoinUtil.GetTagName(tagId);
                    } else {
                        dialogTitle = CoCoinUtil.GetSpendString(expense) + " in " + CoCoinUtil.GetMonthShort(from.get(Calendar.MONTH) + 1) + " " + from.get(Calendar.YEAR) + "\n" +
                                "on " + CoCoinUtil.GetTagName(tagId);
                    }
                }
                new GetData(from, to, tagId, dialogTitle).execute();
                break;
            case R.id.lowest_tags:
                if (gettingData) return;
                tagId = (int)lowestTagExpense.get(position + 1)[2];
                expense = (int)lowestTagExpense.get(position + 1)[0];
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    if (selectYear) {
                        dialogTitle = from.get(Calendar.YEAR) + "年" + "\n" +
                                CoCoinUtil.GetSpendString(expense) +
                                "于" + CoCoinUtil.GetTagName(tagId);
                    } else {
                        dialogTitle = from.get(Calendar.YEAR) + "年" + (from.get(Calendar.MONTH) + 1) + "月" + "\n" +
                                CoCoinUtil.GetSpendString(expense) +
                                "于" + CoCoinUtil.GetTagName(tagId);
                    }
                } else {
                    if (selectYear) {
                        dialogTitle = CoCoinUtil.GetSpendString(expense) + " in " + from.get(Calendar.YEAR) + "\n" +
                                "on " + CoCoinUtil.GetTagName(tagId);
                    } else {
                        dialogTitle = CoCoinUtil.GetSpendString(expense) + " in " + CoCoinUtil.GetMonthShort(from.get(Calendar.MONTH) + 1) + " " + from.get(Calendar.YEAR) + "\n" +
                                "on " + CoCoinUtil.GetTagName(tagId);
                    }
                }
                new GetData(from, to, tagId, dialogTitle).execute();
                break;
            case R.id.highest_month:
                if (gettingData) return;
                expense = (int)highestMonthExpense.get(position + 1)[3];
                month = (int)highestMonthExpense.get(position + 1)[1];
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    dialogTitle = from.get(Calendar.YEAR) + "年" + CoCoinUtil.getInstance().GetMonthShort(month + 1) + "\n" + CoCoinUtil.GetSpendString(expense);
                } else {
                    dialogTitle = CoCoinUtil.GetSpendString(expense) + "\nin " + from.get(Calendar.YEAR) + " " + CoCoinUtil.getInstance().GetMonthShort(month + 1);
                }
                tempFrom.set(reportYear, month, 1, 0, 0, 0);
                tempFrom.add(Calendar.SECOND, 0);
                tempTo.set(reportYear, month, tempFrom.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
                tempTo.add(Calendar.SECOND, 0);
                new GetData(tempFrom, tempTo, Integer.MIN_VALUE, dialogTitle).execute();
                break;
            case R.id.highest_days:
                if (gettingData) return;
                expense = (int)highestDayExpense.get(position + 1)[3];
                month = (int)highestDayExpense.get(position + 1)[1];
                day = (int)highestDayExpense.get(position + 1)[2];
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    dialogTitle = from.get(Calendar.YEAR) + "年" + CoCoinUtil.getInstance().GetMonthShort(month + 1) + day + CoCoinUtil.getInstance().GetWhetherFuck() + "\n" + CoCoinUtil.GetSpendString(expense);
                } else {
                    dialogTitle = CoCoinUtil.GetSpendString(expense) + "\nin " + from.get(Calendar.YEAR) + " " + CoCoinUtil.getInstance().GetMonthShort(month + 1) + " " + day;
                }
                tempFrom.set(reportYear, month, day, 0, 0, 0);
                tempFrom.add(Calendar.SECOND, 0);
                tempTo.set(reportYear, month, day, 23, 59, 59);
                tempTo.add(Calendar.SECOND, 0);
                new GetData(tempFrom, tempTo, Integer.MIN_VALUE, dialogTitle).execute();
                break;
            case R.id.lowest_days:
                if (gettingData) return;
                expense = (int)lowestDayExpense.get(position + 1)[3];
                month = (int)lowestDayExpense.get(position + 1)[1];
                day = (int)lowestDayExpense.get(position + 1)[2];
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    dialogTitle = from.get(Calendar.YEAR) + "年" + CoCoinUtil.getInstance().GetMonthShort(month + 1) + day + CoCoinUtil.getInstance().GetWhetherFuck() + "\n" + CoCoinUtil.GetSpendString(expense);
                } else {
                    dialogTitle = CoCoinUtil.GetSpendString(expense) + "\nin " + from.get(Calendar.YEAR) + " " + CoCoinUtil.getInstance().GetMonthShort(month + 1) + " " + day;
                }
                tempFrom.set(reportYear, month, day, 0, 0, 0);
                tempFrom.add(Calendar.SECOND, 0);
                tempTo.set(reportYear, month, day, 23, 59, 59);
                tempTo.add(Calendar.SECOND, 0);
                new GetData(tempFrom, tempTo, Integer.MIN_VALUE, dialogTitle).execute();
                break;
        }
    }

    // get select list for dialog
    private MaterialDialog progressDialog;
    public class GetSelectListData extends AsyncTask<String, Void, String> {

        private boolean openDialog;

        public GetSelectListData(boolean openDialog) {
            this.openDialog = openDialog;
            progressDialog = new MaterialDialog.Builder(mContext)
                    .title(R.string.report_loading_select_list_title)
                    .content(R.string.report_loading_select_list_content)
                    .cancelable(false)
                    .progress(true, 0)
                    .show();
        }
        @Override
        protected String doInBackground(String... params) {
            selectListData = new ArrayList<>();
            int size = RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.size();
            int currentYearSelectListPosition = -1;
            int currentMonthSelectListPosition = -1;
            int currentYear = -1;
            int currentMonth = -1;
            for (int i = size - 1; i >= 0; i--) {
                CoCoinRecord record = RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.get(i);
                if (record.getCalendar().get(Calendar.YEAR) != currentYear) {
                    double[] newYearSelectList = {record.getCalendar().get(Calendar.YEAR), -1, 1, record.getMoney()};
                    selectListData.add(newYearSelectList);
                    currentYearSelectListPosition = selectListData.size() - 1;
                    currentYear = record.getCalendar().get(Calendar.YEAR);
                    // if the year is different, we have to add new year and month
                    double[] newMonthSelectList = {record.getCalendar().get(Calendar.YEAR), record.getCalendar().get(Calendar.MONTH) + 1, 1, record.getMoney()};
                    selectListData.add(newMonthSelectList);
                    currentMonthSelectListPosition = selectListData.size() - 1;
                    currentMonth = record.getCalendar().get(Calendar.MONTH);
                } else {
                    if (record.getCalendar().get(Calendar.MONTH) != currentMonth) {
                        selectListData.get(currentYearSelectListPosition)[2]++;
                        selectListData.get(currentYearSelectListPosition)[3] += record.getMoney();
                        double[] newMonthSelectList = {record.getCalendar().get(Calendar.YEAR), record.getCalendar().get(Calendar.MONTH) + 1, 1, record.getMoney()};
                        selectListData.add(newMonthSelectList);
                        currentMonthSelectListPosition = selectListData.size() - 1;
                        currentMonth = record.getCalendar().get(Calendar.MONTH);
                    } else {
                        selectListData.get(currentYearSelectListPosition)[2]++;
                        selectListData.get(currentYearSelectListPosition)[3] += record.getMoney();
                        selectListData.get(currentMonthSelectListPosition)[2]++;
                        selectListData.get(currentMonthSelectListPosition)[3] += record.getMoney();
                    }
                }
            }
//            if (BuildConfig.DEBUG) {
//                for (int i = 0; i < selectListData.size(); i++) {
//                    Log.d("CoCoin", "Select List Data: " + selectListData.get(i)[0] + " " + selectListData.get(i)[1] + " " + selectListData.get(i)[2] + " " + selectListData.get(i)[3]);
//                }
//            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            if (progressDialog != null) progressDialog.cancel();
            if (openDialog) showSelectListDataDialog();
        }
    }

    private DialogSelectListDataAdapter selectListDataAdapter = null;
    private void showSelectListDataDialog() {
        if (selectListDataAdapter == null) {
            selectListDataAdapter = new DialogSelectListDataAdapter(selectListData);
        }
        new MaterialDialog.Builder(mContext)
                .title(R.string.report_select_list_title)
                .cancelable(false)
                .negativeText(R.string.cancel)
                .adapter(selectListDataAdapter,
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                dialog.dismiss();
                                makeReport(which);
                            }
                        })
                .show();
    }

    private boolean selectYear = false;
    private void makeReport(int p) {
        progressDialog = new MaterialDialog.Builder(mContext)
                .title(R.string.report_loading_select_list_title)
                .content(R.string.report_loading_select_list_content)
                .cancelable(false)
                .progress(true, 0)
                .show();
        if (selectListData.get(p)[1] == -1) {
            // select year
            selectYear = true;
            from.set((int)selectListData.get(p)[0], 0, 1, 0, 0, 0);
            from.add(Calendar.SECOND, 0);
            to.set((int)selectListData.get(p)[0], 11, 31, 23, 59, 59);
            to.add(Calendar.SECOND, 0);
            new GetReport(from, to, true).execute();
        } else {
            // select month
            selectYear = false;
            from.set((int)selectListData.get(p)[0], (int)selectListData.get(p)[1] - 1, 1, 0, 0, 0);
            from.add(Calendar.SECOND, 0);
            to.set((int)selectListData.get(p)[0], (int)selectListData.get(p)[1] - 1, from.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
            to.add(Calendar.SECOND, 0);
            new GetReport(from, to, false).execute();
        }
    }

    int reportYear = -1;
    int reportMonth = -1;
    double expense = 0;
    int records = 0;
    int tags = 0;
    PieChartData pieChartData = null;
    // expense, percent, tagId, records
    ArrayList<double[]> highestTagExpense = null;
    ArrayList<double[]> lowestTagExpense = null;
    // expense and percent on clothes, food, house and traffic
    ArrayList<double[]> needExpense = null;
    // expense, percent, tagId except the above four tags
    ArrayList<double[]> needlessExpense = null;
    LineChartData lineChartData = null;
    // year, month, day of month, expense and percent of diff months
    ArrayList<double[]> highestMonthExpense = null;
    ArrayList<double[]> lowestMonthExpense = null;
    double averageMonthExpense = -1;
    int averageMonthRecord;
    // year, month, day of month, expense and percent of diff days, most @param MAX_DAY_EXPENSE days
    ArrayList<double[]> highestDayExpense = null;
    ArrayList<double[]> lowestDayExpense = null;
    double averageDayExpense = -1;
    int averageDayRecord;
    public class GetReport extends AsyncTask<String, Void, String> {

        private Calendar from;
        private Calendar to;
        private boolean isYear;

        public GetReport(Calendar from, Calendar to, boolean isYear) {
            this.from = from;
            this.to = to;
            this.isYear = isYear;
        }
        @Override
        protected String doInBackground(String... params) {
            expense = 0;
            records = 0;
            tags = 0;
            highestTagExpense = new ArrayList<>();
            lowestTagExpense = new ArrayList<>();
            needExpense = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                double[] aTag = {0, 0};
                needExpense.add(aTag);
            }
            if (isYear) {
                highestMonthExpense = new ArrayList<>();
                lowestMonthExpense = new ArrayList<>();
            }
            highestDayExpense = new ArrayList<>();
            lowestDayExpense = new ArrayList<>();

            double[] tagExpense = new double[RecordManager.getInstance(CoCoinApplication.getAppContext()).TAGS.size() + 1];
            for (int i = tagExpense.length - 1; i >= 0; i--) tagExpense[i] = 0;
            double[] tagRecords = new double[RecordManager.getInstance(CoCoinApplication.getAppContext()).TAGS.size() + 1];
            for (int i = tagRecords.length - 1; i >= 0; i--) tagRecords[i] = 0;

            // month and expense
            ArrayList<double[]> monthExpense = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                double[] aMonth = {i, 0, 0};
                monthExpense.add(aMonth);
            }

            reportYear = to.get(Calendar.YEAR);
            reportMonth = to.get(Calendar.MONTH) + 1;

            // month, day and expense
            double[][] dayExpense = new double[12][32];
            for (int i = 0; i < 12; i++) {
                for (int j = 1; j <= 31; j++) {
                    dayExpense[i][j] = 0;
                }
            }
            // month, day and records
            double[][] dayRecord = new double[12][32];
            for (int i = 0; i < 12; i++) {
                for (int j = 1; j <= 31; j++) {
                    dayRecord[i][j] = 0;
                }
            }

            int size = RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.size();
            for (int i = size - 1; i >= 0; i--) {
                CoCoinRecord record = RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.get(i);
                if (record.getCalendar().before(from)) break;
                if (!record.getCalendar().after(to)) {
                    for (int j = i; j >= 0; j--) {
                        CoCoinRecord r = RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.get(j);
                        if (r.getCalendar().before(from)) {
                            break;
                        }
                        // here is the record we need
                        expense += r.getMoney();
                        records++;
                        tagExpense[r.getTag()] += r.getMoney();
                        tagRecords[r.getTag()]++;
                        if (isYear) {
                            monthExpense.get(r.getCalendar().get(Calendar.MONTH))[1] += r.getMoney();
                            monthExpense.get(r.getCalendar().get(Calendar.MONTH))[2]++;
                            dayExpense[r.getCalendar().get(Calendar.MONTH)][r.getCalendar().get(Calendar.DAY_OF_MONTH)] += r.getMoney();
                            dayRecord[r.getCalendar().get(Calendar.MONTH)][r.getCalendar().get(Calendar.DAY_OF_MONTH)]++;
                        } else {
                            dayExpense[r.getCalendar().get(Calendar.MONTH)][r.getCalendar().get(Calendar.DAY_OF_MONTH)] += r.getMoney();
                            dayRecord[r.getCalendar().get(Calendar.MONTH)][r.getCalendar().get(Calendar.DAY_OF_MONTH)]++;
                        }
                    }
                    break;
                }
            }

            for (int i = 0; i < tagExpense.length; i++) {
                if (tagExpense[i] != 0) {
                    int cfht = CoCoinUtil.IsCFHT(i);
                    if (cfht != -1) {
                        needExpense.get(cfht)[0] += tagExpense[i];
                    }
                    tags++;
                    double[] aTag = {tagExpense[i], tagExpense[i] / expense, i, tagRecords[i]};
                    highestTagExpense.add(aTag);
                    lowestTagExpense.add(aTag);
                }
            }
            for (int i = 0; i < 4; i++) needExpense.get(i)[1] = needExpense.get(i)[0] / expense;
            Collections.sort(highestTagExpense, new Comparator<double[]>() {
                @Override
                public int compare(double[] lhs, double[] rhs) {
                    return Double.compare(rhs[0], lhs[0]);
                }
            });
            Collections.sort(lowestTagExpense, new Comparator<double[]>() {
                @Override
                public int compare(double[] lhs, double[] rhs) {
                    return Double.compare(lhs[0], rhs[0]);
                }
            });
            // use tag expense values to generate pie data
            ArrayList<SliceValue> sliceValues = new ArrayList<>();
            for (int i = 0; i < lowestTagExpense.size(); i++) {
                SliceValue sliceValue = new SliceValue(
                        (float)(double)lowestTagExpense.get(i)[0], CoCoinUtil.GetTagColor((int)lowestTagExpense.get(i)[2]));
                sliceValue.setLabel(String.valueOf((int)lowestTagExpense.get(i)[2]));
                sliceValues.add(sliceValue);
            }
            pieChartData = new PieChartData(sliceValues);
            pieChartData.setHasLabels(false);
            pieChartData.setHasLabelsOnlyForSelected(false);
            pieChartData.setHasLabelsOutside(false);
            pieChartData.setHasCenterCircle(SettingManager.getInstance().getIsHollow());

            if (isYear) {
                Collections.sort(monthExpense, new Comparator<double[]>() {
                    @Override
                    public int compare(double[] lhs, double[] rhs) {
                        return Double.compare(rhs[1], lhs[1]);
                    }
                });
                for (int i = 0; i < 12; i++) {
                    double[] aMonth = {reportYear, monthExpense.get(i)[0], -1, monthExpense.get(i)[1], monthExpense.get(i)[1] / expense, monthExpense.get(i)[2]};
                    highestMonthExpense.add(aMonth);
                }
                for (int i = 11; i >= 0; i--) {
                    double[] aMonth = {reportYear, monthExpense.get(i)[0], -1, monthExpense.get(i)[1], monthExpense.get(i)[1] / expense, monthExpense.get(i)[2]};
                    lowestMonthExpense.add(aMonth);
                }

                averageMonthExpense = expense / 12;
                averageMonthRecord = records / 12;

                ArrayList<double[]> dayExpense2 = new ArrayList<>();
                for (int i = 0; i < 12; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(reportYear, i, 1, 0, 0, 0);
                    calendar.add(Calendar.SECOND, 0);
                    int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int j = 1; j <= dayOfMonth; j++) {
                        double[] aDay = {i, j, dayExpense[i][j], dayRecord[i][j]};
                        dayExpense2.add(aDay);
                    }
                }
                Collections.sort(dayExpense2, new Comparator<double[]>() {
                    @Override
                    public int compare(double[] lhs, double[] rhs) {
                        return Double.compare(rhs[2], lhs[2]);
                    }
                });
                for (int i = 0; i < MAX_DAY_EXPENSE; i++) {
                    if (i >= dayExpense2.size() || dayExpense2.get(i)[2] == 0) break;
                    double[] aDay = {reportYear, dayExpense2.get(i)[0], dayExpense2.get(i)[1], dayExpense2.get(i)[2], dayExpense2.get(i)[2] / expense, dayExpense2.get(i)[3]};
                    highestDayExpense.add(aDay);
                }
                int counter = min(dayExpense2.size(), MAX_DAY_EXPENSE);
                for (int i = dayExpense2.size() - 1; i >= 0; i--) {
                    if (dayExpense2.get(i)[2] > 0) {
                        double[] aDay = {reportYear, dayExpense2.get(i)[0], dayExpense2.get(i)[1], dayExpense2.get(i)[2], dayExpense2.get(i)[2] / expense, dayExpense2.get(i)[3]};
                        lowestDayExpense.add(aDay);
                        counter--;
                        if (counter == 0) break;
                    }
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(reportYear, 0, 1, 0, 0, 0);
                calendar.add(Calendar.SECOND, 0);
                averageDayExpense = expense / calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
                averageDayRecord = records / calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            } else {
                ArrayList<double[]> dayExpense2 = new ArrayList<>();
                for (int i = 0; i < 12; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(reportYear, i, 1, 0, 0, 0);
                    calendar.add(Calendar.SECOND, 0);
                    int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int j = 1; j <= dayOfMonth; j++) {
                        double[] aDay = {i, j, dayExpense[i][j], dayRecord[i][j]};
                        dayExpense2.add(aDay);
                    }
                }
                Collections.sort(dayExpense2, new Comparator<double[]>() {
                    @Override
                    public int compare(double[] lhs, double[] rhs) {
                        return Double.compare(rhs[2], lhs[2]);
                    }
                });
                for (int i = 0; i < MAX_DAY_EXPENSE; i++) {
                    if (i >= dayExpense2.size() || dayExpense2.get(i)[2] == 0) break;
                    double[] aDay = {reportYear, dayExpense2.get(i)[0], dayExpense2.get(i)[1], dayExpense2.get(i)[2], dayExpense2.get(i)[2] / expense, dayExpense2.get(i)[3]};
                    highestDayExpense.add(aDay);
                }
                int counter = min(dayExpense2.size(), MAX_DAY_EXPENSE);
                for (int i = dayExpense2.size() - 1; i >= 0; i--) {
                    if (dayExpense2.get(i)[2] > 0) {
                        double[] aDay = {reportYear, dayExpense2.get(i)[0], dayExpense2.get(i)[1], dayExpense2.get(i)[2], dayExpense2.get(i)[2] / expense, dayExpense2.get(i)[3]};
                        lowestDayExpense.add(aDay);
                        counter--;
                        if (counter == 0) break;
                    }
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(reportYear, reportMonth - 1, 1, 0, 0, 0);
                calendar.add(Calendar.SECOND, 0);
                averageDayExpense = expense / calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                averageDayRecord = records / calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            // use month/day expense values to generate line data
            if (isYear) {
                List<Line> lines = new ArrayList<>();
                for (int i = 0; i < 1; ++i) {

                    List<PointValue> values = new ArrayList<>();
                    for (int j = 0; j < 12; ++j) {
                        for (int k = 0; k < 12; k++) {
                            if (monthExpense.get(k)[0] == j) {
                                values.add(new PointValue(j, (float)monthExpense.get(k)[1]));
                                break;
                            }
                        }
                    }

                    Line line = new Line(values);
                    line.setColor(ContextCompat.getColor(CoCoinApplication.getAppContext(), R.color.red));
                    line.setShape(ValueShape.CIRCLE);
                    line.setCubic(false);
                    line.setFilled(false);
                    line.setHasLabels(false);
                    line.setHasLabelsOnlyForSelected(false);
                    line.setHasLines(true);
                    line.setHasPoints(true);
                    lines.add(line);
                }

                lineChartData = new LineChartData(lines);

                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                axisX.setName(reportYear + "");
                lineChartData.setAxisXBottom(axisX);
                lineChartData.setAxisYLeft(axisY);

                ArrayList<AxisValue> axisValues = new ArrayList<>();
                for (int i = 0; i < 12; i++) {
                    AxisValue axisValue = new AxisValue(i);
                    axisValue.setLabel((i + 1) + "");
                    axisValues.add(axisValue);
                }
                lineChartData.getAxisXBottom().setValues(axisValues);
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.set(reportYear, reportMonth - 1, 1, 0, 0, 0);
                calendar.add(Calendar.SECOND, 0);
                int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                List<Line> lines = new ArrayList<>();
                for (int i = 0; i < 1; ++i) {

                    List<PointValue> values = new ArrayList<>();
                    for (int j = 0; j < days; ++j) {
                        values.add(new PointValue(j, (float)dayExpense[reportMonth - 1][j + 1]));
                    }

                    Line line = new Line(values);
                    line.setColor(ContextCompat.getColor(CoCoinApplication.getAppContext(), R.color.red));
                    line.setShape(ValueShape.CIRCLE);
                    line.setCubic(false);
                    line.setFilled(false);
                    line.setHasLabels(false);
                    line.setHasLabelsOnlyForSelected(false);
                    line.setHasLines(true);
                    line.setHasPoints(true);
                    lines.add(line);
                }

                lineChartData = new LineChartData(lines);

                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                axisX.setName(reportYear + " " + CoCoinUtil.GetMonthShort(reportMonth));
                lineChartData.setAxisXBottom(axisX);
                lineChartData.setAxisYLeft(axisY);

                ArrayList<AxisValue> axisValues = new ArrayList<>();
                for (int i = 0; i < days; i++) {
                    AxisValue axisValue = new AxisValue(i);
                    axisValue.setLabel((i + 1) + "");
                    axisValues.add(axisValue);
                }
                lineChartData.getAxisXBottom().setValues(axisValues);
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {

            // for title
            if (isYear) REPORT_TITLE = reportYear + "";
            else REPORT_TITLE = reportYear + " - " + reportMonth;

            try {
                ((OnTitleChangedListener)activity)
                        .onTitleChanged();
            } catch (ClassCastException cce){
                cce.printStackTrace();
            }

            // for title
            if (selectYear) {
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    title.setText(" ● " + reportYear + "年" + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_foot));
                } else {
                    title.setText(" ● " + reportYear + " " + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_foot));
                }
            } else {
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    title.setText(" ● " + reportYear + "年" + CoCoinUtil.getInstance().GetMonthShort(reportMonth) + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_foot));
                } else {
                    title.setText(" ● " + reportYear + " " + CoCoinUtil.getInstance().GetMonthShort(reportMonth) + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_foot));
                }
            }

            // for basic information
            expenseTV.setText(CoCoinUtil.getInstance().GetInMoney((int)expense));
            if ("zh".equals(CoCoinUtil.GetLanguage())) {
                tagsTV.setText(records + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_records) + tags + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_tags));
            } else {
                tagsTV.setText(records + " " + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_records) + " " + tags + " " + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_tags));
            }
            emptyTip.setVisibility(View.GONE);

            // for pie
            pieLayout.setVisibility(View.VISIBLE);
            pie.setVisibility(View.VISIBLE);
            pie.setPieChartData(pieChartData);

            // for highest tag expense
            highestTagLayout.setVisibility(View.VISIBLE);
            highestTagIcon.setImageDrawable(CoCoinUtil.GetTagIconDrawable((int)highestTagExpense.get(0)[2]));
            highestTagText.setText(CoCoinUtil.GetTagName((int)highestTagExpense.get(0)[2]) + CoCoinUtil.getInstance().GetPurePercentString(highestTagExpense.get(0)[1] * 100));
            highestTagExpenseTV.setText(CoCoinUtil.GetInMoney((int)highestTagExpense.get(0)[0]));
            highestTagRecord.setText(CoCoinUtil.GetInRecords((int)highestTagExpense.get(0)[3]));
            highestTagsAdapter = new ReportTagAdapter(highestTagExpense);
            highestTags.setAdapter(highestTagsAdapter);

            // for lowest tag expense
            lowestTagLayout.setVisibility(View.VISIBLE);
            lowestTagIcon.setImageDrawable(CoCoinUtil.GetTagIconDrawable((int)lowestTagExpense.get(0)[2]));
            lowestTagText.setText(CoCoinUtil.GetTagName((int)lowestTagExpense.get(0)[2]) + CoCoinUtil.getInstance().GetPurePercentString(lowestTagExpense.get(0)[1] * 100));
            lowestTagExpenseTV.setText(CoCoinUtil.GetInMoney((int)lowestTagExpense.get(0)[0]));
            lowestTagRecord.setText(CoCoinUtil.GetInRecords((int)lowestTagExpense.get(0)[3]));
            lowestTagsAdapter = new ReportTagAdapter(lowestTagExpense);
            lowestTags.setAdapter(lowestTagsAdapter);

            // for line
            lineLayout.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            line.setLineChartData(lineChartData);

            // for month
            if (selectYear) {
                highestMonthLayout.setVisibility(View.VISIBLE);
                highestFirstIcon.setBackgroundResource(getBackgroundResource());
                highestFirstIcon.setText(((int)highestMonthExpense.get(0)[1] + 1) + "");
                highestFirstText.setText(CoCoinUtil.GetMonthShort((int)highestMonthExpense.get(0)[1] + 1) + " " + reportYear + CoCoinUtil.getInstance().GetPurePercentString(highestMonthExpense.get(0)[4] * 100));
                highestFirstExpenseTV.setText(CoCoinUtil.GetInMoney((int)highestMonthExpense.get(0)[3]));
                highestFirstRecord.setText(CoCoinUtil.GetInRecords((int)highestMonthExpense.get(0)[5]));
                highestLastIcon.setBackgroundResource(getBackgroundResource());
                highestLastIcon.setText(((int)highestMonthExpense.get(11)[1] + 1) + "");
                highestLastText.setText(CoCoinUtil.GetMonthShort((int)highestMonthExpense.get(11)[1] + 1) + " " + reportYear + CoCoinUtil.getInstance().GetPurePercentString(highestMonthExpense.get(11)[4] * 100));
                highestLastExpenseTV.setText(CoCoinUtil.GetInMoney((int)highestMonthExpense.get(11)[3]));
                highestLastRecord.setText(CoCoinUtil.GetInRecords((int)highestMonthExpense.get(11)[5]));
                highestMonthsAdapter = new ReportMonthAdapter(highestMonthExpense, reportYear);
                highestMonths.setAdapter(highestMonthsAdapter);

                // for average day expense
                averageMonthExpenseTV.setText(CoCoinUtil.getInstance().GetInMoney((int)averageMonthExpense));
                averageMonthRecordTV.setText(CoCoinUtil.getInstance().GetInRecords(averageMonthRecord));
            } else {
                highestMonthLayout.setVisibility(View.GONE);
            }

            // for highest day expense
            highestDayLayout.setVisibility(View.VISIBLE);
            highestDayIcon.setBackgroundResource(getBackgroundResource());
            highestDayIcon.setText((int)highestDayExpense.get(0)[2] + "");
            highestDayText.setText(CoCoinUtil.getInstance().GetCalendarStringDayExpenseSort(CoCoinApplication.getAppContext(), (int)highestDayExpense.get(0)[0], (int)highestDayExpense.get(0)[1] + 1, (int)highestDayExpense.get(0)[2]) + CoCoinUtil.getInstance().GetPurePercentString(highestDayExpense.get(0)[4] * 100));
            highestDayExpenseTV.setText(CoCoinUtil.GetInMoney((int)highestDayExpense.get(0)[3]));
            highestDayRecord.setText(CoCoinUtil.GetInRecords((int)highestDayExpense.get(0)[5]));
            highestDaysAdapter = new ReportDayAdapter(highestDayExpense, reportMonth);
            highestDays.setAdapter(highestDaysAdapter);

            // for lowest day expense
            lowestDayLayout.setVisibility(View.VISIBLE);
            lowestDayIcon.setBackgroundResource(getBackgroundResource());
            lowestDayIcon.setText((int)lowestDayExpense.get(0)[2] + "");
            lowestDayText.setText(CoCoinUtil.getInstance().GetCalendarStringDayExpenseSort(CoCoinApplication.getAppContext(), (int)lowestDayExpense.get(0)[0], (int)lowestDayExpense.get(0)[1] + 1, (int)lowestDayExpense.get(0)[2]) + CoCoinUtil.getInstance().GetPurePercentString(lowestDayExpense.get(0)[4] * 100));
            lowestDayExpenseTV.setText(CoCoinUtil.GetInMoney((int)lowestDayExpense.get(0)[3]));
            lowestDayRecord.setText(CoCoinUtil.GetInRecords((int)lowestDayExpense.get(0)[5]));
            lowestDaysAdapter = new ReportDayAdapter(lowestDayExpense, reportMonth);
            lowestDays.setAdapter(lowestDaysAdapter);

            // for average day expense
            averageDayExpenseTV.setText(CoCoinUtil.getInstance().GetInMoney((int)averageDayExpense));
            averageDayRecordTV.setText(CoCoinUtil.getInstance().GetInRecords(averageDayRecord));

            // for foot
            foot.setVisibility(View.VISIBLE);
            if (selectYear) {
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    foot.setText(" ● " + reportYear + "年" + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_foot));
                } else {
                    foot.setText(" ● " + reportYear + " " + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_foot));
                }
            } else {
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    foot.setText(" ● " + reportYear + "年" + CoCoinUtil.getInstance().GetMonthShort(reportMonth) + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_foot));
                } else {
                    foot.setText(" ● " + reportYear + " " + CoCoinUtil.getInstance().GetMonthShort(reportMonth) + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_foot));
                }
            }

            if (progressDialog != null) progressDialog.dismiss();
        }
    }

    private ArrayList<CoCoinRecord> selectedRecord;
    private boolean gettingData = false;
    public class GetData extends AsyncTask<String, Void, String> {

        private Calendar fromDate;
        private Calendar toDate;
        private int tagId;
        private String dialogTitle;

        public GetData(Calendar fromDate, Calendar toDate, int tagId, String dialogTitle) {
            gettingData = true;
            this.fromDate = fromDate;
            this.tagId = tagId;
            this.toDate = toDate;
            this.dialogTitle = dialogTitle;
            progressDialog = new MaterialDialog.Builder(mContext)
                    .title(R.string.report_loading_select_list_title)
                    .content(R.string.report_loading_select_list_content)
                    .cancelable(false)
                    .progress(true, 0)
                    .show();
        }

        @Override
        protected String doInBackground(String... params) {
            selectedRecord = new ArrayList<>();
            int size = RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.size();
            for (int i = size - 1; i >= 0; i--) {
                CoCoinRecord record = RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.get(i);
                if (record.getCalendar().before(fromDate)) break;
                if (!record.getCalendar().after(toDate)) {
                    for (int j = i; j >= 0; j--) {
                        CoCoinRecord r = RecordManager.getInstance(CoCoinApplication.getAppContext()).RECORDS.get(j);
                        if (r.getCalendar().before(fromDate)) {
                            break;
                        }
                        if (tagId == Integer.MIN_VALUE || r.getTag() == tagId) selectedRecord.add(r);
                    }
                    break;
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            gettingData = false;
            if (progressDialog != null) progressDialog.dismiss();
            ((FragmentActivity)mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .add(new RecordCheckDialogFragment(
                            mContext, selectedRecord, dialogTitle), "MyDialog")
                    .commit();
        }
    }

    public interface OnTitleChangedListener {
        void onTitleChanged();
    }

    private int getBackgroundResource() {
        Random random = new Random();
        switch (random.nextInt(6)) {
            case 0: return R.drawable.bg_month_icon_big_0;
            case 1: return R.drawable.bg_month_icon_big_1;
            case 2: return R.drawable.bg_month_icon_big_2;
            case 3: return R.drawable.bg_month_icon_big_3;
            case 4: return R.drawable.bg_month_icon_big_4;
            case 5: return R.drawable.bg_month_icon_big_5;
            default:return R.drawable.bg_month_icon_big_0;
        }
    }

    private int min(int a, int b) {
        return (a < b ? a : b);
    }
}
