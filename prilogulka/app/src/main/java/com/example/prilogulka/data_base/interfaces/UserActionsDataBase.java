package com.example.prilogulka.data_base.interfaces;

import com.example.prilogulka.data.Video;

import java.util.List;

public interface UserActionsDataBase {
    String CLASS_TAG = "DATA_BASE_USER_ACTIONS";
    String DATA_BASE_NAME = "user_actions";
    int DATA_BASE_VERSION = 10;

    String DATA_BASE_QUERY = "create table IF NOT EXISTS user_actions(\n" +
            "              id                integer primary key autoincrement,\n" +
            "              email             varchar(255) not null,\n" +
            "              watched_video_id  varchar(50)  not null,\n" +
            "              watch_points      double        not null,\n" +
            "              watch_date        varchar(15)  not null\n" +
            "            );";

    String SELECT_ALL_QUERY = "SELECT * FROM user_actions;";

    String INSERT_QUERY = "INSERT OR IGNORE INTO " + DATA_BASE_NAME + " VALUES \n" +
            "(null, '%s', '%s', %d, '%s');";

    String COLUMN_EMAIL = "email";
    String COLUMN_WATCHED_VIDEO_ID = "watched_video_id";
    String COLUMN_WATCH_POINTS = "watch_points";
    String COLUMN_WATCH_DATE = "watch_date";


    public int getUsersActionsCount();

    public List<Video> selectAll();
    public List<Video> findUserActions (String column, String filter);

    public void insertOutCome(String email, int watchpoints);
    public void insertUserActions (Video video);
    public void insertUserActions (Video video, boolean mine);

    public void dropTable();
    public void createTable();

    public void updateUserActions(String userEmail, String column, String newData);
    public void updateUserActions(String userEmail, String column, float newData);
    public void updateUserActions(Video video);

    public float getUserMoney(String email);
    public float getUserByDate(String email, String filter);


}
