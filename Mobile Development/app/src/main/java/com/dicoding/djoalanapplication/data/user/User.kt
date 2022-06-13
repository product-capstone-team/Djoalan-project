package com.dicoding.djoalanapplication.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,
    val name: String,
    val email: String,
    val password: String,

    @ColumnInfo(name = "business_account")
    val isBusinessAccount: Boolean,
    @ColumnInfo(name = "store_name")
    var storeName: String? = null,
    var company: String? = null,

    @ColumnInfo(name = "store_latitude")
    var storeLatitude: Double? = 0.0,
    @ColumnInfo(name = "store_longitude")
    var storeLongitude: Double? = 0.0

)
