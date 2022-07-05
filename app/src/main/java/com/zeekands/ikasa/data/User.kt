package com.zeekands.ikasa.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    var nama: String,
    var email: String,
    var password: String,
    var role: Int,
): Parcelable
