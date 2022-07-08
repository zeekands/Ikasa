package com.zeekands.ikasa.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Login(
    var id_user: Int,
    var nama: String,
    var role: Int,
): Parcelable
