package com.example.prilogulka.data_base

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.prilogulka.data.Video

class VideoDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    object VideoEntery : BaseColumns {
        const val TABLE_NAME = "videos"
        const val _ID = "id"
        const val _NAME = "name"
        const val _URL = "url"
        const val _B2B_CLIENT_ID = "b2b_client_id"
        const val _CREATED_AT = "created_at"
        const val _WATCH_COUNTER = "watch_counter"
        const val _PRICE = "price"
    }
    
    private val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${VideoEntery.TABLE_NAME} (" +
                    "${VideoEntery._ID} INTEGER PRIMARY KEY UNIQUE," +
                    "${VideoEntery._NAME} TEXT," +
                    "${VideoEntery._URL} TEXT," +
                    "${VideoEntery._WATCH_COUNTER} INTEGER," +
                    "${VideoEntery._PRICE} INTEGER," +
                    "${VideoEntery._B2B_CLIENT_ID} TEXT," +
                    "${VideoEntery._CREATED_AT} TEXT)"
    
    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${VideoEntery.TABLE_NAME}"
    
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
        const val DATABASE_NAME = "VideoDataSource.db"
    }
    
    fun insert(videoList: List<Video>) = videoList.forEach { insert(it) }
    
    fun insert(video: Video) {
        val db = this.writableDatabase
        val values = setContentValues(video)
        
        db?.replace(VideoEntery.TABLE_NAME, null, values)
    }
    
    fun selectAll(): List<Video> {
        val db = this.readableDatabase
        
        val cursor = db.query(
                VideoEntery.TABLE_NAME,    // The table to query
                null,//projection,    // The array of columns to return (pass null to get all)
                null,//selection,               // The columns for the WHERE clause
                null,                       // The values for the WHERE clause
                null,                          // don't group the rows
                null,                           // don't filter by row groups
                null                           // The sort order
        )
        val items = arrayListOf<Video>()
        with(cursor) {
            while (moveToNext()) {
                val video = Video()
                video.videoItem.id = getInt(getColumnIndexOrThrow(VideoEntery._ID))
                video.videoItem.name = getString(getColumnIndexOrThrow(VideoEntery._NAME))
                video.videoItem.url = getString(getColumnIndexOrThrow(VideoEntery._URL))
                video.videoItem.price = getInt(getColumnIndexOrThrow(VideoEntery._PRICE))
                video.videoItem.createdAt = getString(getColumnIndexOrThrow(VideoEntery._CREATED_AT))
                video.videoItem.b2bClientId = getInt(getColumnIndexOrThrow(VideoEntery._B2B_CLIENT_ID))
                
                items.add(video)
            }
        }
        return items
    }
    
    fun deleteAll() {
        val db = this.writableDatabase
        db?.delete(VideoEntery.TABLE_NAME, null, null)
    }
    fun delete(video: Video){
        val db = this.writableDatabase
        db?.delete(VideoEntery.TABLE_NAME, "${VideoEntery._ID} = ?", arrayOf(video.videoItem.id.toString()))
    }
    
    fun update(video: Video) {
        val db = this.writableDatabase
        val values = setContentValues(video)
        
        db?.update(VideoEntery.TABLE_NAME, values, "${VideoEntery._ID} = ?", arrayOf(video.videoItem.id.toString()))
    }
    
    private fun setContentValues(video: Video) : ContentValues {
        val values = ContentValues().apply {
            put(VideoEntery._ID, video.videoItem.id)
            put(VideoEntery._NAME, video.videoItem.name)
            put(VideoEntery._URL, video.videoItem.url)
            put(VideoEntery._PRICE, video.videoItem.price)
            put(VideoEntery._B2B_CLIENT_ID, video.videoItem.b2bClientId)
            put(VideoEntery._CREATED_AT, video.videoItem.createdAt)
        }
        return values
    }
}


