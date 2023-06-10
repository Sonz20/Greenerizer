package com.dicoding.greenerizer.data.local

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var name: String? = null,
    var email: String? = null,
    var totalPoint: Long? = null,
)
