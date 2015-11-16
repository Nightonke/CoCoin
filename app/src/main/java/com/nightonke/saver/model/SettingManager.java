package com.nightonke.saver.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nightonke.saver.activity.CoCoinApplication;

/**
 * Created by 伟平 on 2015/11/17.
 */

public class SettingManager {

// store value//////////////////////////////////////////////////////////////////////////////////////

    // whether is logged on
    private Boolean LOGGED_ON;
    // user name
    private String USER_NAME;
    // user email
    private String USER_EMAIL;
    // has profile logo
    private Boolean HAS_LOGO;
    // whether is month-limit
    private Boolean IS_MONTH_LIMIT;
    // month-limit
    private Integer MONTH_LIMIT;
    // color reminder
    private Boolean IS_COLOR_REMIND;
    // whether is able to be recorded when over the limit
    private Boolean IS_FORBIDDEN;
    // account bool name
    private String ACCOUNT_BOOK_NAME;
    // the password
    private String PASSWORD;
    // whether show picture in account book
    private Boolean SHOW_PICTURE;
    // whether draw a hollow pie chart
    private Boolean IS_HOLLOW;

// default value////////////////////////////////////////////////////////////////////////////////////

    // whether is logged on defaultly
    private final Boolean DEFAULT_LOGGED_ON = false;
    // user name defaultly
    private final String DEFAULT_USER_NAME = null;
    // user email defaultly
    private final String DEFAULT_USER_EMAIL = null;
    // has profile logo defaultly
    private final Boolean DEFAULT_HAS_LOGO = false;
    // whether is month-limit defaultly
    private final Boolean DEFAULT_IS_MONTH_LIMIT = false;
    // month-limit defaultly
    private final Integer DEFAULT_MONTH_LIMIT = 1000;
    // color reminder defaultly
    private final Boolean DEFAULT_IS_COLOR_REMIND = false;
    // whether is able to be recorded when over the limit defaultly
    private final Boolean DEFAULT_IS_FORBIDDEN = false;
    // account bool name defaultly
    private final String DEFAULT_ACCOUNT_BOOK_NAME = "CoCoin Account Book";
    // the password
    private final String DEFAULT_PASSWORD = null;
    // whether show picture in account book
    private final Boolean DEFAULT_SHOW_PICTURE = false;
    // whether draw a hollow pie chart
    private final Boolean DEFAULT_IS_HOLLOW = false;
    // the profile logo store place
    private final String DEFAULT_PROFILE_IMAGE_DIR = "imageDir";
    // the profile logo name
    private final String DEFAULT_PROFILE_IMAGE_NAME = "profile.jpg";

    private static SettingManager ourInstance = new SettingManager();

    public static SettingManager getInstance() {
        return ourInstance;
    }

    private SettingManager() {
    }

    public Boolean getLoggenOn() {
        LOGGED_ON = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("LOGGED_ON", DEFAULT_LOGGED_ON);
        return LOGGED_ON;
    }

    public void setLoggenOn(Boolean LOGGED_ON) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("LOGGED_ON", LOGGED_ON);
        editor.commit();
        this.LOGGED_ON = LOGGED_ON;
    }

    public String getUserName() {
        USER_NAME = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getString("USER_NAME", DEFAULT_USER_NAME);
        return USER_NAME;
    }

    public void setUserName(String USER_NAME) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putString("USER_NAME", USER_NAME);
        editor.commit();
        this.USER_NAME = USER_NAME;
    }

    public String getUserEmail() {
        USER_EMAIL = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getString("USER_EMAIL", DEFAULT_USER_EMAIL);
        return USER_EMAIL;
    }

    public void setUserEmail(String USER_EMAIL) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putString("USER_EMAIL", USER_EMAIL);
        editor.commit();
        this.USER_EMAIL = USER_EMAIL;
    }

    public Boolean getHasLogo() {
        HAS_LOGO = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("HAS_LOGO", DEFAULT_HAS_LOGO);
        return HAS_LOGO;
    }

    public void setHasLogo(Boolean HAS_LOGO) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("HAS_LOGO", HAS_LOGO);
        editor.commit();
        this.HAS_LOGO = HAS_LOGO;
    }

    public Boolean getIsMonthLimit() {
        IS_MONTH_LIMIT = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("IS_MONTH_LIMIT", DEFAULT_IS_MONTH_LIMIT);
        return IS_MONTH_LIMIT;
    }

    public void setIsMonthLimit(Boolean IS_MONTH_LIMIT) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("IS_MONTH_LIMIT", IS_MONTH_LIMIT);
        editor.commit();
        this.IS_MONTH_LIMIT = IS_MONTH_LIMIT;
    }

    public Integer getMonthLimit() {
        MONTH_LIMIT = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getInt("MONTH_LIMIT", DEFAULT_MONTH_LIMIT);
        return MONTH_LIMIT;
    }

    public void setMonthLimit(Integer MONTH_LIMIT) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putInt("MONTH_LIMIT", MONTH_LIMIT);
        editor.commit();
        this.MONTH_LIMIT = MONTH_LIMIT;
    }

    public Boolean getIsColorRemind() {
        IS_COLOR_REMIND = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("IS_COLOR_REMIND", DEFAULT_IS_COLOR_REMIND);
        return IS_COLOR_REMIND;
    }

    public void setIsColorRemind(Boolean IS_COLOR_REMIND) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("IS_COLOR_REMIND", IS_COLOR_REMIND);
        editor.commit();
        this.IS_COLOR_REMIND = IS_COLOR_REMIND;
    }

    public Boolean getIsForbidden() {
        IS_FORBIDDEN = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("IS_FORBIDDEN", DEFAULT_IS_FORBIDDEN);
        return IS_FORBIDDEN;
    }

    public void setIsForbidden(Boolean IS_FORBIDDEN) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("IS_FORBIDDEN", IS_FORBIDDEN);
        editor.commit();
        this.IS_FORBIDDEN = IS_FORBIDDEN;
    }

    public String getAccountBookName() {
        ACCOUNT_BOOK_NAME = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getString("ACCOUNT_BOOK_NAME", DEFAULT_ACCOUNT_BOOK_NAME);
        return ACCOUNT_BOOK_NAME;
    }

    public void setAccountBookName(String ACCOUNT_BOOK_NAME) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putString("ACCOUNT_BOOK_NAME", ACCOUNT_BOOK_NAME);
        editor.commit();
        this.ACCOUNT_BOOK_NAME = ACCOUNT_BOOK_NAME;
    }

    public String getPassword() {
        PASSWORD = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getString("PASSWORD", DEFAULT_PASSWORD);
        return PASSWORD;
    }

    public void setPassword(String PASSWORD) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putString("PASSWORD", PASSWORD);
        editor.commit();
        this.PASSWORD = PASSWORD;
    }

    public Boolean getShowPicture() {
        SHOW_PICTURE = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("SHOW_PICTURE", DEFAULT_SHOW_PICTURE);
        return SHOW_PICTURE;
    }

    public void setShowPicture(Boolean SHOW_PICTURE) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("SHOW_PICTURE", SHOW_PICTURE);
        editor.commit();
        this.SHOW_PICTURE = SHOW_PICTURE;
    }

    public Boolean getIsHollow() {
        IS_HOLLOW = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("IS_HOLLOW", DEFAULT_IS_HOLLOW);
        return IS_HOLLOW;
    }

    public void setIsHollow(Boolean IS_HOLLOW) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("IS_HOLLOW", IS_HOLLOW);
        editor.commit();
        this.IS_HOLLOW = IS_HOLLOW;
    }

    public String getProfileImageName() {
        return DEFAULT_PROFILE_IMAGE_NAME;
    }

    public String getProfileImageDir() {
        return DEFAULT_PROFILE_IMAGE_DIR;
    }

}
