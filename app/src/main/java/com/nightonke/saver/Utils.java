package com.nightonke.saver;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by 伟平 on 2015/10/16.
 */

public class Utils {

    public static String PASSWORD = "1234";

    public static String[] MONTHS = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static String[] MONTHS_SHORT = {"", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public static String[] FLOATINGLABELS = {"", "", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿"};

    public static String[] BUTTONS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "D", "0", "Y"};

    public static Typeface typefaceLatoHairline;
    public static Typeface typefaceLatoLight;
    public static Typeface typefaceCodeLight;

    public static void init(Context context) {
        typefaceLatoHairline = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Hairline.ttf");
        typefaceLatoLight = Typeface.createFromAsset(context.getAssets(), "fonts/LatoLatin-Light.ttf");
        typefaceCodeLight = Typeface.createFromAsset(context.getAssets(), "fonts/CODE_Light.otf");
        relativeSizeSpan = new RelativeSizeSpan(2f);
        redForegroundSpan = new ForegroundColorSpan(Color.parseColor("#ff5252"));
        greenForegroundSpan = new ForegroundColorSpan(Color.parseColor("#4ca550"));
        whiteForegroundSpan = new ForegroundColorSpan(Color.parseColor("#ffffff"));

        lastColor0 = "";
        lastColor1 = "";
        lastColor2 = "";

        random = new Random();
    }

    public static boolean WEEK_START_WITH_SUNDAY = false;

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
            calendar.add(Calendar.DAY_OF_WEEK, diff[nowDayOfWeek]);
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

    public static String TODAY_VIEW_TITLE[] = {"Today",
                                               "Yesterday",
                                               "This Week",
                                               "Last Week",
                                               "This Month",
                                               "Last Month",
                                               "This Year",
                                               "Last Year",};

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
            default:
                return R.drawable.sum_header_histogram;
        }
    }

    public static int GetSnackBarBackground(int position) {
        switch (position) {
            case 0:
                return R.drawable.snackbar_shape_sum_pie;
            case 1:
                return R.drawable.snackbar_shape_sum_histogram;
            case 2:
                return R.drawable.snackbar_shape_book;
            case 3:
                return R.drawable.snackbar_shape_closet;
            case 4:
                return R.drawable.snackbar_shape_donation;
            case 5:
                return R.drawable.snackbar_shape_education;
            case 6:
                return R.drawable.snackbar_shape_entertainment;
            case 7:
                return R.drawable.snackbar_shape_friend;
            case 8:
                return R.drawable.snackbar_shape_hobby;
            case 9:
                return R.drawable.snackbar_shape_home;
            case 10:
                return R.drawable.snackbar_shape_insurance;
            case 11:
                return R.drawable.snackbar_shape_internet;
            case 12:
                return R.drawable.snackbar_shape_meal;
            case 13:
                return R.drawable.snackbar_shape_medical;
            case 14:
                return R.drawable.snackbar_shape_snack;
            case 15:
                return R.drawable.snackbar_shape_sport;
            case 16:
                return R.drawable.snackbar_shape_traffic;
            case 17:
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
            default:
                return R.drawable.book_icon;
        }
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
}
