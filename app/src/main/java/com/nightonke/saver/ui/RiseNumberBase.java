package com.nightonke.saver.ui;

/**
 * Created by 伟平 on 2015/11/16.
 */

public interface RiseNumberBase {
    public void start();
    public RiseNumberTextView withNumber(float number);
    public RiseNumberTextView withNumber(int number);
    public RiseNumberTextView setDuration(long duration);
    public void setOnEnd(RiseNumberTextView.EndListener callback);
}
