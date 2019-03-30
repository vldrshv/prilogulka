package com.example.prilogulka.data_base.interfaces;

import com.example.prilogulka.data.GiftCard;

import java.util.List;

public interface ActivatedCardsDataBase {

    String CLASS_TAG = "ActivatedCardsDataBase";
    String DATA_BASE_NAME = "activated_cards";
    int DATA_BASE_VERSION = 10;

    String DATA_BASE_QUERY = "create table IF NOT EXISTS " + DATA_BASE_NAME + "(\n" +
            "              id                integer primary key autoincrement,\n" +
            "              name              varchar(255) not null,\n" +
            "              destination       int          not null,\n" +
            "              coast             int          not null,\n" +
            "              description       varchar(512),\n" +
            "              code              varchar(255) not null,\n" +
            "              owner             varchar(255) not null\n" +
            "            );";

    String SELECT_ALL_QUERY = "SELECT * FROM activated_cards WHERE owner = ";

    String INSERT_QUERY = "INSERT OR IGNORE INTO " + DATA_BASE_NAME + " VALUES \n" +
            "(null, '%s', %d, %d, '%s', '%s', '%s');";

    String COLUMN_NAME = "name";
    String COLUMN_DESTINATION = "destination";
    String COLUMN_COAST = "coast";
    String COLUMN_DESCRIPTION = "description";
    String COLUMN_CODE = "code";
    String COLUMN_OWNER = "owner";

    public int getActivatedCardsCount(String ownerEmail);
    public List<GiftCard> selectAll(String ownerEmail);
    public GiftCard findCard(String name, String ownerEmail);
    public void insertActivatedCard(GiftCard giftCard, int cost, String code);
    public void dropTable();
    public void createTable();
}
