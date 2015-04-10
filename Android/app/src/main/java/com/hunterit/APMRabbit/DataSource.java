package com.hunterit.APMRabbit;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;

public class DataSource {

    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;

    public DataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void create(String location, String name) {
        //Get the current time for a timestamp
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        //SQL Statement
        String insert = "INSERT INTO " + MySQLiteHelper.TABLE_NAME + " VALUES("
                + null + ","
                + "'" + location + "',"
                + "'" + today.format("%k:%M:%S") + "',"
                + "'" + name + "')";

        //Load into the Database
        db.execSQL(insert);
        db.close();
    }


    public void rename(String old_name, String new_name) {

        String update = "UPDATE " + MySQLiteHelper.COL_NAME + " SET " + MySQLiteHelper.COL_NAME
                + " = " + new_name + " WHERE " + MySQLiteHelper.COL_NAME + " = " + old_name;

        db.execSQL(update);
        db.close();
    }

    public List<WayPoints> getAllWaypoints() {

        List<WayPoints> waypoints = new ArrayList<WayPoints>();

        String select = "SELECT * FROM " + MySQLiteHelper.TABLE_NAME;

        Cursor c = db.rawQuery(select, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            WayPoints result = save(c);
            waypoints.add(result);
            c.moveToNext();
        }
        // Make sure to close the cursor
        c.close();
        return waypoints;
    }

    private WayPoints save(Cursor cursor) {
        WayPoints entry = new WayPoints();
        entry.setID(cursor.getLong(0));
        entry.setTimestamp(cursor.getString(1));
        entry.setLocation(cursor.getString(2));
        entry.setName(cursor.getString(3));
        return entry;
    }

}