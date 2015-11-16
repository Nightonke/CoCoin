package com.nightonke.saver.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.Record;
import com.nightonke.saver.model.Tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * Created by 伟平 on 2015/10/20.
 */
public class DB {

    public static final String DB_NAME_STRING = "CoCoin Database";
    public static final String RECORD_DB_NAME_STRING = "Record";
    public static final String TAG_DB_NAME_STRING = "Tag";

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

    public void getData() {
        RecordManager.RECORDS = new LinkedList<>();
        RecordManager.TAGS = new LinkedList<>();

        Cursor cursor = sqliteDatabase
                .query(TAG_DB_NAME_STRING, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Tag tag = new Tag();
                tag.setId(cursor.getInt(cursor.getColumnIndex("ID")) - 1);
                tag.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                tag.setWeight(cursor.getInt(cursor.getColumnIndex("WEIGHT")));
                RecordManager.TAGS.add(tag);
            } while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
            }
        }

        cursor = sqliteDatabase
                .query(RECORD_DB_NAME_STRING, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getLong(cursor.getColumnIndex("ID")));
                record.setMoney(cursor.getFloat(cursor.getColumnIndex("MONEY")));
                record.setCurrency(cursor.getString(cursor.getColumnIndex("CURRENCY")));
                record.setTag(cursor.getInt(cursor.getColumnIndex("TAG")));
                record.setCalendar(cursor.getString(cursor.getColumnIndex("TIME")));
                record.setRemark(cursor.getString(cursor.getColumnIndex("REMARK")));
                RecordManager.RECORDS.add(record);
                RecordManager.SUM += (int)record.getMoney();
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
        long insertId = sqliteDatabase.insert(RECORD_DB_NAME_STRING, null, values);
        record.setId(insertId);
        if (RecordManager.SHOW_LOG) {
            Log.d("Saver", "DB: Insert record: " + record.toString());
        }
        return insertId;
    }

    public int saveTag(Tag tag) {
        ContentValues values = new ContentValues();
        values.put("NAME", tag.getName());
        values.put("WEIGHT", tag.getWeight());
        int insertId = (int)sqliteDatabase.insert(TAG_DB_NAME_STRING, null, values);
        tag.setId(insertId);
        if (RecordManager.SHOW_LOG) {
            Log.d("Saver", "DB: Insert tag: " + tag.toString());
        }
        return insertId - 1;
    }

    public long deleteRecord(long id) {
        long deletedNumber = sqliteDatabase.delete(RECORD_DB_NAME_STRING,
                "ID = ?",
                new String[]{id + ""});
        if (RecordManager.SHOW_LOG) {
            Log.d("Saver",
                    "DB: Delete record: " + "Record(id = " + id + ", deletedId = " + deletedNumber + ")");
        }
        return id;
    }

    public int deleteTag(int id) {
        int deletedNumber = sqliteDatabase.delete(TAG_DB_NAME_STRING,
                "ID = ?",
                new String[]{(id + 1) + ""});
        if (RecordManager.SHOW_LOG) {
            Log.d("Saver",
                    "DB: Delete tag: " + "tag(id = " + id + ", deletedId = " + deletedNumber + ")");
        }
        return id;
    }

    public long updateRecord(Record record) {
        ContentValues values = new ContentValues();
        values.put("ID", record.getId());
        values.put("MONEY", record.getMoney());
        values.put("CURRENCY", record.getCurrency());
        values.put("TAG", record.getTag());
        values.put("TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(record.getCalendar().getTime()));
        values.put("REMARK", record.getRemark());
        sqliteDatabase.update(RECORD_DB_NAME_STRING, values,
                "ID = ?",
                new String[]{record.getId() + ""});
        if (RecordManager.SHOW_LOG) {
            Log.d("Saver", "DB: Update record: " + record.toString());
        }
        return record.getId();
    }

    public int updateTag(Tag tag) {
        ContentValues values = new ContentValues();
        values.put("NAME", tag.getName());
        values.put("WEIGHT", tag.getWeight());
        sqliteDatabase.update(TAG_DB_NAME_STRING, values,
                "ID = ?",
                new String[]{(tag.getId() + 1) + ""});
        if (RecordManager.SHOW_LOG) {
            Log.d("Saver", "DB: Update tag: " + tag.toString());
        }
        return tag.getId();
    }
}
