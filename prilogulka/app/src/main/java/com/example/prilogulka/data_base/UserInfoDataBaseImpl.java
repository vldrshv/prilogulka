package com.example.prilogulka.data_base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.prilogulka.data.Time;
import com.example.prilogulka.data.userData.User;
import com.example.prilogulka.data_base.interfaces.UserInfoDataBase;

import java.util.ArrayList;
import java.util.List;

public class UserInfoDataBaseImpl extends SQLiteOpenHelper implements UserInfoDataBase {
    public UserInfoDataBaseImpl(Context context) {
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

    public void insertUserInfo(User user) {
        SQLiteDatabase database = this.getWritableDatabase();
        Log.i(CLASS_TAG, INSERT_QUERY);
        String query = String.format(INSERT_QUERY,
                user.getName() + " " + user.getLastname(), user.getBirthday(),
                user.getSex(), user.getLocation(), "reg_date", user.getEmail(), "password",
        "code", 0, Time.getTodayTime() + " " + Time.getToday());
        Log.i(CLASS_TAG, query);

        database.execSQL(query);
        database.close();
    }

    @Override
    public int getUsersCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DATA_BASE_NAME + ";";

        Log.i(CLASS_TAG, query);

        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    @Override
    public List<User> selectAll() {
        return findUserInfo(null, null);
    }

    @Override
    public List<User> findUserInfo(String column, String filter) {
        String query;
        if (column == null || filter == null)
            query = SELECT_ALL_QUERY;
        else
            query = "SELECT * FROM " + DATA_BASE_NAME + " WHERE " + column + " LIKE " + "'" + filter + "';";

        Log.i(CLASS_TAG, query);

        List<User> userList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        while(cursor.moveToNext()) {
//            userList.add( new User(
//                    cursor.getInt(0), // id
//                    cursor.getString(1), // full_name
//                    cursor.getString(2), // birthday
//                    cursor.getString(3), // sex
//                    cursor.getString(4), // city
//                    cursor.getString(5), // registration_date
//                    cursor.getString(6), // email
//                    cursor.getString(7), // password
//                    cursor.getString(8), // email_check_code
//                    cursor.getInt(9) == 1, // is_email_checked
//                    cursor.getString(10) // last_date_online
//            ));
        }
        return userList;
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
    public void updateUserInfo(String userEmail, String column, String newData) {
        String query = "UPDATE " + DATA_BASE_NAME + " SET " + column + " = '" + newData
                + "' WHERE email = '" + userEmail + "';";

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);
    }

    @Override
    public void updateUserInfo(String userEmail, String column, int newData) {
        String query = "UPDATE " + DATA_BASE_NAME + " SET " + column + " = " + newData
                + " WHERE email = '" + userEmail + "';";

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);
    }

    @Override
    public void updateUserInfo(User user) {
//        String query = "UPDATE " + DATA_BASE_NAME + " SET "
//                + COLUMN_FULL_NAME + " = '" + (user.getSurname() + " " + user.getName() + " " + user.getLastName()) + "', "
//                + COLUMN_CITY + " = '" + user.getCity() + "', "
//                + COLUMN_BIRTHDAY + " = '" + user.getBirthday() + "', "
//                + COLUMN_SEX + " = '" + (user.getSex().equals("1") ? "муж" : "жен") + "'"
//                + " WHERE email = '" + user.getEmail() + "';";
//
//        Log.i(CLASS_TAG, query);
//        SQLiteDatabase database = this.getWritableDatabase();
//        database.execSQL(query);
    }
}
