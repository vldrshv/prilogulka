package com.example.prilogulka.data_base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.prilogulka.data.Time;
import com.example.prilogulka.data.Video;
import com.example.prilogulka.data_base.interfaces.UserActionsDataBase;

import java.util.ArrayList;
import java.util.List;

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

    public void insertUserActions(Video video) {
        SQLiteDatabase database = this.getWritableDatabase();
        Log.i(CLASS_TAG, INSERT_QUERY);
        String query = String.format(INSERT_QUERY, video.getUserWatched(), video.getVideoId(),
                video.getWatchPoints(), Time.getTodayTime() + " " + Time.getToday());
        Log.i(CLASS_TAG, query);

        database.execSQL(query);
        database.close();
    }

    public void insertUserActions(Video video, boolean mine) {
        SQLiteDatabase database = this.getWritableDatabase();
        Log.i(CLASS_TAG, INSERT_QUERY);
        String query = String.format(INSERT_QUERY, video.getUserWatched(), video.getVideoId(),
                video.getWatchPoints(), video.getWatchDate());
        Log.i(CLASS_TAG, query);

        database.execSQL(query);
        database.close();
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

    /**
     * TODO: WHERE user = '-------'
     */
    @Override
    public int getUsersActionsCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DATA_BASE_NAME + ";";

        Log.i(CLASS_TAG, query);

        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    /**
     * TODO: WHERE user = '-------'
     */
    @Override
    public List<Video> selectAll() {
        return findUserActions(null, null);
    }

    /**
     * TODO: WHERE user = '-------'
     */
    @Override
    public List<Video> findUserActions(String column, String filter) {
        String query;
        if (column == null || filter == null)
            query = SELECT_ALL_QUERY;
        else
            query = "SELECT * FROM " + DATA_BASE_NAME + " WHERE " + column + " LIKE " + "'" + filter + "';";

        Log.i(CLASS_TAG, query);

        List<Video> videoList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            videoList.add( new Video(
               cursor.getInt(0), // id
               cursor.getString(1), // email
               cursor.getString(2), // watched_video_id
               cursor.getInt(3), // watch_points
               cursor.getString(4) // watch_date
            ));
        }
        return videoList;
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

    /**
     * TODO: WHERE user = '-------'
     */
    @Override
    public void updateUserActions(Video video) {
        String query = "UPDATE " + DATA_BASE_NAME + " SET "
                + COLUMN_WATCHED_VIDEO_ID + " = '" + video.getVideoId() + "', "
                + COLUMN_WATCH_POINTS + " = '" + video.getWatchPoints() + "', "
                + COLUMN_WATCH_DATE + " = '" + video.getWatchDate() + "';";

        Log.i(CLASS_TAG, query);
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
