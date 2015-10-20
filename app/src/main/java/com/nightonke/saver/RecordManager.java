package com.nightonke.saver;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class RecordManager {

    private static RecordManager recordManager;

    private static DB db;

    public static List<Record> RECORDS;
    public static List<String> TAGS;

    private boolean ISDEBUG = true;
    private int RANDOM_DATA_NUMBER = 100;

    private RecordManager(Context context) {
        try {
            db = db.getInstance(context);
            Log.d("Saver", "Loading database successfully.");
        } catch(IOException e) {
            e.printStackTrace();
        }
        if (ISDEBUG) {

            SharedPreferences preferences =
                    context.getSharedPreferences("Values", Context.MODE_PRIVATE);
            if (preferences.getBoolean("RANDOM", false)) {
                return;
            }

            randomDataCreater();

            SharedPreferences.Editor editor =
                    context.getSharedPreferences("Values", Context.MODE_PRIVATE).edit();
            editor.putBoolean("RANDOM", true);
            editor.commit();

        }
    }

    public synchronized static RecordManager getInstance(Context context)
            throws IOException {
        if (recordManager == null) {
            RECORDS = new LinkedList<>();
            TAGS = new LinkedList<>();
            recordManager = new RecordManager(context);

            db.getRecordList();

            Log.d("Saver", "Loading " +
                    RECORDS.size() +
                    " records successfully.");
            Log.d("Saver", "Loading " +
                    TAGS.size() +
                    " tags successfully.");

            Collections.sort(TAGS, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return lhs.compareTo(rhs);
                }
            });
        }
        return recordManager;
    }

    public static long saveRecord(Record record) {
        long insertId = -1;
        Log.d("Saver", "Manager: Save record: " + record.toString());
        insertId = db.saveRecord(record);
        if (insertId == -1) {
            Log.d("Saver", "Save the above record FAIL!");
        } else {
            Log.d("Saver", "Save the above record SUCCESSFULLY!");
            RECORDS.add(record);
        }
        if (!TAGS.contains(record.getTag())) {
            TAGS.add(record.getTag());
        }
        return insertId;
    }

    public static long deleteRecord(long id) {
        long deletedId = -1;
        Log.d("Saver",
                "Manager: Delete record: " + "Record(id = " + id + ", deletedId = " + deletedId + ")");
        deletedId = db.deleteRecord(id);
        if (deletedId == -1) {
            Log.d("Saver", "Delete the above record FAIL!");
        } else {
            Log.d("Saver", "Delete the above record SUCCESSFULLY!");
            for (Record record : RECORDS) {
                if (record.getId() == deletedId) {
                    RECORDS.remove(record);
                    break;
                }
            }
        }
        return deletedId;
    }

    public static long updateRecord(Record record) {
        long updateId = -1;
        Log.d("Saver",
                "Manager: Update record: " + record.toString());
        updateId = db.updateRecord(record);
        if (updateId == -1) {
            Log.d("Saver", "Update the above record FAIL!");
        } else {
            Log.d("Saver", "Update the above record SUCCESSFULLY!");
            for (Record r : RECORDS) {
                if (r.getId() == updateId) {
                    r.set(record);
                    break;
                }
            }
        }
        if (!TAGS.contains(record.getTag())) {
            TAGS.add(record.getTag());
        }
        return updateId;
    }

    public static List<Record> queryRecordByTime(Calendar c1, Calendar c2) {
        List<Record> list = new LinkedList<>();
        for (Record record : RECORDS) {
            if (record.isInTime(c1, c2)) {
                list.add(record);
            }
        }
        return list;
    }

    public static List<Record> queryRecordByCurrency(String currency) {
        List<Record> list = new LinkedList<>();
        for (Record record : RECORDS) {
            if (record.getCurrency().equals(currency)) {
                list.add(record);
            }
        }
        return list;
    }

    public static List<Record> queryRecordByTag(String tag) {
        List<Record> list = new LinkedList<>();
        for (Record record : RECORDS) {
            if (record.getTag().equals(tag)) {
                list.add(record);
            }
        }
        return list;
    }

    public static List<Record> queryRecordByMoney(double money1, double money2, String currency) {
        List<Record> list = new LinkedList<>();
        for (Record record : RECORDS) {
            if (record.isInMoney(money1, money2, currency)) {
                list.add(record);
            }
        }
        return list;
    }

    public static List<Record> queryRecordByRemark(String remark) {
        List<Record> list = new LinkedList<>();
        for (Record record : RECORDS) {
            if (Utils.IsStringRelation(record.getRemark(), remark)) {
                list.add(record);
            }
        }
        return list;
    }

    private void randomDataCreater() {

        Random random = new Random();

        String[] tagStrings = {"Food", "Party", "Traffic", "Snack", "Game", "Develop", "Book"};

        for (int i = 0; i < RANDOM_DATA_NUMBER; i++) {
            Record record = new Record();

            record.setMoney(random.nextFloat() * 200);
            record.setRemark("Remark " + i);
            record.setTag(tagStrings[random.nextInt(tagStrings.length)]);
            record.setCurrency("RMB");
            Calendar calendar = Calendar.getInstance();
            calendar.set(random.nextInt(2) + 2014,
                         random.nextInt(12),
                         random.nextInt(28) + 1,
                         random.nextInt(24),
                         random.nextInt(60));
            calendar.add(Calendar.MINUTE, 0);
//            new SimpleDateFormat("yyyy-MM-dd hh:mm").format(calendar.getTime());
            record.setCalendar(calendar);

            saveRecord(record);
        }
    }

}
