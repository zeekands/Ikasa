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
    fun queryAllByUser(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "${DatabaseContract.TransaksiColumns.ID_USER} = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }
    fun queryByStatus(status: String, id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "${DatabaseContract.TransaksiColumns.STATUS} = ? AND ${DatabaseContract.TransaksiColumns.ID_USER} = ?",
            arrayOf(status, id),
            null,
            null,
            null,
            null)
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

    fun queryJoinwithIkan(idIkan : String, id: String, status: String) : Cursor{
        return database.rawQuery(
            "SELECT * FROM ${DatabaseContract.TransaksiColumns.TABLE_NAME} JOIN ${DatabaseContract.IkanColumns.TABLE_NAME} ON ${DatabaseContract.TransaksiColumns.ID_IKAN} = ${DatabaseContract.IkanColumns._ID} WHERE ${DatabaseContract.IkanColumns._ID} = $idIkan AND ${DatabaseContract.TransaksiColumns.ID_USER} = $id AND ${DatabaseContract.TransaksiColumns.STATUS} = $status",
            null
        )
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