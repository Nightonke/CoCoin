package com.nightonke.saver.model;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.nightonke.saver.activity.CoCoinApplication;

/**
 * Created by 伟平 on 2015/11/17.
 */

public class SettingManager {

// store value//////////////////////////////////////////////////////////////////////////////////////

    // whether it is CoCoin's first time
    private Boolean FIRST_TIME;
    // whether is logged on
    private Boolean LOGGED_ON;
    // user name
    private String USER_NAME;
    // user email
    private String USER_EMAIL;
    // has profile logo
    private Boolean HAS_LOGO;
    // the logo's name in server
    private String LOGO_NAME_IN_SERVER;
    // the logo's object id
    private String LOGO_OBJECT_ID;
    // whether is month-limit
    private Boolean IS_MONTH_LIMIT;
    // month-limit
    private Integer MONTH_LIMIT;
    // month warning
    private Integer MONTH_WARNING;
    // color reminder
    private Boolean IS_COLOR_REMIND;
    // the color of the reminder
    private Integer REMIND_COLOR;
    // whether is able to be recorded when over the limit
    private Boolean IS_FORBIDDEN;
    // account bool name
    private String ACCOUNT_BOOK_NAME;
    // the password
    private String PASSWORD;
    // the user password
    private String USER_PASSWORD;
    // whether show picture in account book
    private Boolean SHOW_PICTURE;
    // whether draw a hollow pie chart
    private Boolean IS_HOLLOW;
    // whether remind update
    private Boolean REMIND_UPDATE;
    // whether this version can be updated
    private Boolean CAN_BE_UPDATED;
    // recently sync to mobile time
    private String RECENTLY_SYNC_TIME;

    // tell the mainActivity whether the tags' order should be changed
    private Boolean MAIN_ACTIVITY_TAG_SHOULD_CHANGE = false;

    // tell the today view to change the pie
    private Boolean TODAY_VIEW_PIE_SHOULD_CHANGE = false;

    // tell the today view to change the title
    private Boolean TODAY_VIEW_TITLE_SHOULD_CHANGE = false;

    // tell the main view to change the title
    private Boolean MAIN_VIEW_TITLE_SHOULD_CHANGE = false;

    // tell the today view to change the data
    private Boolean RECORD_IS_UPDATED = false;

    // tell the today view to update the month expense
    private Boolean TODAY_VIEW_MONTH_EXPENSE_SHOULD_CHANGE = false;

    // tell the main view to update the month expense
    private Boolean MAIN_VIEW_MONTH_EXPENSE_SHOULD_CHANGE = false;

    // tell the main view to update the remind color
    private Boolean MAIN_VIEW_REMIND_COLOR_SHOULD_CHANGE = false;

    // tell the today view to update the logo
    private Boolean TODAY_VIEW_LOGO_SHOULD_CHANGE = false;

    // tell the today view to update the infomation of user
    private Boolean TODAY_VIEW_INFO_SHOULD_CHANGE = false;

// default value////////////////////////////////////////////////////////////////////////////////////

    // default first time
    private final Boolean DEFAULT_FIRST_TIME = true;
    // whether is logged on by default
    private final Boolean DEFAULT_LOGGED_ON = false;
    // user name by default
    private final String DEFAULT_USER_NAME = null;
    // user email by default
    private final String DEFAULT_USER_EMAIL = null;
    // has profile logo by default
    private final Boolean DEFAULT_HAS_LOGO = false;
    // whether is month-limit by default
    private final Boolean DEFAULT_IS_MONTH_LIMIT = false;
    // month-limit by default
    private final Integer DEFAULT_MONTH_LIMIT = 1000;
    // month warning by default
    private final Integer DEFAULT_MONTH_WARNING = 800;
    // color reminder by default
    private final Boolean DEFAULT_IS_COLOR_REMIND = false;
    // the color of the reminder defaulty
    private final Integer DEFAULT_REMIND_COLOR = (int)Long.parseLong("FFE91E63", 16);
    // whether is able to be recorded when over the limit by default
    private final Boolean DEFAULT_IS_FORBIDDEN = false;
    // account bool name by default
    private final String DEFAULT_ACCOUNT_BOOK_NAME = "CoCoin";
    // the password
    private final String DEFAULT_PASSWORD = "1234";
    // whether show picture in account book
    private final Boolean DEFAULT_SHOW_PICTURE = false;
    // whether draw a hollow pie chart
    private final Boolean DEFAULT_IS_HOLLOW = true;
    // whether remind update
    private final Boolean DEFAULT_REMIND_UPDATE = true;
    // whether this version can be updated
    private final Boolean DEFAULT_CAN_BE_UPDATED = false;
    // the profile logo store place
    private final String DEFAULT_PROFILE_IMAGE_DIR = "imageDir";
    // the profile logo name
    private final String DEFAULT_PROFILE_IMAGE_NAME = "profile.jpg";
    // recently sync to mobile
    private final String DEFAULT_RECENTLY_SYNC_TIME = null;

    private boolean SHOW_MAIN_ACTIVITY_GUIDE = true;

    private boolean SHOW_LIST_VIEW_GUIDE = true;

    private static SettingManager ourInstance = new SettingManager();

    public static SettingManager getInstance() {
        return ourInstance;
    }

    private SettingManager() {
    }

    public Boolean getFirstTime() {
        FIRST_TIME = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("FIRST_TIME", DEFAULT_FIRST_TIME);
        return FIRST_TIME;
    }

    public void setFirstTime(Boolean FIRST_TIME) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("FIRST_TIME", FIRST_TIME);
        editor.commit();
        this.FIRST_TIME = FIRST_TIME;
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

    public String getLogoNameInServer() {
        LOGO_NAME_IN_SERVER = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getString("LOGO_NAME_IN_SERVER", null);
        return LOGO_NAME_IN_SERVER;
    }

    public void setLogoNameInServer(String LOGO_NAME_IN_SERVER) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putString("LOGO_NAME_IN_SERVER", LOGO_NAME_IN_SERVER);
        editor.commit();
        this.LOGO_NAME_IN_SERVER = LOGO_NAME_IN_SERVER;
    }

    public String getLogoObjectId() {
        LOGO_OBJECT_ID = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getString("LOGO_OBJECT_ID", null);
        return LOGO_OBJECT_ID;
    }

    public void setLogoObjectId(String LOGO_OBJECT_ID) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putString("LOGO_OBJECT_ID", LOGO_OBJECT_ID);
        editor.commit();
        this.LOGO_OBJECT_ID = LOGO_OBJECT_ID;
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

    public Integer getMonthWarning() {
        MONTH_WARNING = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getInt("MONTH_WARNING", DEFAULT_MONTH_WARNING);
        return MONTH_WARNING;
    }

    public void setMonthWarning(Integer MONTH_WARNING) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putInt("MONTH_WARNING", MONTH_WARNING);
        editor.commit();
        this.MONTH_WARNING = MONTH_WARNING;
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

    public int getRemindColor() {
        REMIND_COLOR = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getInt("REMIND_COLOR", DEFAULT_REMIND_COLOR);
        return REMIND_COLOR;
    }

    public void setRemindColor(int REMIND_COLOR) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putInt("REMIND_COLOR", REMIND_COLOR);
        editor.commit();
        this.REMIND_COLOR = REMIND_COLOR;
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

    public String getUserPassword() {
        USER_PASSWORD = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getString("USER_PASSWORD", "");
        return USER_PASSWORD;
    }

    public void setUserPassword(String USER_PASSWORD) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putString("USER_PASSWORD", USER_PASSWORD);
        editor.commit();
        this.USER_PASSWORD = USER_PASSWORD;
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

    public Boolean getRemindUpdate() {
        REMIND_UPDATE = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("REMIND_UPDATE", DEFAULT_REMIND_UPDATE);
        return REMIND_UPDATE;
    }

    public void setRemindUpdate(Boolean REMIND_UPDATE) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("REMIND_UPDATE", REMIND_UPDATE);
        editor.commit();
        this.REMIND_UPDATE = REMIND_UPDATE;
    }

    public Boolean getCanBeUpdated() {
        CAN_BE_UPDATED = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("CAN_BE_UPDATED", DEFAULT_CAN_BE_UPDATED);
        return CAN_BE_UPDATED;
    }

    public void setCanBeUpdated(Boolean CAN_BE_UPDATED) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("CAN_BE_UPDATED", CAN_BE_UPDATED);
        editor.commit();
        this.CAN_BE_UPDATED = CAN_BE_UPDATED;
    }

    public String getRecentlySyncTime() {
        RECENTLY_SYNC_TIME = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getString("RECENTLY_SYNC_TIME", DEFAULT_RECENTLY_SYNC_TIME);
        return RECENTLY_SYNC_TIME;
    }

    public void setRecentlySyncTime(String RECENTLY_SYNC_TIME) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putString("RECENTLY_SYNC_TIME", RECENTLY_SYNC_TIME);
        editor.commit();
        this.RECENTLY_SYNC_TIME = RECENTLY_SYNC_TIME;
    }

    public String getProfileImageName() {
        return DEFAULT_PROFILE_IMAGE_NAME;
    }

    public String getProfileImageDir() {
        return DEFAULT_PROFILE_IMAGE_DIR;
    }

    public Boolean getMainActivityTagShouldChange() {
        return MAIN_ACTIVITY_TAG_SHOULD_CHANGE;
    }

    public void setMainActivityTagShouldChange(Boolean MAIN_ACTIVITY_TAG_SHOULD_CHANGE) {
        this.MAIN_ACTIVITY_TAG_SHOULD_CHANGE = MAIN_ACTIVITY_TAG_SHOULD_CHANGE;
    }

    public Boolean getTodayViewPieShouldChange() {
        return TODAY_VIEW_PIE_SHOULD_CHANGE;
    }

    public void setTodayViewPieShouldChange(Boolean TODAY_VIEW_PIE_SHOULD_CHANGE) {
        this.TODAY_VIEW_PIE_SHOULD_CHANGE = TODAY_VIEW_PIE_SHOULD_CHANGE;
    }

    public Boolean getTodayViewTitleShouldChange() {
        return TODAY_VIEW_TITLE_SHOULD_CHANGE;
    }

    public void setTodayViewTitleShouldChange(Boolean TODAY_VIEW_TITLE_SHOULD_CHANGE) {
        this.TODAY_VIEW_TITLE_SHOULD_CHANGE = TODAY_VIEW_TITLE_SHOULD_CHANGE;
    }

    public Boolean getRecordIsUpdated() {
        return RECORD_IS_UPDATED;
    }

    public void setRecordIsUpdated(Boolean RECORD_IS_UPDATED) {
        this.RECORD_IS_UPDATED = RECORD_IS_UPDATED;
    }

    public Boolean getMainViewTitleShouldChange() {
        return MAIN_VIEW_TITLE_SHOULD_CHANGE;
    }

    public void setMainViewTitleShouldChange(Boolean MAIN_VIEW_TITLE_SHOULD_CHANGE) {
        this.MAIN_VIEW_TITLE_SHOULD_CHANGE = MAIN_VIEW_TITLE_SHOULD_CHANGE;
    }

    public Boolean getTodayViewMonthExpenseShouldChange() {
        return TODAY_VIEW_MONTH_EXPENSE_SHOULD_CHANGE;
    }

    public void setTodayViewMonthExpenseShouldChange(Boolean TODAY_VIEW_MONTH_EXPENSE_SHOULD_CHANGE) {
        this.TODAY_VIEW_MONTH_EXPENSE_SHOULD_CHANGE = TODAY_VIEW_MONTH_EXPENSE_SHOULD_CHANGE;
    }

    public Boolean getMainViewMonthExpenseShouldChange() {
        return MAIN_VIEW_MONTH_EXPENSE_SHOULD_CHANGE;
    }

    public void setMainViewMonthExpenseShouldChange(Boolean MAIN_VIEW_MONTH_EXPENSE_SHOULD_CHANGE) {
        this.MAIN_VIEW_MONTH_EXPENSE_SHOULD_CHANGE = MAIN_VIEW_MONTH_EXPENSE_SHOULD_CHANGE;
    }

    public Boolean getMainViewRemindColorShouldChange() {
        return MAIN_VIEW_REMIND_COLOR_SHOULD_CHANGE;
    }

    public void setMainViewRemindColorShouldChange(Boolean MAIN_VIEW_REMIND_COLOR_SHOULD_CHANGE) {
        this.MAIN_VIEW_REMIND_COLOR_SHOULD_CHANGE = MAIN_VIEW_REMIND_COLOR_SHOULD_CHANGE;
    }

    public Boolean getTodayViewLogoShouldChange() {
        return TODAY_VIEW_LOGO_SHOULD_CHANGE;
    }

    public void setTodayViewLogoShouldChange(Boolean TODAY_VIEW_LOGO_SHOULD_CHANGE) {
        this.TODAY_VIEW_LOGO_SHOULD_CHANGE = TODAY_VIEW_LOGO_SHOULD_CHANGE;
    }

    public Boolean getTodayViewInfoShouldChange() {
        return TODAY_VIEW_INFO_SHOULD_CHANGE;
    }

    public void setTodayViewInfoShouldChange(Boolean TODAY_VIEW_INFO_SHOULD_CHANGE) {
        this.TODAY_VIEW_INFO_SHOULD_CHANGE = TODAY_VIEW_INFO_SHOULD_CHANGE;
    }

    public boolean getShowMainActivityGuide() {
        SHOW_MAIN_ACTIVITY_GUIDE = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("SHOW_MAIN_ACTIVITY_GUIDE", false);
        return SHOW_MAIN_ACTIVITY_GUIDE;
    }

    public void setShowMainActivityGuide(boolean SHOW_MAIN_ACTIVITY_GUIDE) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("SHOW_MAIN_ACTIVITY_GUIDE", SHOW_MAIN_ACTIVITY_GUIDE);
        editor.commit();
        this.SHOW_MAIN_ACTIVITY_GUIDE = SHOW_MAIN_ACTIVITY_GUIDE;
    }

    public boolean getListViewGuide() {
        SHOW_LIST_VIEW_GUIDE = PreferenceManager.
                getDefaultSharedPreferences(CoCoinApplication.getAppContext())
                .getBoolean("SHOW_LIST_VIEW_GUIDE", false);
        return SHOW_LIST_VIEW_GUIDE;
    }

    public void setListViewGuide(boolean SHOW_LIST_VIEW_GUIDE) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(CoCoinApplication.getAppContext()).edit();
        editor.putBoolean("SHOW_LIST_VIEW_GUIDE", SHOW_LIST_VIEW_GUIDE);
        editor.commit();
        this.SHOW_LIST_VIEW_GUIDE = SHOW_LIST_VIEW_GUIDE;
    }
}
