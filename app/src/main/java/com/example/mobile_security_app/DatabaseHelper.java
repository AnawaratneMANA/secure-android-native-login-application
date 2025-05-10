package com.example.mobile_security_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "secure_database.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Implement the database tables
        String sqlCreateQuery = "CREATE TABLE IF NOT EXISTS " +
                "user (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, salt TEXT, password TEXT)";
        sqLiteDatabase.execSQL(sqlCreateQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Upgrade the table when there's a change
        String upgradeTableQuery = "DROP TABLE IF EXISTS user";
        sqLiteDatabase.execSQL(upgradeTableQuery);
    }
}
