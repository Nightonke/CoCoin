package com.nightonke.saver;

import android.nfc.Tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class Record {

    private long id;
    private float money;
    private String currency;
    private int tag;
    private Calendar calendar;
    private String remark;

    public String toString() {
        return "Record(" +
                "id = " + id + ", " +
                "money = " + money + ", " +
                "currency = " + currency + ", " +
                "tag = " + tag + ", " +
                "calendar = " +
                new SimpleDateFormat("yyyy-MM-dd hh:mm").format(calendar.getTime()) + ", " +
                "remark = " + remark + ")";
    }

    public Record() {
        this.id = -1;
    }

    public Record(long id, float money, String currency, int tag, Calendar calendar) {
        this.id = id;
        this.money = money;
        this.currency = currency;
        this.tag = tag;
        this.calendar = calendar;
    }

    public Record(long id, float money, String currency, int tag, Calendar calendar, String remark) {
        this.id = id;
        this.money = money;
        this.currency = currency;
        this.tag = tag;
        this.calendar = calendar;
        this.remark = remark;
    }

    public void set(Record record) {
        this.id = record.id;
        this.money = record.money;
        this.currency = record.currency;
        this.tag = record.tag;
        this.calendar = record.calendar;
        this.remark = record.remark;
    }

    public boolean isInTime(Calendar c1, Calendar c2) {
        return (!this.calendar.before(c1)) && this.calendar.before(c2);
    }

    public boolean isInMoney(double money1, double money2, String currency) {
        return Utils.ToDollas(money1, currency) <= Utils.ToDollas(this.money, this.currency)
                && Utils.ToDollas(money2, currency) > Utils.ToDollas(this.money, this.currency);
    }

    public String getCalendarString() {
        return String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.format("%02d", calendar.get(Calendar.MINUTE)) + " "
                + Utils.GetMonthShort(calendar.get(Calendar.MONTH) + 1) + " "
                + calendar.get(Calendar.DAY_OF_MONTH) + " "
                + calendar.get(Calendar.YEAR);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendarString) {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date =sdf.parse(calendarString);
            this.calendar = Calendar.getInstance();
            this.calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
