package com.nightonke.saver.model;

import java.io.File;

import cn.bmob.v3.BmobObject;

/**
 * Created by Weiping on 2016/1/27.
 */
public class APK extends BmobObject {

    private String name;
    private File file;
    private Integer version;
    private Boolean tooOld;
    private String info;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getTooOld() {
        return tooOld;
    }

    public void setTooOld(Boolean tooOld) {
        this.tooOld = tooOld;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
