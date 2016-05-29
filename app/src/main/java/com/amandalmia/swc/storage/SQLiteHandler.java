/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.amandalmia.swc.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SWC";

    //User Tables
    private static final String TABLE_SCHEDULE= "schedule";
    private static final String TABLE_ANNOUNCEMENT= "announcement";


    //Table Columns names
    private static final String KEY_DAY = "day";
    private static final String KEY_TIME = "time";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_VENUE = "venue";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";



    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + TABLE_SCHEDULE + "("
                + KEY_DAY + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_SUBJECT + " TEXT,"
                + KEY_VENUE + " TEXT"
                + ")";

        String CREATE_ANNOUNCEMENT_TABLE = "CREATE TABLE " + TABLE_ANNOUNCEMENT + "("
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION+ " TEXT"
                + ")";


        db.execSQL(CREATE_SCHEDULE_TABLE);
        db.execSQL(CREATE_ANNOUNCEMENT_TABLE);
        Log.d(TAG, "Database tables created");
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        // Create tables again
        onCreate(db);
    }

    public void addSchedule(String day, String time, String venue, String subject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIME, time);
        values.put(KEY_VENUE, venue);
        values.put(KEY_SUBJECT, subject);
        values.put(KEY_DAY, day);

        // Inserting Row
        long id = db.insert(TABLE_SCHEDULE, null, values);
        ; // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addAnnouncement(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_DESCRIPTION, description);

        // Inserting Row
        long id = db.insert(TABLE_ANNOUNCEMENT, null, values);
        ; // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }



    public ArrayList<HashMap<String, String>> getSchedule(String day) {
        ArrayList<HashMap<String, String>> personalNotes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SCHEDULE + " WHERE " + KEY_DAY + "='" + day + "'";
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            HashMap<String, String> dayDetails=  new HashMap<>();
            dayDetails.put(KEY_DAY,cursor.getString(0));
            dayDetails.put(KEY_TIME,cursor.getString(1));
            dayDetails.put(KEY_SUBJECT,cursor.getString(2));
            dayDetails.put(KEY_VENUE, cursor.getString(3));
            personalNotes.add(i, dayDetails);
            cursor.moveToNext();
            i++;

        }
        cursor.close();
        ;
        // return user
        Log.d(TAG, "Fetching teacher Personal notes from Sqlite: " + personalNotes.toString());

        return personalNotes;
    }

    public ArrayList<HashMap<String, String>> getAnnouncements() {
        ArrayList<HashMap<String, String>> announcements = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ANNOUNCEMENT;
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            HashMap<String, String> announcement=  new HashMap<>();
            announcement.put(KEY_TITLE,cursor.getString(0));
            announcement.put(KEY_DESCRIPTION,cursor.getString(1));
            announcements.add(i, announcement);
            cursor.moveToNext();
            i++;

        }
        cursor.close();
        ;
        // return user
        Log.d(TAG, "Fetching teacher Personal notes from Sqlite: " + announcements.toString());

        return announcements;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANNOUNCEMENT);
        onCreate(db);
    }

}
