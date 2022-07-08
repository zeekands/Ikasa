package com.zeekands.ikasa

import android.database.Cursor
import com.zeekands.ikasa.data.*
import com.zeekands.ikasa.db.DatabaseContract

object MappingHelper {
    fun mapIkanCursorToArrayList(ikanCursor: Cursor?): ArrayList<Ikan> {
        val ikanList = ArrayList<Ikan>()
        ikanCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.IkanColumns._ID))
                val nama = getString(getColumnIndexOrThrow(DatabaseContract.IkanColumns.NAMA))
                val harga = getInt(getColumnIndexOrThrow(DatabaseContract.IkanColumns.HARGA))
                val stock = getInt(getColumnIndexOrThrow(DatabaseContract.IkanColumns.STOCK))
                val deskripsi = getString(getColumnIndexOrThrow(DatabaseContract.IkanColumns.DESKRIPSI))
                ikanList.add(Ikan(id, nama, harga, stock, deskripsi))
            }
        }
        return ikanList
    }

    fun mapIkanCursorToIkan(ikanCursor: Cursor?): Ikan {
        var ikan: Ikan? = null
        ikanCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.IkanColumns._ID))
                val nama = getString(getColumnIndexOrThrow(DatabaseContract.IkanColumns.NAMA))
                val harga = getInt(getColumnIndexOrThrow(DatabaseContract.IkanColumns.HARGA))
                val stock = getInt(getColumnIndexOrThrow(DatabaseContract.IkanColumns.STOCK))
                val deskripsi = getString(getColumnIndexOrThrow(DatabaseContract.IkanColumns.DESKRIPSI))
                ikan = Ikan(id, nama, harga, stock, deskripsi)
            }
        }
        return ikan as Ikan
    }

    fun mapUserCursorToUser(userCursor: Cursor?): User? {
        var user: User? = null
        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val nama = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAMA))
                val email = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.EMAIL))
                val password = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.PASSWORD))
                val role = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.ROLE))
                user = User(id, nama, email, password, role)
            }
        }
        return user
    }

    fun mapUserCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()
        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val nama = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAMA))
                val email = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.EMAIL))
                val password = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.PASSWORD))
                val role = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.ROLE))
                userList.add(User(id, nama, email, password, role))
            }
        }
        return userList
    }

    fun mapTransaksiCursorToArrayList(transaksiCursor: Cursor?): ArrayList<Transaksi> {
        val transaksiList = ArrayList<Transaksi>()
        transaksiCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.TransaksiColumns._ID))
                val idIkan = getInt(getColumnIndexOrThrow(DatabaseContract.TransaksiColumns.ID_IKAN))
                val idUser = getInt(getColumnIndexOrThrow(DatabaseContract.TransaksiColumns.ID_USER))
                val berat = getInt(getColumnIndexOrThrow(DatabaseContract.TransaksiColumns.BERAT))
                val status = getString(getColumnIndexOrThrow(DatabaseContract.TransaksiColumns.STATUS))
                val total = getInt(getColumnIndexOrThrow(DatabaseContract.TransaksiColumns.TOTAL))
                transaksiList.add(Transaksi(id, idUser, idIkan, berat, total, status))
            }
        }
        return transaksiList
    }

    fun mapCartCursorToArrayList(cartCursor: Cursor?): ArrayList<Cart> {
        val cartList = ArrayList<Cart>()
        cartCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.CartColumns._ID))
                val idIkan = getInt(getColumnIndexOrThrow(DatabaseContract.CartColumns.ID_IKAN))
                val idUser = getInt(getColumnIndexOrThrow(DatabaseContract.CartColumns.ID_USER))
                val berat = getInt(getColumnIndexOrThrow(DatabaseContract.CartColumns.BERAT))
                val total = getInt(getColumnIndexOrThrow(DatabaseContract.CartColumns.TOTAL))
                cartList.add(Cart(id, idUser, idIkan, berat, total))
            }
        }
        return cartList
    }

    fun mapLoginCursorToArrayList(cartCursor: Cursor?): ArrayList<Login> {
        val cartList = ArrayList<Login>()
        cartCursor?.apply {
            while (moveToNext()) {
                val idUser = getInt(getColumnIndexOrThrow(DatabaseContract.LoginColumns.ID_USER))
                val namaUser = getString(getColumnIndexOrThrow(DatabaseContract.LoginColumns.NAMA_USER))
                val role = getInt(getColumnIndexOrThrow(DatabaseContract.LoginColumns.ROLE))
                cartList.add(Login(idUser,namaUser,role))
            }
        }
        return cartList
    }
}