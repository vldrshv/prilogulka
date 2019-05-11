package com.example.prilogulka.data_base

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.prilogulka.data.GiftCard


class GiftCardDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    object GiftCardEntery : BaseColumns {
        const val TABLE_NAME = "giftcards"
        const val _ID = "id"
        const val _CARD_ID = "card_id"
        const val _SERIAL_NUMBER = "serial_number"
        const val _AD_COMPANY_ID = "ad_company_id"
        const val _DUE_DATE = "due_date"
        const val _IMAGE_URL = "image_url"
        const val _BRAND = "brand"
        const val _VENDOR = "vendor"
        const val _DESCRIPTION = "description"
        const val _PRICE = "price"
    }
    
    private val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${GiftCardEntery.TABLE_NAME} (" +
                    "${GiftCardEntery._ID} INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                    "${GiftCardEntery._CARD_ID} INTEGER UNIQUE," +
                    "${GiftCardEntery._SERIAL_NUMBER} TEXT," +
                    "${GiftCardEntery._AD_COMPANY_ID} INTEGER," +
                    "${GiftCardEntery._DUE_DATE} TEXT," +
                    "${GiftCardEntery._IMAGE_URL} TEXT," +
                    "${GiftCardEntery._BRAND} TEXT," +
                    "${GiftCardEntery._VENDOR} TEXT," +
                    "${GiftCardEntery._DESCRIPTION} TEXT," +
                    "${GiftCardEntery._PRICE} INTEGER)"
    
    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${GiftCardEntery.TABLE_NAME}"
    
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
        const val DATABASE_NAME = "CardDataSource.db"
    }
    
    fun insert(giftCardList: List<GiftCard>) = giftCardList.forEach { insert(it) }
    
    fun insert(giftCard: GiftCard) {
        println(giftCard)
        val db = this.writableDatabase
        val values = setContentValues(giftCard)
        
        db?.insert(GiftCardEntery.TABLE_NAME, null, values)
    }
    
    fun selectAll(): List<GiftCard> {
        val db = this.readableDatabase
        
        val cursor = db.query(
                GiftCardEntery.TABLE_NAME,    // The table to query
                null,//projection,    // The array of columns to return (pass null to get all)
                null,//selection,               // The columns for the WHERE clause
                null,                       // The values for the WHERE clause
                null,                          // don't group the rows
                null,                           // don't filter by row groups
                null                           // The sort order
        )
        val items = arrayListOf<GiftCard>()
        with(cursor) {
            while (moveToNext()) {
                val giftCard = GiftCard()
                giftCard.card.cardId = getInt(getColumnIndexOrThrow(GiftCardEntery._CARD_ID))
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
                giftCard.card.cardId = getInt(getColumnIndexOrThrow(GiftCardEntery._CARD_ID))
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
        
        db?.update(GiftCardEntery.TABLE_NAME, values, "${GiftCardEntery._CARD_ID} = ?", arrayOf(giftCard.card.cardId.toString()))
    }
    
    private fun setContentValues(giftCard: GiftCard) : ContentValues {
        val values = ContentValues().apply {
            put(GiftCardEntery._CARD_ID, giftCard.card.cardId)
            put(GiftCardEntery._SERIAL_NUMBER, giftCard.card.serialNumber)
            put(GiftCardEntery._AD_COMPANY_ID, giftCard.card.companyAdvertisementId)
            put(GiftCardEntery._DUE_DATE, giftCard.card.dueDate)
            put(GiftCardEntery._IMAGE_URL, giftCard.card.imageUrl)
            put(GiftCardEntery._BRAND, giftCard.card.brand)
            put(GiftCardEntery._VENDOR, giftCard.card.vendor)
            put(GiftCardEntery._DESCRIPTION, giftCard.card.description)
            var price: String = giftCard.card.priceArray.toString()
            price = price.replace("[", "")
            price = price.replace("]", "")
            put(GiftCardEntery._PRICE, price)
        }
        return values
    }
}