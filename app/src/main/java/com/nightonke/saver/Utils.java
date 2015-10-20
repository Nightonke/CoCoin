package com.nightonke.saver;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

/**
 * Created by 伟平 on 2015/10/16.
 */

public class Utils {

    public static String PASSWORD = "1234";

    public static String[] FLOATINGLABELS = {"", "", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿"};

    public static String[] BUTTONS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "D", "0", "Y"};

    public static Typeface typefaceBernhardFashion;

    public static void init(Context context) {
        typefaceBernhardFashion = Typeface.createFromAsset(context.getAssets(), "fonts/BernhardFashion BT.ttf");
        relativeSizeSpan = new RelativeSizeSpan(2f);
        redForegroundSpan = new ForegroundColorSpan(Color.parseColor("#ff5252"));
        greenForegroundSpan = new ForegroundColorSpan(Color.parseColor("#4ca550"));
        whiteForegroundSpan = new ForegroundColorSpan(Color.parseColor("#ffffff"));
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

}
