package com.dicoding.djoalanapplication.network.response

import com.google.gson.annotations.SerializedName

data class GetStoreInfoResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: CompanyData? = null

)

data class CompanyData(

    @field:SerializedName("user")
    val user: AccountData
)
