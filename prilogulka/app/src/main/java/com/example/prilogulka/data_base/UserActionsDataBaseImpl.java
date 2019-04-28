package com.example.prilogulka.data_base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.prilogulka.data.Time;
import com.example.prilogulka.data_base.interfaces.UserActionsDataBase;

public class UserActionsDataBaseImpl extends SQLiteOpenHelper implements UserActionsDataBase {
    public UserActionsDataBaseImpl(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(CLASS_TAG, DATA_BASE_QUERY);
        sqLiteDatabase.execSQL(DATA_BASE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            System.out.println("UPGRADE DB oldVersion=" + oldVersion + " - newVersion=" + newVersion);
            onCreate(sqLiteDatabase);
            if (oldVersion < 10) {
                sqLiteDatabase.execSQL(DATA_BASE_QUERY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        System.out.println("DOWNGRADE DB oldVersion=" + oldVersion + " - newVersion=" + newVersion);
    }

    public void insertOutCome(String email, int watchpoints) {
        SQLiteDatabase database = this.getWritableDatabase();
        Log.i(CLASS_TAG, INSERT_QUERY);
        String query = String.format(INSERT_QUERY, email, "outcome",
                watchpoints, Time.getTodayTime() + " " + Time.getToday());
        Log.i(CLASS_TAG, query);

        database.execSQL(query);
        database.close();
    }

    @Override
    public void dropTable() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + DATA_BASE_NAME);
    }

    @Override
    public void createTable() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(DATA_BASE_QUERY);
    }

    @Override
    public void updateUserActions(String userEmail, String column, String newData) {
        String query = "UPDATE " + DATA_BASE_NAME + " SET " + column + " = '" + newData
                + "' WHERE email = '" + userEmail + "';";

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);
    }

    @Override
    public void updateUserActions(String userEmail, String column, float newData) {
        String query = "UPDATE " + DATA_BASE_NAME + " SET " + column + " = " + newData
                + " WHERE email = '" + userEmail + "';";

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);
    }

    @Override
    public float getUserMoney(String email){
        String query = "SELECT SUM(watch_points) FROM user_actions WHERE email = '" + email + "';";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getFloat(0);
    }

    public float getUserByDate(String email, String filter){
        String query = "SELECT SUM(watch_points) FROM user_actions" +
                " WHERE (email = '" + email + "' " +
                "AND " + COLUMN_WATCH_DATE + " LIKE " + "'" + filter + "');";
        SQLiteDatabase database = this.getWritableDatabase();
        if (database.isDbLockedByCurrentThread())
            database.endTransaction();
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        Log.i(CLASS_TAG, query);
        float result = cursor.getFloat(0);
        database.close();
        return result;
    }

}
