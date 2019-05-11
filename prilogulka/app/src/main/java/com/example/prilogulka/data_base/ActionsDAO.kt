package com.example.prilogulka.data_base

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.prilogulka.data.Video


class ActionsDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    object ActionsEntery : BaseColumns {
        const val TABLE_NAME = "actions"
        const val _ID = "id"
        const val _EMAIL = "email"
        const val _VIDEO_ID = "watched_video_id"
        const val _POINTS = "watch_points"
        const val _WATCHED_DATE = "watch_date"
    }
    
    private val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${ActionsEntery.TABLE_NAME} (" +
                    "${ActionsEntery._ID} INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                    "${ActionsEntery._EMAIL} TEXT," +
                    "${ActionsEntery._VIDEO_ID} INTEGER NULL," +
                    "${ActionsEntery._POINTS} FLOAT," +
                    "${ActionsEntery._WATCHED_DATE} TEXT)"
    
    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ActionsEntery.TABLE_NAME}"
    
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "LocationDataSource.db"
    }
    
    fun insert(email: String, video: Video?, points: Float) {
        println(video.toString() + "" + points)
        val db = this.writableDatabase
        val values = setContentValues(email, video, points)
        
        db.insert(ActionsEntery.TABLE_NAME, null, values)
    }
    
    fun insertOutcome(email: String, outcome: Float) {
        insert(email, null, outcome)
    }
    
    fun getUserMoney(email: String): Float {
        val db = this.readableDatabase
    
        val cursor = db.query(
                ActionsEntery.TABLE_NAME,    // The table to query
                arrayOf(ActionsEntery._POINTS),//projection,    // The array of columns to return (pass null to get all)
                "${ActionsEntery._EMAIL} = ?",//selection,               // The columns for the WHERE clause
                arrayOf(email),                       // The values for the WHERE clause
                null,                          // don't group the rows
                null,                           // don't filter by row groups
                null                           // The sort order
        )
        var totalPoints: Float = 0f
        with(cursor) {
            while (moveToNext())
                totalPoints += getFloat(getColumnIndexOrThrow(ActionsEntery._POINTS))
        }
        
        return totalPoints
    }
/*
    fun select(id: Int): List<GiftCard> {
        val db = this.writableDatabase
        val cursor = db.query(
                GiftCardEntery.TABLE_NAME,    // The table to query
                null,//projection,    // The array of columns to return (pass null to get all)
                "${GiftCardEntery._ID} = ?",//selection,               // The columns for the WHERE clause
                arrayOf(id.toString()),                       // The values for the WHERE clause
                null,                          // don't group the rows
                null,                           // don't filter by row groups
                null                           // The sort order
        )
        val items = arrayListOf<GiftCard>()
        with(cursor) {
            while (moveToNext()) {
                val giftCard = GiftCard()
                giftCard.card.id = getInt(getColumnIndexOrThrow(GiftCardEntery._ID))
                giftCard.card.serialNumber = getString(getColumnIndexOrThrow(GiftCardEntery._SERIAL_NUMBER))
                giftCard.card.companyAdvertisementId = getInt(getColumnIndexOrThrow(GiftCardEntery._AD_COMPANY_ID))
                giftCard.card.dueDate = getString(getColumnIndexOrThrow(GiftCardEntery._DUE_DATE))
                giftCard.card.imageUrl = getString(getColumnIndexOrThrow(GiftCardEntery._IMAGE_URL))
                giftCard.card.brand = getString(getColumnIndexOrThrow(GiftCardEntery._BRAND))
                giftCard.card.vendor = getString(getColumnIndexOrThrow(GiftCardEntery._VENDOR))
                giftCard.card.description = getString(getColumnIndexOrThrow(GiftCardEntery._DESCRIPTION))
                val price = getString(getColumnIndexOrThrow(GiftCardEntery._PRICE))
                giftCard.card.priceArray = price.split(", ")
                items.add(giftCard)
            }
        }
        return items
    }
    private fun deleteAll() {
        val db = this.writableDatabase
        db?.delete(GiftCardEntery.TABLE_NAME, null, null)
    }
    
    fun update(giftCard: GiftCard) {
        val db = this.writableDatabase
        val values = setContentValues(giftCard)
        
        db?.update(GiftCardEntery.TABLE_NAME, values, "${GiftCardEntery._ID} = ?", arrayOf(giftCard.card.id.toString()))
    }
*/
    private fun setContentValues(email: String, video: Video?, points: Float) : ContentValues {
    return ContentValues().apply {
            put(ActionsEntery._EMAIL, email)
            put(ActionsEntery._POINTS, points)
            put(ActionsEntery._VIDEO_ID, video?.videoItem?.id)
            put(ActionsEntery._WATCHED_DATE, System.currentTimeMillis())
        }
    }

}
