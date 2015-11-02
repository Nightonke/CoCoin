package com.nightonke.saver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * Created by 伟平 on 2015/10/20.
 */
public class DB {

    public static final String DB_NAME_STRING = "Record";

    public static final int VERSION = 1;

    private static DB db;
    private SQLiteDatabase sqliteDatabase;
    private DBHelper dbHelper;

    private DB(Context context) throws IOException {
        dbHelper = new DBHelper(
                context, DB_NAME_STRING, null, VERSION);
        sqliteDatabase = dbHelper.getWritableDatabase();
    }

    public synchronized static DB getInstance(Context context)
            throws IOException {
        if (db == null) {
            db = new DB(context);
        }
        return db;
    }

    public void getRecordList() {
        RecordManager.RECORDS = new LinkedList<>();
        RecordManager.TAGS = new LinkedList<>();
        Cursor cursor = sqliteDatabase
                .query(DB_NAME_STRING, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                record.setMoney(cursor.getFloat(cursor.getColumnIndex("MONEY")));
                record.setCurrency(cursor.getString(cursor.getColumnIndex("CURRENCY")));
                record.setTag(cursor.getString(cursor.getColumnIndex("TAG")));
                record.setCalendar(cursor.getString(cursor.getColumnIndex("TIME")));
                record.setRemark(cursor.getString(cursor.getColumnIndex("REMARK")));
                RecordManager.RECORDS.add(record);

                if (!RecordManager.TAGS.contains(record.getTag())) {
                    RecordManager.TAGS.add(record.getTag());
                }

            } while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public long saveRecord(Record record) {
        ContentValues values = new ContentValues();
        values.put("MONEY", record.getMoney());
        values.put("CURRENCY", record.getCurrency());
        values.put("TAG", record.getTag());
        values.put("TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(record.getCalendar().getTime()));
        values.put("REMARK", record.getRemark());
        long insertId = sqliteDatabase.insert("Record", null, values);
        record.setId(insertId);
        if (RecordManager.SHOW_LOG) {
            Log.d("Saver", "DB: Insert record: " + record.toString());
        }
        return insertId;
    }

    public long deleteRecord(long id) {
        long deletedId = sqliteDatabase.delete("Record",
                "ID = ?",
                new String[]{id + ""});
        Log.d("Saver",
                "DB: Delete record: " + "Record(id = " + id + ", deletedId = " + deletedId + ")");
        return deletedId;
    }

    public long updateRecord(Record record) {
        ContentValues values = new ContentValues();
        values.put("ID", record.getId());
        values.put("MONEY", record.getMoney());
        values.put("CURRENCY", record.getCurrency());
        values.put("TAG", record.getTag());
        values.put("TIME", record.getCalendar().toString());
        values.put("REMARK", record.getRemark());
        long updateId = sqliteDatabase.update("Record", values,
                "ID = ?",
                new String[]{record.getId() + ""});
        if (RecordManager.SHOW_LOG) {
            Log.d("Saver", "DB: Update record: " + record.toString());
        }
        return updateId;
    }


}
