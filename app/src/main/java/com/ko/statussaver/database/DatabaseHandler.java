package com.ko.statussaver.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ko.statussaver.model.FileDetail;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "filesManager";
    private static final String TABLE_FILES = "files";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_FILE_TYPE = "file_type";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FILES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_FILE_TYPE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
        onCreate(db);
    }

    public void addFile(FileDetail fileDetail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String spiltFileName = fileDetail.file.getName();
        String[] fileName = spiltFileName.split("\\.");
        values.put(KEY_NAME, fileName[0]);
        values.put(KEY_FILE_TYPE, fileDetail.fileType);
        db.insertWithOnConflict(TABLE_FILES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public int getFileCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_FILES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean checkIsInDBorNot(String fileName) {
        String[] fileNameSpilt = fileName.split("\\.");
        String selectQuery = "SELECT  * FROM " + TABLE_FILES + " WHERE " + KEY_NAME + " = " + "'" + fileNameSpilt[0] + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}