package com.example.prilogulka.data_base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data_base.interfaces.GiftCardsDataBase;

import java.util.ArrayList;
import java.util.List;

public class GiftCardsDataBaseImpl extends SQLiteOpenHelper implements GiftCardsDataBase {
    public GiftCardsDataBaseImpl(Context context) {
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

    @Override
    public int getGiftCardsCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DATA_BASE_NAME + ";";

        Log.i(CLASS_TAG, query);

        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    @Override
    public List<GiftCard> selectAll(String ownerEmail) {
        return findGiftCard(null, null, ownerEmail);
    }

    @Override
    public List<GiftCard> selectAll(String column, String value, String ownerEmail){
        return findGiftCard(column, value, ownerEmail);
    }

    @Override
    public List<GiftCard> selectAll(String column, int value, String ownerEmail) {
        return findGiftCard(column, value, ownerEmail);
    }

    private List<GiftCard> findGiftCard(String column, int value, @NonNull String ownerEmail){
        String query;
        if (column == null)
            query = SELECT_ALL_QUERY;
        else
            query = "SELECT * FROM " + DATA_BASE_NAME + " WHERE " + column + " = " + value + " AND "
                    + COLUMN_OWNER + " = " + "'" + ownerEmail + "';";

        Log.i(CLASS_TAG, query);

        return findInDataBase(query);
    }


    private List<GiftCard> findGiftCard(@Nullable String column, @Nullable String value, @NonNull String ownerEmail){
        String query;
        if (value == null || column == null)
            query = SELECT_ALL_QUERY;
        else
            query = "SELECT * FROM " + DATA_BASE_NAME + " WHERE " + column + " = " + "'" + value + "'" +
                    " AND " + COLUMN_OWNER + " = " + "'" + ownerEmail + "';";

        Log.i(CLASS_TAG, query);

        return findInDataBase(query);

    }

    private List<GiftCard> findInDataBase(String query){
        List<GiftCard> giftCardsList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            giftCardsList.add( new GiftCard(
                    cursor.getInt(0), // id
                    cursor.getString(1), // name
                    cursor.getInt(2), // destination
                    cursor.getInt(3), // coast_bronze
                    cursor.getInt(4), // coast_silver
                    cursor.getInt(5), // coast_golden
                    cursor.getString(6), // description
                    cursor.getString(7), // bronze_code
                    cursor.getString(8), // silver_code
                    cursor.getString(9), // golden_code
                    cursor.getString(10) // ownerEmail
            ));
        }

        return giftCardsList;
    }

    @Override
    public void insertGiftCard(GiftCard giftCard) {
        SQLiteDatabase database = this.getWritableDatabase();
        Log.i(CLASS_TAG, INSERT_QUERY);
        String query = String.format(INSERT_QUERY,
                giftCard.getName(),
                giftCard.getDestination(),
                giftCard.getCoastBronze(),
                giftCard.getCoastSilver(),
                giftCard.getCoastGolden(),
                giftCard.getDescription(),
                giftCard.getBronzeCode(),
                giftCard.getSilverCode(),
                giftCard.getGoldenCode(),
                giftCard.getOwnerEmail()
        );
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
    public void updateGiftCard(String name, String column, String newData, String ownerEmail) {
        String query = "UPDATE " + DATA_BASE_NAME + " SET " + column + " = '" + newData
                + "' WHERE name = '" + name + "'" + " AND "
                + COLUMN_OWNER + " = " + "'" + ownerEmail + "';";

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);
    }

    @Override
    public void updateGiftCard(String name, String column, int coast, String ownerEmail) {
        String query = "UPDATE " + DATA_BASE_NAME + " SET " + column + " = " + coast
                + " WHERE name = '" + name + "'"+ " AND "
                + COLUMN_OWNER + " = " + "'" + ownerEmail + "';";

        Log.i(CLASS_TAG, query);
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);
    }

    /**
     * TODO: доделать UPDATE
     */

    @Override
    public void updateGiftCard(GiftCard giftCard) {
        String query = "UPDATE " + DATA_BASE_NAME
                + " SET "
                + COLUMN_COAST_BRONZE + " = "  + giftCard.getCoastBronze() + ", "
                + COLUMN_COAST_SILVER + " = "  + giftCard.getCoastSilver() + ", "
                + COLUMN_COAST_GOLDEN + " = "  + giftCard.getCoastGolden() + ", "
                + COLUMN_DESCRIPTION  + " = '" + giftCard.getDescription() + "', "
                + COLUMN_BRONZE_CODE  + " = '" + giftCard.getBronzeCode()  + "',  "
                + COLUMN_SILVER_CODE  + " = '" + giftCard.getSilverCode()  + "',  "
                + COLUMN_GOLDEN_CODE  + " = '" + giftCard.getGoldenCode()  + "',  "
                + "WHERE "
                + COLUMN_NAME + " = '" + giftCard.getName() + "';";

        Log.i(CLASS_TAG, query);
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(query);
    }
}
