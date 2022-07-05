package com.zeekands.ikasa.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zeekands.ikasa.db.DatabaseContract.IkanColumns.Companion.TABLE_NAME
import com.zeekands.ikasa.db.DatabaseContract.TransaksiColumns.Companion.TABLE_NAME as TABLE_TRANSAKSI
import com.zeekands.ikasa.db.DatabaseContract.UserColumns.Companion.TABLE_NAME as TABLE_USER
import com.zeekands.ikasa.db.DatabaseContract.CartColumns.Companion.TABLE_NAME as TABLE_CART

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ikasa"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_IKAN = "CREATE TABLE IF NOT EXISTS $TABLE_NAME" +
                " (${DatabaseContract.IkanColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.IkanColumns.NAMA} TEXT NOT NULL," +
                " ${DatabaseContract.IkanColumns.HARGA} INTEGER NOT NULL," +
                " ${DatabaseContract.IkanColumns.DESKRIPSI} TEXT NOT NULL," +
                " ${DatabaseContract.IkanColumns.STOCK} INTEGER NOT NULL);"
        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS $TABLE_USER" +
                " (${DatabaseContract.UserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.UserColumns.NAMA} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.EMAIL} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.ROLE} INTEGER NOT NULL," +
                " ${DatabaseContract.UserColumns.PASSWORD} TEXT NOT NULL);"
        private const val SQL_CREATE_TABLE_TRANSAKSI = "CREATE TABLE IF NOT EXISTS $TABLE_TRANSAKSI" +
                " (${DatabaseContract.TransaksiColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.TransaksiColumns.ID_USER} INTEGER NOT NULL," +
                " ${DatabaseContract.TransaksiColumns.ID_IKAN} INTEGER NOT NULL," +
                " ${DatabaseContract.TransaksiColumns.BERAT} INTEGER NOT NULL," +
                " ${DatabaseContract.TransaksiColumns.TOTAL} INTEGER NOT NULL," +
                " ${DatabaseContract.TransaksiColumns.STATUS} TEXT NOT NULL);"
        private const val SQL_CREATE_TABLE_CART = "CREATE TABLE IF NOT EXISTS $TABLE_CART" +
                " (${DatabaseContract.CartColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.CartColumns.ID_USER} INTEGER NOT NULL," +
                " ${DatabaseContract.CartColumns.ID_IKAN} INTEGER NOT NULL," +
                " ${DatabaseContract.CartColumns.BERAT} INTEGER NOT NULL," +
                " ${DatabaseContract.CartColumns.TOTAL} INTEGER NOT NULL);"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_IKAN)
        db.execSQL(SQL_CREATE_TABLE_USER)
        db.execSQL(SQL_CREATE_TABLE_TRANSAKSI)
        db.execSQL(SQL_CREATE_TABLE_CART)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSAKSI")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }
}