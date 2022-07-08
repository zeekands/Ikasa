package com.zeekands.ikasa.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class IkanColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "ikan"
            const val _ID = "_id"
            const val NAMA = "nama"
            const val HARGA = "harga"
            const val STOCK = "stock"
            const val DESKRIPSI = "deskripsi"
        }
    }
    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val NAMA = "nama"
            const val EMAIL = "email"
            const val PASSWORD = "password"
            const val ROLE = "role"
        }
    }
    internal class TransaksiColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "transaksi"
            const val _ID = "_id"
            const val ID_USER = "idUser"
            const val ID_IKAN = "idIkan"
            const val BERAT = "berat"
            const val TOTAL = "total"
            const val STATUS = "status"
        }
    }
    internal class CartColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "cart"
            const val _ID = "_id"
            const val ID_USER = "idUser"
            const val ID_IKAN = "idIkan"
            const val BERAT = "berat"
            const val TOTAL = "total"
        }
    }
    internal class LoginColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "login"
            const val ID_USER = "idUser"
            const val NAMA_USER = "namaUser"
        }
    }
}