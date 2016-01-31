package com.nightonke.saver.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.melnykov.fab.FloatingActionButton;
import com.nightonke.saver.R;
import com.nightonke.saver.activity.CoCoinApplication;
import com.nightonke.saver.adapter.DialogMonthSelectGridViewAdapter;
import com.nightonke.saver.adapter.DialogSelectListDataAdapter;
import com.nightonke.saver.adapter.ReportTagAdapter;
import com.nightonke.saver.model.CoCoinRecord;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.ui.ExpandedListView;
import com.nightonke.saver.ui.MyGridView;
import com.nightonke.saver.util.CoCoinUtil;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.squareup.leakcanary.RefWatcher;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
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
    private TextView emptyTip;
    private TextView tagsTV;

    private Calendar from = Calendar.getInstance();
    private Calendar to = Calendar.getInstance();

    private SuperToast superToast;

    private PieChartView pie;
    private LineChartView line;

    private MaterialIconView iconRight;
    private MaterialIconView iconLeft;
    private MaterialIconView all;

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

    // select list
    // year, month(-1 means the whole year), records, expenses
    private ArrayList<double[]> selectListData = null;

    // highest tag list
    private LinearLayout highestTagLayout;
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
    private ImageView lowestTagIcon;
    private TextView lowestTagText;
    private TextView lowestTagExpenseTV;
    private TextView lowestTagRecord;
    private ExpandedListView lowestTags;
    private ReportTagAdapter lowestTagsAdapter;
    private ExpandableRelativeLayout lowestTagsLayout;
    private LinearLayout lowestTagMore;
    private TextView lowestTagMoreText;

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
    private DialogMonthSelectGridViewAdapter dialogMonthSelectGridViewAdapter;
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

        pie = (PieChartView)view.findViewById(R.id.chart_pie);
        pie.setVisibility(View.INVISIBLE);

        iconRight = (MaterialIconView)view.findViewById(R.id.icon_right);
        iconLeft = (MaterialIconView)view.findViewById(R.id.icon_left);
        iconRight.setVisibility(View.INVISIBLE);
        iconLeft.setVisibility(View.INVISIBLE);

//        all = (MaterialIconView)view.findViewById(R.id.all);
//        all.setVisibility(View.INVISIBLE);

        emptyTip = (TextView)view.findViewById(R.id.empty_tip);
        emptyTip.setTypeface(CoCoinUtil.GetTypeface());

        highestTagLayout = (LinearLayout)view.findViewById(R.id.highest_tag_layout);
        highestTagLayout.setVisibility(View.GONE);
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

    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;

        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);

        options.setDisplayOptions(displayOptions);

        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.button:
                showSelectListDataDialog();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String dialogTitle = "";
        int sum;
        int tagId;
        switch (parent.getId()) {
            case R.id.highest_tags:
                if (gettingData) return;
                tagId = (int)highestTagExpense.get(position + 1)[2];
                sum = (int)highestTagExpense.get(position + 1)[0];
                if ("zh".equals(CoCoinUtil.GetLanguage())) {
                    if (selectYear) {
                        dialogTitle = from.get(Calendar.YEAR) + "年" + "\n" +
                                CoCoinUtil.GetSpendString(sum) +
                                "于" + CoCoinUtil.GetTagName(tagId);
                    } else {
                        dialogTitle = from.get(Calendar.YEAR) + "年" + (from.get(Calendar.MONTH) + 1) + "月" + "\n" +
                                CoCoinUtil.GetSpendString(sum) +
                                "于" + CoCoinUtil.GetTagName(tagId);
                    }
                } else {
                    if (selectYear) {
                        dialogTitle = CoCoinUtil.GetSpendString(sum) + " in " + from.get(Calendar.YEAR) + "\n" +
                                "on " + CoCoinUtil.GetTagName(tagId);
                    } else {
                        dialogTitle = CoCoinUtil.GetSpendString(sum) + " in " + CoCoinUtil.GetMonthShort(from.get(Calendar.MONTH) + 1) + " " + from.get(Calendar.YEAR) + "\n" +
                                "on " + CoCoinUtil.GetTagName(tagId);
                    }
                }
                new GetData(from, to, tagId, dialogTitle).execute();
                break;
        }
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
    // year, month, day of month, expense and percent of diff days, most @param MAX_DAY_EXPENSE days
    ArrayList<double[]> highestDayExpense = null;
    ArrayList<double[]> lowestDayExpense = null;
    double averageDayExpense = -1;
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
                double[] aMonth = {i, 0};
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
                            dayExpense[r.getCalendar().get(Calendar.MONTH)][r.getCalendar().get(Calendar.DAY_OF_MONTH)] += r.getMoney();
                        } else {
                            dayExpense[r.getCalendar().get(Calendar.MONTH)][r.getCalendar().get(Calendar.DAY_OF_MONTH)] += r.getMoney();
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
            for (int i = 0; i < highestTagExpense.size(); i++) {
                SliceValue sliceValue = new SliceValue(
                        (float)(double)highestTagExpense.get(i)[0], CoCoinUtil.GetTagColor((int)highestTagExpense.get(i)[2]));
                sliceValue.setLabel(String.valueOf((int)highestTagExpense.get(i)[2]));
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
                    double[] aMonth = {reportYear, monthExpense.get(i)[0], -1, monthExpense.get(i)[1], monthExpense.get(i)[1] / expense};
                    highestMonthExpense.add(aMonth);
                }
                for (int i = 11; i >= 0; i--) {
                    double[] aMonth = {reportYear, monthExpense.get(i)[0], -1, monthExpense.get(i)[1], monthExpense.get(i)[1] / expense};
                    lowestMonthExpense.add(aMonth);
                }

                averageMonthExpense = expense / 12;

                ArrayList<double[]> dayExpense2 = new ArrayList<>();
                for (int i = 0; i < 12; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(reportYear, i, 1, 0, 0, 0);
                    calendar.add(Calendar.SECOND, 0);
                    int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int j = 0; j < dayOfMonth; j++) {
                        double[] aDay = {i, j, dayExpense[i][j]};
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
                    double[] aDay = {reportYear, dayExpense2.get(i)[0], dayExpense2.get(i)[1], dayExpense2.get(i)[2], dayExpense2.get(i)[2] / expense};
                    highestDayExpense.add(aDay);
                }
                for (int i = dayExpense2.size() - 1; i >= dayExpense2.size() - MAX_DAY_EXPENSE; i--) {
                    if (i < 0 || dayExpense2.get(i)[2] == 0) break;
                    double[] aDay = {reportYear, dayExpense2.get(i)[0], dayExpense2.get(i)[1], dayExpense2.get(i)[2], dayExpense2.get(i)[2] / expense};
                    lowestDayExpense.add(aDay);
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(reportYear, 0, 1, 0, 0, 0);
                calendar.add(Calendar.SECOND, 0);
                averageDayExpense = expense / calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            } else {
                ArrayList<double[]> dayExpense2 = new ArrayList<>();
                for (int i = 0; i < 12; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(reportYear, i, 1, 0, 0, 0);
                    calendar.add(Calendar.SECOND, 0);
                    int dayOfMonth = calendar.getActualMaximum(Calendar.MONTH);
                    for (int j = 0; j < dayOfMonth; j++) {
                        double[] aDay = {i, j, dayExpense[i][j]};
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
                    double[] aDay = {reportYear, dayExpense2.get(i)[0], dayExpense2.get(i)[1], dayExpense2.get(i)[2], dayExpense2.get(i)[2] / expense};
                    highestDayExpense.add(aDay);
                }
                for (int i = dayExpense2.size() - 1; i >= dayExpense2.size() - MAX_DAY_EXPENSE; i--) {
                    if (i < 0 || dayExpense2.get(i)[2] == 0) break;
                    double[] aDay = {reportYear, dayExpense2.get(i)[0], dayExpense2.get(i)[1], dayExpense2.get(i)[2], dayExpense2.get(i)[2] / expense};
                    lowestDayExpense.add(aDay);
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(reportYear, reportMonth - 1, 1, 0, 0, 0);
                calendar.add(Calendar.SECOND, 0);
                averageDayExpense = expense / calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            // use month/day expense values to generate line data
            if (isYear) {
                List<Line> lines = new ArrayList<>();
                for (int i = 0; i < 1; ++i) {

                    List<PointValue> values = new ArrayList<>();
                    for (int j = 0; j < 12; ++j) {
                        values.add(new PointValue(j, (float)monthExpense.get(j)[1]));
                    }

                    Line line = new Line(values);
                    line.setColor(ChartUtils.COLORS[i]);
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
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.set(reportYear, reportMonth - 1, 1, 0, 0, 0);
                calendar.add(Calendar.SECOND, 0);
                int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                List<Line> lines = new ArrayList<>();
                for (int i = 0; i < 1; ++i) {

                    List<PointValue> values = new ArrayList<>();
                    for (int j = 1; j <= days; ++j) {
                        values.add(new PointValue(j, (float)dayExpense[reportMonth - 1][j]));
                    }

                    Line line = new Line(values);
                    line.setColor(ChartUtils.COLORS[i]);
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
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            if (progressDialog != null) progressDialog.dismiss();

            // for title
            if (isYear) REPORT_TITLE = reportYear + "";
            else REPORT_TITLE = reportYear + " - " + reportMonth;

            try {
                ((OnTitleChangedListener)activity)
                        .onTitleChanged();
            } catch (ClassCastException cce){
                cce.printStackTrace();
            }

            // for basic information
            expenseTV.setText(CoCoinUtil.getInstance().GetInMoney((int)expense));
            tagsTV.setText(records + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_records) + tags + CoCoinApplication.getAppContext().getResources().getString(R.string.report_view_tags));

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
                        if (r.getTag() == tagId) selectedRecord.add(r);
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
}
