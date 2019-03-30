package com.example.prilogulka.data_base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.prilogulka.data.GiftCard;
import com.example.prilogulka.data_base.interfaces.ActivatedCardsDataBase;

import java.util.ArrayList;
import java.util.List;

public class ActivatedCardsDataBaseImpl extends SQLiteOpenHelper implements ActivatedCardsDataBase {
    public ActivatedCardsDataBaseImpl(Context context) {
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
    public int getActivatedCardsCount(String ownerEmail) {
        return selectAll(ownerEmail).size();
    }

    @Override
    public List<GiftCard> selectAll(String ownerEmail) {
        List<GiftCard> giftCardsList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(SELECT_ALL_QUERY + "'" + ownerEmail+ "';", null);
        while (cursor.moveToNext()) {
            giftCardsList.add( new GiftCard(
                    cursor.getInt(0), // id
                    cursor.getString(1), // name
                    cursor.getInt(2), // destination
                    cursor.getInt(3), // coast_bronze
                    0, // coast_silver
                    0, // coast_golden
                    cursor.getString(4), // description
                    cursor.getString(5), // bronze_code
                    null, // silver_code
                    null, // golden_code
                    cursor.getString(6) // ownerEmail
            ));
        }
        Log.i(CLASS_TAG, SELECT_ALL_QUERY + "'" + ownerEmail+ "';");
        return giftCardsList;
    }

    @Override
    public GiftCard findCard(String code, String ownerEmail){
        SQLiteDatabase database = this.getReadableDatabase();
        String query = SELECT_ALL_QUERY + "'" + ownerEmail+ "' AND " + COLUMN_CODE + " = '" + code +"';";
        Cursor cursor = database.rawQuery(query, null);

        Log.i(CLASS_TAG, query);
        // 4678174783841904180
        cursor.moveToFirst();

        return new GiftCard(
                    cursor.getInt(0), // id
                    cursor.getString(1), // name
                    cursor.getInt(2), // destination
                    cursor.getInt(3), // coast_bronze
                    0, // coast_silver
                    0, // coast_golden
                    cursor.getString(4), // description
                    cursor.getString(5), // bronze_code
                    null, // silver_code
                    null, // golden_code
                    cursor.getString(6) // ownerEmail
            );
    }

    @Override
    public void insertActivatedCard(GiftCard giftCard, int cost, String code) {
        SQLiteDatabase database = this.getWritableDatabase();
        Log.i(CLASS_TAG, INSERT_QUERY);
        String query = String.format(INSERT_QUERY,
                giftCard.getName(),
                giftCard.getDestination(),
                cost,
                giftCard.getDescription(),
                code,
                giftCard.getOwnerEmail()
        );
        Log.i(CLASS_TAG, query);

        database.execSQL(query);
        database.close();
    }


}
