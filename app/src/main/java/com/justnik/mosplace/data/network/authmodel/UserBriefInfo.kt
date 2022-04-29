package com.justnik.mosplace.data.network.authmodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

open class UserBriefInfo (
    val email: String,
    val password: String
)