package com.zeekands.ikasa.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.zeekands.ikasa.data.User

class CartHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = DatabaseContract.CartColumns.TABLE_NAME

        private var INSTANCE: CartHelper? = null
        fun getInstance(context: Context): CartHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CartHelper(context)
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
            "${DatabaseContract.CartColumns._ID} ASC")
    }
    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "${DatabaseContract.CartColumns.ID_USER} = ?",
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
        return database.update(DATABASE_TABLE, values, "${DatabaseContract.CartColumns._ID} = ?", arrayOf(id))
    }
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "${DatabaseContract.CartColumns._ID} = '$id'", null)
    }
    fun delete(idUser: String): Int {
        return database.delete(DATABASE_TABLE, "${DatabaseContract.CartColumns.ID_USER} = '$idUser'", null)
    }
}