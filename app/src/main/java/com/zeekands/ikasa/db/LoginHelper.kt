package com.zeekands.ikasa.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.zeekands.ikasa.db.DatabaseContract.LoginColumns.Companion.ID_USER

class LoginHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = DatabaseContract.LoginColumns.TABLE_NAME

        private var INSTANCE: LoginHelper? = null
        fun getInstance(context: Context): LoginHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: LoginHelper(context)
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
            null
        )

    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun delete(): Int {
        return database.delete(DATABASE_TABLE, null, null)
    }
}