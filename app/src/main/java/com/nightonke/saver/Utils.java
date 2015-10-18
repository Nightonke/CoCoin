package com.nightonke.saver;

import android.content.Context;
import android.graphics.Typeface;

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

}
