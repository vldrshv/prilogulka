package com.example.prilogulka.data_base

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.prilogulka.data.GiftCard


class ActivatedCardsDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    object ActivatedCardEntery : BaseColumns {
        const val TABLE_NAME = "activated_cards"
        const val _ID = "_id"
        const val _CARD_ID = "card_id"
        const val _SERIAL_NUMBER = "serial_number"
        const val _DUE_DATE = "due_date"
        const val _DAY_BOUGHT = "day_bought"
        const val _IMAGE_URL = "image_url"
        const val _DESCRIPTION = "description"
        const val _PRICE = "price"
        const val _EMAIL = "email"
    }

    private val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${ActivatedCardEntery.TABLE_NAME} (" +
                    "${ActivatedCardEntery._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${ActivatedCardEntery._CARD_ID} INTEGER," +
                    "${ActivatedCardEntery._SERIAL_NUMBER} TEXT UNIQUE," +
                    "${ActivatedCardEntery._DUE_DATE} TEXT," +
                    "${ActivatedCardEntery._DAY_BOUGHT} TEXT," +
                    "${ActivatedCardEntery._IMAGE_URL} TEXT," +
                    "${ActivatedCardEntery._DESCRIPTION} TEXT," +
                    "${ActivatedCardEntery._EMAIL} TEXT," +
                    "${ActivatedCardEntery._PRICE} INTEGER)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ActivatedCardEntery.TABLE_NAME}"

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
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "CardsDataSource.db"
    }

    fun insert(giftCard: GiftCard, price: Int, email: String) {
        println(giftCard)
        val db = this.writableDatabase
        val values = setContentValues(giftCard, price, email)

        db?.insert(ActivatedCardEntery.TABLE_NAME, null, values)
    }

    fun selectAll(email: String): List<GiftCard> {
        val db = this.readableDatabase

        val cursor = db.query(
                ActivatedCardEntery.TABLE_NAME,    // The table to query
                null,//projection,    // The array of columns to return (pass null to get all)
                "${ActivatedCardEntery._EMAIL} = ?",//selection,               // The columns for the WHERE clause
                arrayOf(email),                       // The values for the WHERE clause
                null,                          // don't group the rows
                null,                           // don't filter by row groups
                null                           // The sort order
        )
        val items = arrayListOf<GiftCard>()
        with(cursor) {
            while (moveToNext()) {
                val giftCard = GiftCard()
                giftCard.card.cardId = getInt(getColumnIndexOrThrow(ActivatedCardEntery._CARD_ID))
                giftCard.card.serialNumber = getString(getColumnIndexOrThrow(ActivatedCardEntery._SERIAL_NUMBER))
                giftCard.card.dueDate = getString(getColumnIndexOrThrow(ActivatedCardEntery._DUE_DATE))
                giftCard.card.dayBought = getString(getColumnIndexOrThrow(ActivatedCardEntery._DAY_BOUGHT))
                giftCard.card.imageUrl = getString(getColumnIndexOrThrow(ActivatedCardEntery._IMAGE_URL))
                giftCard.card.description = getString(getColumnIndexOrThrow(ActivatedCardEntery._DESCRIPTION))
                val price = getString(getColumnIndexOrThrow(ActivatedCardEntery._PRICE))
                giftCard.card.priceArray = price.split(", ")
                items.add(giftCard)
            }
        }
        return items
    }
    fun select(id: Int, email: String): GiftCard {
        val db = this.readableDatabase
        val cursor = db.query(
                ActivatedCardEntery.TABLE_NAME,    // The table to query
                null,//projection,    // The array of columns to return (pass null to get all)
                "${ActivatedCardEntery._CARD_ID} = ? AND ${ActivatedCardEntery._EMAIL} = ?",//selection,               // The columns for the WHERE clause
                arrayOf(id.toString(), email),                       // The values for the WHERE clause
                null,                          // don't group the rows
                null,                           // don't filter by row groups
                null                           // The sort order
        )
        val items = arrayListOf<GiftCard>()
        with(cursor) {
            while (moveToNext()) {
                val giftCard = GiftCard()
                giftCard.card.cardId = getInt(getColumnIndexOrThrow(ActivatedCardEntery._CARD_ID))
                giftCard.card.serialNumber = getString(getColumnIndexOrThrow(ActivatedCardEntery._SERIAL_NUMBER))
                giftCard.card.dueDate = getString(getColumnIndexOrThrow(ActivatedCardEntery._DUE_DATE))
                giftCard.card.imageUrl = getString(getColumnIndexOrThrow(ActivatedCardEntery._IMAGE_URL))
                giftCard.card.description = getString(getColumnIndexOrThrow(ActivatedCardEntery._DESCRIPTION))
                val price = getString(getColumnIndexOrThrow(ActivatedCardEntery._PRICE))
                giftCard.card.priceArray = price.split(", ")
                items.add(giftCard)
            }
        }
        return items[0]
    }
    private fun deleteAll(email: String) {
        val db = this.writableDatabase
        db?.delete(ActivatedCardEntery.TABLE_NAME,
                "${ActionsDAO.ActionsEntery._EMAIL} = ?",
                arrayOf(email))
    }

    fun update(giftCard: GiftCard, price: Int = 0, email: String) {
        val db = this.writableDatabase
        val values = setContentValues(giftCard, price, email)

        db?.update(ActivatedCardEntery.TABLE_NAME, values,
                "${ActivatedCardEntery._ID} = ?",
                arrayOf(giftCard.card.cardId.toString()))
    }

    private fun setContentValues(giftCard: GiftCard, price: Int = 0, email: String) : ContentValues {
        val values = ContentValues().apply {
            put(ActivatedCardEntery._CARD_ID, giftCard.card.cardId)
            put(ActivatedCardEntery._SERIAL_NUMBER, giftCard.card.serialNumber)
            put(ActivatedCardEntery._DUE_DATE, giftCard.card.dueDate)
            put(ActivatedCardEntery._DAY_BOUGHT, giftCard.card.dayBought)
            put(ActivatedCardEntery._IMAGE_URL, giftCard.card.imageUrl)
            put(ActivatedCardEntery._DESCRIPTION, giftCard.card.description)
            put(ActivatedCardEntery._EMAIL, email)
            put(ActivatedCardEntery._PRICE, price.toString())
        }
        return values
    }
}
