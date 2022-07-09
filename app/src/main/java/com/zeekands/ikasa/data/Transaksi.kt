package com.zeekands.ikasa.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaksi(
    var id: Int,
    var idUser: Int,
    var idIkan: Int,
    var berat: Int,
    var total: Int,
    var status: String,
): Parcelable

@Parcelize
data class TransaksiJoin(
    var id: Int,
    var idUser: Int,
    var idIkan: Int,
    var berat: Int,
    var total: Int,
    var status: String,
    var gambar: ByteArray
) : Parcelable