package com.example.prilogulka.data_base.interfaces;

import com.example.prilogulka.data.GiftCard;

import java.util.List;

public interface GiftCardsDataBase {

    String CLASS_TAG = "GiftCardsDataBase";
    String DATA_BASE_NAME = "gift_cards";
    int DATA_BASE_VERSION = 10;

    String DATA_BASE_QUERY = "create table IF NOT EXISTS gift_cards(\n" +
            "              id                integer primary key autoincrement,\n" +
            "              name              varchar(255) not null,\n" +
            "              destination       int          not null,\n" +
            "              coast_bronze      int          not null,\n" +
            "              coast_silver      int          not null,\n" +
            "              coast_golden      int          not null,\n" +
            "              description       varchar(512),\n" +
            "              bronze_code       varchar(255) not null,\n" +
            "              silver_code       varchar(255) not null,\n" +
            "              golden_code       varchar(255) not null,\n" +
            "              owner             varchar(255) not null,\n" +

            "              constraint name_UNIQUE\n" +
            "              unique (name, owner)\n" +
            "            );";

    String SELECT_ALL_QUERY = "SELECT * FROM gift_cards;";

    String INSERT_QUERY = "INSERT OR IGNORE INTO " + DATA_BASE_NAME + " VALUES \n" +
"(null, '%s', %d, %d, %d, %d, '%s', '%s', '%s','%s', '%s');";

    String COLUMN_NAME = "name";
    String COLUMN_DESTINATION = "destination";
    String COLUMN_COAST_BRONZE = "coast_bronze";
    String COLUMN_COAST_SILVER = "coast_silver";
    String COLUMN_COAST_GOLDEN = "coast_golden";
    String COLUMN_DESCRIPTION = "description";
    String COLUMN_BRONZE_CODE = "bronze_code";
    String COLUMN_SILVER_CODE = "silver_code";
    String COLUMN_GOLDEN_CODE = "golden_code";
    String COLUMN_OWNER = "owner";

    public int getGiftCardsCount();
    public List<GiftCard> selectAll(String ownerEmail);
    public void insertGiftCard (GiftCard giftCard);
    public void dropTable();
    public void createTable();
    public void updateGiftCard(String name, String column, String newData, String ownerEmail);
    public void updateGiftCard(String name, String column, int coast, String ownerEmail);
    public void updateGiftCard(GiftCard giftCard);
    public List<GiftCard> selectAll(String column, String value, String ownerEmail);
    public List<GiftCard> selectAll(String column, int value, String ownerEmail);

}
