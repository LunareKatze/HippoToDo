package com.lunarekatze.hippotodo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// КЛАСС БАЗЫ ДАННЫХ

public class TaskHelper extends SQLiteOpenHelper {      // Класс наследуется от класса SQLiteOpenHelper (ибо нужен SQLite)

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "todo.db";
    public static final String DATABASE_TABLE = "tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STATUS = "status";

    SQLiteDatabase database;    // Объявляем элемент database типа SQLiteDatabase


    public TaskHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE " + DATABASE_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT" + ")");
        db.execSQL("CREATE TABLE " + DATABASE_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT, " + COLUMN_STATUS + " TEXT" + ")");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }
}
