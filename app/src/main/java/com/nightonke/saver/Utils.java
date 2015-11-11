package com.nightonke.saver;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by 伟平 on 2015/10/16.
 */

// Todo delete record method
// Todo make Utils singleton

public class Utils {

    public static String PASSWORD = "1234";

    public static int[] WEEKDAY_SHORT_START_ON_MONDAY = {0, R.string.monday_short, R.string.tuesday_short, R.string.wednesday_short, R.string.thursday_short, R.string.friday_short, R.string.saturday_short, R.string.sunday_short};

    public static int[] WEEKDAY_SHORT_START_ON_SUNDAY = {0, R.string.sunday_short, R.string.monday_short, R.string.tuesday_short, R.string.wednesday_short, R.string.thursday_short, R.string.friday_short, R.string.saturday_short};

    public static int[] WEEKDAY_START_ON_MONDAY = {0, R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday, R.string.sunday};

    public static int[] WEEKDAY_START_ON_SUNDAY = {0, R.string.sunday, R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday};

    public static String[] FLOATINGLABELS = {"", "", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿"};

    public static String[] BUTTONS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "D", "0", "Y"};

    public static int[] TODAY_VIEW_EMPTY_TIP = {R.string.today_empty, R.string.yesterday_empty, R.string.this_week_empty, R.string.last_week_empty, R.string.this_month_empty, R.string.last_month_empty, R.string.this_year_empty, R.string.last_year_empty};

    public static int[] MONTHS_SHORT = {0, R.string.january_short, R.string.february_short, R.string.march_short, R.string.april_short, R.string.may_short, R.string.june_short, R.string.july_short, R.string.august_short, R.string.september_short, R.string.october_short, R.string.november_short, R.string.december_short};

    public static int[] MONTHS = {0, R.string.january, R.string.february, R.string.march, R.string.april, R.string.may, R.string.june, R.string.july, R.string.august, R.string.september, R.string.october, R.string.november, R.string.december};

    public static int TODAY_VIEW_TITLE[] = {R.string.today_view_today, R.string.today_view_yesterday, R.string.today_view_this_week, R.string.today_view_last_week, R.string.today_view_this_month, R.string.today_view_last_month, R.string.today_view_this_year, R.string.today_view_last_year};

    public static int[] TAG_ICON = {
            R.drawable.sum_pie_icon,
            R.drawable.sum_histogram_icon,
            R.drawable.meal_icon,
            R.drawable.closet_icon,
            R.drawable.home_icon,
            R.drawable.traffic_icon,
            R.drawable.vehicle_maintenance_icon,
            R.drawable.book_icon,
            R.drawable.hobby_icon,
            R.drawable.internet_icon,
            R.drawable.friend_icon,
            R.drawable.education_icon,
            R.drawable.entertainment_icon,
            R.drawable.medical_icon,
            R.drawable.insurance_icon,
            R.drawable.donation_icon,
            R.drawable.sport_icon,
            R.drawable.snack_icon,
            R.drawable.music_icon,
            R.drawable.fund_icon,
            R.drawable.drink_icon,
            R.drawable.fruit_icon,
            R.drawable.film_icon,
            R.drawable.baby_icon,
            R.drawable.partner_icon,
            R.drawable.housing_loan_icon,
            R.drawable.pet_icon,
            R.drawable.telephone_bill_icon,
            R.drawable.travel_icon
    };

    public static int[] TAG_NAME = {
            R.string.tag_sum_pie,
            R.string.tag_sum_histogram,
            R.string.tag_meal,
            R.string.tag_closet,
            R.string.tag_home,
            R.string.tag_traffic,
            R.string.tag_vehicle_maintenance,
            R.string.tag_book,
            R.string.tag_hobby,
            R.string.tag_internet,
            R.string.tag_friend,
            R.string.tag_education,
            R.string.tag_entertainment,
            R.string.tag_medical,
            R.string.tag_insurance,
            R.string.tag_donation,
            R.string.tag_sport,
            R.string.tag_snack,
            R.string.tag_music,
            R.string.tag_fund,
            R.string.tag_drink,
            R.string.tag_fruit,
            R.string.tag_film,
            R.string.tag_baby,
            R.string.tag_partner,
            R.string.tag_housing_loan,
            R.string.tag_pet,
            R.string.tag_telephone_bill,
            R.string.tag_travel
    };

    public static Typeface typefaceLatoRegular;
    public static Typeface typefaceLatoHairline;
    public static Typeface typefaceLatoLight;
    public static Typeface typefaceSourceHanSansExtraLight;

    private Utils() {
        Log.d("Saver", "Utils create");
    }

    public static void init(Context context) {

        typefaceLatoRegular = Typeface.createFromAsset(
                context.getAssets(), "fonts/Lato-Regular.ttf");
        typefaceLatoHairline = Typeface.createFromAsset(
                context.getAssets(), "fonts/Lato-Hairline.ttf");
        typefaceLatoLight = Typeface.createFromAsset(
                context.getAssets(), "fonts/LatoLatin-Light.ttf");
        typefaceSourceHanSansExtraLight
                = Typeface.createFromAsset(
                context.getAssets(), "fonts/SourceHanSansCN-ExtraLight.otf");
        relativeSizeSpan = new RelativeSizeSpan(2f);
        redForegroundSpan = new ForegroundColorSpan(Color.parseColor("#ff5252"));
        greenForegroundSpan = new ForegroundColorSpan(Color.parseColor("#4ca550"));
        whiteForegroundSpan = new ForegroundColorSpan(Color.parseColor("#ffffff"));

        lastColor0 = "";
        lastColor1 = "";
        lastColor2 = "";

        random = new Random();
    }

    public static Typeface GetTypeface() {
        if ("en".equals(Locale.getDefault().getLanguage()))
            return typefaceLatoLight;
        if ("zh".equals(Locale.getDefault().getLanguage()))
            return Typeface.DEFAULT;
        return typefaceLatoLight;
    }

    public static String GetLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String GetWhetherBlank() {
        if ("zh".equals(Locale.getDefault().getLanguage()))
            return "";
        else
            return " ";
    }

    public static String GetWhetherFuck() {
        if ("zh".equals(Locale.getDefault().getLanguage()))
            return "日";
        else
            return "";
    }

    public static String GetSpendString(int money) {
        if ("zh".equals(Locale.getDefault().getLanguage()))
            return "消费 ¥" + money;
        else
            return "Spend $" + money + " ";
    }

    public static String GetPercentString(double percent) {
        if ("zh".equals(Locale.getDefault().getLanguage()))
            return " (占" + String.format("%.2f", percent) + "%)";
        else
            return " (takes " + String.format("%.2f", percent) + "%)";
    }

    public static String GetTodayViewTitle(int fragmentPosition) {
        return myApplication.getAppContext().getString(TODAY_VIEW_TITLE[fragmentPosition]);
    }

    public static boolean WEEK_START_WITH_SUNDAY = false;

    public static String GetAxisDateName(int type, int position) {
        switch (type) {
            case Calendar.HOUR_OF_DAY:
                return position + "";
            case Calendar.DAY_OF_WEEK:
                if (WEEK_START_WITH_SUNDAY) return myApplication.getAppContext().getResources().getString(WEEKDAY_SHORT_START_ON_SUNDAY[position + 1]);
                else return myApplication.getAppContext().getResources().getString(WEEKDAY_SHORT_START_ON_MONDAY[position + 1]);
            case Calendar.DAY_OF_MONTH:
                return (position + 1) + "";
            case Calendar.MONTH:
                return myApplication.getAppContext().getResources().getString(MONTHS_SHORT[position + 1]);
            default:
                return "";
        }
    }

    public static int GetTodayViewEmptyTip(int fragmentPosition) {
        return TODAY_VIEW_EMPTY_TIP[fragmentPosition];
    }

    public static String GetMonthShort(int i) {
        return myApplication.getAppContext().getResources().getString(MONTHS_SHORT[i]);
    }

    public static String GetMonth(int i) {
        return myApplication.getAppContext().getResources().getString(MONTHS[i]);
    }

    public static String GetWeekDay(int position) {
        if (WEEK_START_WITH_SUNDAY) return myApplication.getAppContext().getResources().getString(WEEKDAY_START_ON_SUNDAY[position + 1]);
        else return myApplication.getAppContext().getResources().getString(WEEKDAY_START_ON_MONDAY[position + 1]);
    }

    public static Calendar GetTodayLeftRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static Calendar GetTodayRightRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static Calendar GetYesterdayLeftRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static Calendar GetYesterdayRightRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static Calendar GetThisWeekLeftRange(Calendar today) {
        int nowDayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 0);
        if (Utils.WEEK_START_WITH_SUNDAY) {
            int[] diff = new int[]{0, 0, -1, -2, -3, -4, -5, -6};
            calendar.add(Calendar.DATE, diff[nowDayOfWeek]);
        } else {
            int[] diff = new int[]{0, -6, 0, -1, -2, -3, -4, -5};
            calendar.add(Calendar.DATE, diff[nowDayOfWeek]);
        }
        return calendar;
    }

    public static Calendar GetThisWeekRightRange(Calendar today) {
            Calendar calendar = (Calendar) GetThisWeekLeftRange(today).clone();
            calendar.add(Calendar.DATE, 7);
        return calendar;
    }

    public static Calendar GetLastWeekLeftRange(Calendar today) {
        int nowDayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 0);
        if (Utils.WEEK_START_WITH_SUNDAY) {
            int[] diff = new int[]{0, 0, -1, -2, -3, -4, -5, -6};
            calendar.add(Calendar.DATE, diff[nowDayOfWeek] - 7);
        } else {
            int[] diff = new int[]{0, -6, 0, -1, -2, -3, -4, -5};
            calendar.add(Calendar.DATE, diff[nowDayOfWeek] - 7);
        }
        return calendar;
    }

    public static Calendar GetLastWeekRightRange(Calendar today) {
        Calendar calendar = (Calendar) GetLastWeekLeftRange(today).clone();
        calendar.add(Calendar.DATE, 7);
        return calendar;
    }

    public static Calendar GetNextWeekLeftRange(Calendar today) {
        int nowDayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 0);
        if (Utils.WEEK_START_WITH_SUNDAY) {
            int[] diff = new int[]{0, 0, -1, -2, -3, -4, -5, -6};
            calendar.add(Calendar.DATE, diff[nowDayOfWeek] + 7);
        } else {
            int[] diff = new int[]{0, -6, 0, -1, -2, -3, -4, -5};
            calendar.add(Calendar.DATE, diff[nowDayOfWeek] + 7);
        }
        return calendar;
    }

    public static Calendar GetNextWeekRightRange(Calendar today) {
        Calendar calendar = (Calendar) GetNextWeekLeftRange(today).clone();
        calendar.add(Calendar.DATE, 7);
        return calendar;
    }

    public static Calendar GetNextWeekRightShownRange(Calendar today) {
        Calendar calendar = (Calendar) GetNextWeekLeftRange(today).clone();
        calendar.add(Calendar.DATE, 6);
        return calendar;
    }

    public static Calendar GetThisWeekRightShownRange(Calendar today) {
        Calendar calendar = (Calendar) GetThisWeekLeftRange(today).clone();
        calendar.add(Calendar.DATE, 6);
        return calendar;
    }

    public static Calendar GetLastWeekRightShownRange(Calendar today) {
        Calendar calendar = (Calendar) GetLastWeekLeftRange(today).clone();
        calendar.add(Calendar.DATE, 6);
        return calendar;
    }

    public static Calendar GetThisMonthLeftRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static Calendar GetThisMonthRightRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static Calendar GetLastMonthLeftRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static Calendar GetLastMonthRightRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static Calendar GetThisYearLeftRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static Calendar GetThisYearRightRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static Calendar GetLastYearLeftRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.YEAR, -1);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static Calendar GetLastYearRightRange(Calendar today) {
        Calendar calendar = (Calendar)today.clone();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, 0);
        return calendar;
    }

    public static boolean ClickButtonDelete(int position) {
        return position == 9;
    }

    public static boolean ClickButtonCommit(int position) {
        return position == 11;
    }

    public static boolean ClickButtonIsZero(int position) {
        return position == 10;
    }

    public static double ToDollas(double money, String currency) {

        return 1.0 * money;

    }

    public static boolean IsStringRelation(String s1, String s2) {
        return true;
    }

    public static RelativeSizeSpan relativeSizeSpan;
    public static ForegroundColorSpan redForegroundSpan;
    public static ForegroundColorSpan greenForegroundSpan;
    public static ForegroundColorSpan whiteForegroundSpan;

    private static String lastColor0, lastColor1, lastColor2;

    private static Random random;

    private static String[] Colors = {"#F44336",
            "#E91E63",
            "#9C27B0",
            "#673AB7",
            "#3F51B5",
            "#2196F3",
            "#03A9F4",
            "#00BCD4",
            "#009688",
            "#4CAF50",
            "#8BC34A",
            "#CDDC39",
            "#FFEB3B",
            "#FFC107",
            "#FF9800",
            "#FF5722",
            "#795548",
            "#9E9E9E",
            "#607D8B"};

    public static int GetRandomColor() {
        int p = random.nextInt(Colors.length);
        while (Colors[p].equals(lastColor0)
                || Colors[p].equals(lastColor1)
                || Colors[p].equals(lastColor2)) {
            p = random.nextInt(Colors.length);
        }
        lastColor0 = lastColor1;
        lastColor1 = lastColor2;
        lastColor2 = Colors[p];
        return Color.parseColor(Colors[p]);
    }

    public static int GetTagColor(String tag) {
        switch (tag) {
            case "Sum Pie":
                return R.color.sum_header_pie;
            case "Sum Histogram":
                return R.color.sum_header_histogram;
            case "Book":
                return R.color.book_header;
            case "Clothing & Footwear":
                return R.color.closet_header;
            case "Donation":
                return R.color.donation_header;
            case "Education":
                return R.color.education_header;
            case "Entertainment":
                return R.color.entertainment_header;
            case "Friend":
                return R.color.friend_header;
            case "Hobby":
                return R.color.hobby_header;
            case "Home":
                return R.color.home_header;
            case "Insurance":
                return R.color.insurance_header;
            case "Internet":
                return R.color.internet_header;
            case "Meal":
                return R.color.meal_header;
            case "Medical":
                return R.color.medical_header;
            case "Snack":
                return R.color.snack_header;
            case "Sport":
                return R.color.sport_header;
            case "Traffic":
                return R.color.traffic_header;
            case "Vehicle Maintenance":
                return R.color.vehicle_maintenance_header;
            default:
                return R.color.green;
        }
    }

    public static int GetTagColor(int tag) {
        switch (tag) {
            case -2:
                return R.color.sum_header_pie;
            case -1:
                return R.color.sum_header_histogram;
            case 5:
                return R.color.book_header;
            case 1:
                return R.color.closet_header;
            case 13:
                return R.color.donation_header;
            case 9:
                return R.color.education_header;
            case 10:
                return R.color.entertainment_header;
            case 8:
                return R.color.friend_header;
            case 6:
                return R.color.hobby_header;
            case 2:
                return R.color.home_header;
            case 12:
                return R.color.insurance_header;
            case 7:
                return R.color.internet_header;
            case 0:
                return R.color.meal_header;
            case 11:
                return R.color.medical_header;
            case 15:
                return R.color.snack_header;
            case 14:
                return R.color.sport_header;
            case 3:
                return R.color.traffic_header;
            case 4:
                return R.color.vehicle_maintenance_header;
            default:
                return R.color.green;
        }
    }

    public static int GetTagDrawable(String tag) {
        switch (tag) {
            case "Sum Pie":
                return R.drawable.sum_header_pie;
            case "Sum Histogram":
                return R.drawable.sum_header_histogram;
            case "Book":
                return R.drawable.book_header;
            case "Clothing & Footwear":
                return R.drawable.closet_header;
            case "Donation":
                return R.drawable.donation_header;
            case "Education":
                return R.drawable.education_header;
            case "Entertainment":
                return R.drawable.entertainment_header;
            case "Friend":
                return R.drawable.friend_header;
            case "Hobby":
                return R.drawable.hobby_header;
            case "Home":
                return R.drawable.home_header;
            case "Insurance":
                return R.drawable.insurance_header;
            case "Internet":
                return R.drawable.internet_header;
            case "Meal":
                return R.drawable.meal_header;
            case "Medical":
                return R.drawable.medical_header;
            case "Snack":
                return R.drawable.snack_header;
            case "Sport":
                return R.drawable.sport_header;
            case "Traffic":
                return R.drawable.traffic_header;
            case "Vehicle Maintenance":
                return R.drawable.vehicle_maintenance_header;
            case "Transparent":
                return R.drawable.transparent;
            default:
                return R.drawable.sum_header_histogram;
        }
    }

    public static int GetSnackBarBackground(int tagId) {
        switch (tagId) {
            case -2:
                return R.drawable.snackbar_shape_sum_pie;
            case -1:
                return R.drawable.snackbar_shape_sum_histogram;
            case 5:
                return R.drawable.snackbar_shape_book;
            case 1:
                return R.drawable.snackbar_shape_closet;
            case 13:
                return R.drawable.snackbar_shape_donation;
            case 9:
                return R.drawable.snackbar_shape_education;
            case 10:
                return R.drawable.snackbar_shape_entertainment;
            case 8:
                return R.drawable.snackbar_shape_friend;
            case 6:
                return R.drawable.snackbar_shape_hobby;
            case 2:
                return R.drawable.snackbar_shape_home;
            case 12:
                return R.drawable.snackbar_shape_insurance;
            case 7:
                return R.drawable.snackbar_shape_internet;
            case 0:
                return R.drawable.snackbar_shape_meal;
            case 11:
                return R.drawable.snackbar_shape_medical;
            case 15:
                return R.drawable.snackbar_shape_snack;
            case 14:
                return R.drawable.snackbar_shape_sport;
            case 3:
                return R.drawable.snackbar_shape_traffic;
            case 4:
                return R.drawable.snackbar_shape_vehicle_maintenance;
            default:
                return R.drawable.snackbar_shape;
        }
    }

    public static int GetSnackBarBackground(String tag) {
        switch (tag) {
            case "Sum Pie":
                return R.drawable.snackbar_shape_sum_pie;
            case "Sum Histogram":
                return R.drawable.snackbar_shape_sum_histogram;
            case "Book":
                return R.drawable.snackbar_shape_book;
            case "Clothing & Footwear":
                return R.drawable.snackbar_shape_closet;
            case "Donation":
                return R.drawable.snackbar_shape_donation;
            case "Education":
                return R.drawable.snackbar_shape_education;
            case "Entertainment":
                return R.drawable.snackbar_shape_entertainment;
            case "Friend":
                return R.drawable.snackbar_shape_friend;
            case "Hobby":
                return R.drawable.snackbar_shape_hobby;
            case "Home":
                return R.drawable.snackbar_shape_home;
            case "Insurance":
                return R.drawable.snackbar_shape_insurance;
            case "Internet":
                return R.drawable.snackbar_shape_internet;
            case "Meal":
                return R.drawable.snackbar_shape_meal;
            case "Medical":
                return R.drawable.snackbar_shape_medical;
            case "Snack":
                return R.drawable.snackbar_shape_snack;
            case "Sport":
                return R.drawable.snackbar_shape_sport;
            case "Traffic":
                return R.drawable.snackbar_shape_traffic;
            case "Vehicle Maintenance":
                return R.drawable.snackbar_shape_vehicle_maintenance;
            default:
                return R.drawable.snackbar_shape;
        }
    }

    static int GetTagIcon(String tag) {
        switch (tag) {
            case "Sum Pie":
                return R.drawable.sum_pie_icon;
            case "Sum Histogram":
                return R.drawable.sum_histogram_icon;
            case "Book":
                return R.drawable.book_icon;
            case "Clothing & Footwear":
                return R.drawable.closet_icon;
            case "Donation":
                return R.drawable.donation_icon;
            case "Education":
                return R.drawable.education_icon;
            case "Entertainment":
                return R.drawable.entertainment_icon;
            case "Friend":
                return R.drawable.friend_icon;
            case "Hobby":
                return R.drawable.hobby_icon;
            case "Home":
                return R.drawable.home_icon;
            case "Insurance":
                return R.drawable.insurance_icon;
            case "Internet":
                return R.drawable.internet_icon;
            case "Meal":
                return R.drawable.meal_icon;
            case "Medical":
                return R.drawable.medical_icon;
            case "Snack":
                return R.drawable.snack_icon;
            case "Sport":
                return R.drawable.sport_icon;
            case "Traffic":
                return R.drawable.traffic_icon;
            case "Vehicle Maintenance":
                return R.drawable.vehicle_maintenance_icon;
            case "Music":
                return R.drawable.music_icon;
            case "Fund":
                return R.drawable.fund_icon;
            case "Drink":
                return R.drawable.drink_icon;
            case "Fruit":
                return R.drawable.fruit_icon;
            case "Film":
                return R.drawable.film_icon;
            case "Baby":
                return R.drawable.baby_icon;
            case "Partner":
                return R.drawable.partner_icon;
            case "Housing Loan":
                return R.drawable.housing_loan_icon;
            case "Pet":
                return R.drawable.pet_icon;
            case "Telephone Bill":
                return R.drawable.telephone_bill_icon;
            case "Travel":
                return R.drawable.travel_icon;
            default:
                return R.drawable.book_icon;
        }
    }

    static int GetTagIcon(int tagId) {
        return TAG_ICON[tagId + 2];
    }

    static String GetTagName(int tagId) {
        return myApplication.getAppContext().getResources().getString(TAG_NAME[tagId + 2]);
    }

    public static <K, V extends Comparable<V>> Map<K, V> SortTreeMapByValues(final Map<K, V> map) {
        Comparator<K> valueComparator =  new Comparator<K>() {
            public int compare(K k1, K k2) {
                int compare = map.get(k1).compareTo(map.get(k2));
                if (compare == 0) return 1;
                else return compare;
            }
        };
        TreeMap<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }

    private static final int[] EMPTY_STATE = new int[] {};

    public static void clearState(Drawable drawable) {
        if (drawable != null) {
            drawable.setState(EMPTY_STATE);
        }
    }
}
