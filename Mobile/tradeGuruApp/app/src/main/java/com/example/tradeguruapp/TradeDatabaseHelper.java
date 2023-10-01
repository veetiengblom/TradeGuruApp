package com.example.tradeguruapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TradeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TradeHistory.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "trade_history";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_COMPANY_NAME = "company_name";
    public static final String COLUMN_PRICE_DIFFERENCE = "price_difference";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_COMPANY_NAME + " TEXT, " +
                    COLUMN_PRICE_DIFFERENCE + " REAL, " +
                    COLUMN_TIMESTAMP + " TEXT);";

    public TradeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}