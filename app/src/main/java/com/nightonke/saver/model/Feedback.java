package com.nightonke.saver.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Weiping on 2016/2/3.
 */
public class Feedback extends BmobObject {

    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
