package com.zeekands.ikasa.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val id: Int,
    val id_user: Int,
    val id_ikan: Int,
    val jumlah : Int,
    val total: Int,
) : Parcelable
