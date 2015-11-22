package com.nightonke.saver.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 伟平 on 2015/11/22.
 */

public class Logo extends BmobObject {

    private BmobFile file;

    public Logo(BmobFile file) {
        this.file = file;
    }

    public Logo(String tableName, BmobFile file) {
        super(tableName);
        this.file = file;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }
}
