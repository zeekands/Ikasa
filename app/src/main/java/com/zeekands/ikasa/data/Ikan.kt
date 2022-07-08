package com.zeekands.ikasa.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ikan(
    var id: Int,
    var nama: String,
    var harga: Int,
    var stock: Int,
    var deskripsi: String,
    var gambar: ByteArray
): Parcelable
