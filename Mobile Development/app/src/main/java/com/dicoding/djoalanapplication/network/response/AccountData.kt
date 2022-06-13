package com.dicoding.djoalanapplication.network.response

import com.google.gson.annotations.SerializedName

data class AccountData(

    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("isBusinessAcc")
    val isBusinessAcc: Boolean,

    @field:SerializedName("storeName")
    val storeName: String? = null,

    @field:SerializedName("company")
    val company: String? = null,

    @field:SerializedName("storeLocation")
    val storeLocation: StoreLocation? = null,

)

data class StoreLocation(

    @field:SerializedName("lat")
    val lat: Double,

    @field:SerializedName("lon")
    val lon: Double

)

data class UpdateAccRequest(

    @field:SerializedName("storeName")
    val storeName: String,

    @field:SerializedName("company")
    val company: String,

    @field:SerializedName("storeLocation")
    val storeLocation: StoreLocation
)

