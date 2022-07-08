package com.zeekands.ikasa.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

class TransaksiHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = DatabaseContract.TransaksiColumns.TABLE_NAME

        private var INSTANCE: TransaksiHelper? = null
        fun getInstance(context: Context): TransaksiHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TransaksiHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${DatabaseContract.TransaksiColumns._ID} ASC")
    }
    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "${DatabaseContract.TransaksiColumns._ID} = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "${DatabaseContract.TransaksiColumns._ID} = ?", arrayOf(id))
    }
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "${DatabaseContract.TransaksiColumns._ID} = '$id'", null)
    }
}