package com.example.prilogulka.data_base.interfaces;

import com.example.prilogulka.data.userData.User;

import java.util.List;

public interface UserInfoDataBase {
    String CLASS_TAG = "DATA_BASE_USER_INFO";
    String DATA_BASE_NAME = "user_info";
    int DATA_BASE_VERSION = 10;

    String DATA_BASE_QUERY = "create table IF NOT EXISTS user_info(\n" +
            "              id                integer primary key autoincrement,\n" +
            "              full_name         varchar(255) null,\n" +
            "              birthday          varchar(10)  null,\n" +
            "              sex               varchar(10)  null,\n" +
            "              city              varchar(45)  null,\n" +
            "              registration_date varchar(10)  not null,\n" +
            "              email             varchar(255) not null,\n" +
            "              password          varchar(50)  not null,\n" +
            "              email_check_code  varchar(10)  not null,\n" +
            "              is_email_checked  int          not null,\n" +
            "              last_date_online  varchar(15)  not null,\n" +

            "              constraint email_UNIQUE\n" +
            "              unique (email)\n" +
            "            );";

    String SELECT_ALL_QUERY = "SELECT * FROM user_info;";

    String INSERT_QUERY = "INSERT OR IGNORE INTO " + DATA_BASE_NAME + " VALUES \n" +
            "(null, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d,  '%s');";

    String COLUMN_FULL_NAME = "full_name";
    String COLUMN_BIRTHDAY = "birthday";
    String COLUMN_SEX = "sex";
    String COLUMN_CITY = "city";
    String COLUMN_EMAIL = "email";
    String COLUMN_PASSWORD = "password";
    String COLUMN_EMAIL_CHECK_CODE = "email_check_code";
    String COLUMN_IS_EMAIL_CHECKED = "is_email_checked";


    public int getUsersCount();
    public List<User> selectAll();
    public List<User> findUserInfo (String column, String filter);
    public void insertUserInfo (User user);
    public void dropTable();
    public void createTable();
    public void updateUserInfo(String userEmail, String column, String newData);
    public void updateUserInfo(String userEmail, String column, int newData);
    public void updateUserInfo(User user);
}
