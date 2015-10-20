package com.nightonke.saver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String CREATE_RECORD_STRING =
            "create table Record (" +
            "ID integer primary key autoincrement," +
            "MONEY float," +
            "CURRENCY text," +
            "TAG text," +
            "TIME text," +
            "REMARK text)";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORD_STRING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
