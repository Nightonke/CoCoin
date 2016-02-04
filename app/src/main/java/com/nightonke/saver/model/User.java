package com.nightonke.saver.model;

import com.bmob.BmobPro;
import com.bmob.BmobProFile;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 伟平 on 2015/11/20.
 */

public class User extends BmobUser {

    // android id
    private String androidId;

    // the user's logo
    private String logoObjectId = "";

    // the user's setting for month limit
    private Boolean isMonthLimit;

    // the user's setting for month limit number
    private Integer monthLimit;

    // the user's setting for month warning
    private Integer monthWarning;

    // the user's setting for color remind or not
    private Boolean isColorRemind;

    // the user's setting for remind color
    private Integer remindColor;

    // the user's setting for whether forbidden record
    private Boolean isForbidden;

    // the user's setting for account book name
    private String accountBookName;

    // the user's setting for password
    private String accountBookPassword;

    // the user's setting for whether show picture in tag view
    private Boolean showPicture;

    // the user's setting for whether hollow
    private Boolean isHollow;

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getLogoObjectId() {
        return logoObjectId;
    }

    public void setLogoObjectId(String logoObjectId) {
        this.logoObjectId = logoObjectId;
    }

    public Boolean getIsMonthLimit() {
        return isMonthLimit;
    }

    public void setIsMonthLimit(Boolean isMonthLimit) {
        this.isMonthLimit = isMonthLimit;
    }

    public Integer getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(Integer monthLimit) {
        this.monthLimit = monthLimit;
    }

    public Integer getMonthWarning() {
        return monthWarning;
    }

    public void setMonthWarning(Integer monthWarning) {
        this.monthWarning = monthWarning;
    }

    public Boolean getIsColorRemind() {
        return isColorRemind;
    }

    public void setIsColorRemind(Boolean isColorRemind) {
        this.isColorRemind = isColorRemind;
    }

    public Integer getRemindColor() {
        return remindColor;
    }

    public void setRemindColor(Integer remindColor) {
        this.remindColor = remindColor;
    }

    public Boolean getIsForbidden() {
        return isForbidden;
    }

    public void setIsForbidden(Boolean isForbidden) {
        this.isForbidden = isForbidden;
    }

    public String getAccountBookName() {
        return accountBookName;
    }

    public void setAccountBookName(String accountBookName) {
        this.accountBookName = accountBookName;
    }

    public String getAccountBookPassword() {
        return accountBookPassword;
    }

    public void setAccountBookPassword(String accountBookPassword) {
        this.accountBookPassword = accountBookPassword;
    }

    public Boolean getShowPicture() {
        return showPicture;
    }

    public void setShowPicture(Boolean showPicture) {
        this.showPicture = showPicture;
    }

    public Boolean getIsHollow() {
        return isHollow;
    }

    public void setIsHollow(Boolean isHollow) {
        this.isHollow = isHollow;
    }
}
