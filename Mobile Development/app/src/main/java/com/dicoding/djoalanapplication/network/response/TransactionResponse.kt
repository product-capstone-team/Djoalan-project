package com.dicoding.djoalanapplication.network.response

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("total")
    val total: Int,

    @field:SerializedName("__v")
    val V: Int,

    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("listOfItems")
    val listOfItems: List<ProductResponse>,

    @field:SerializedName("userId")
    val userId: String
)

data class RequestBodyPayment(

    @field:SerializedName("amount")
    val amount: Int,

    @field:SerializedName("channelCode")
    val channelCode: String

)

data class AddTransactionResponse(

    @field:SerializedName("data")
    val data: AddTransactionResponseData? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null

)

data class AddTransactionResponseData(

    @field:SerializedName("newTransaction")
    val newTransaction: TransactionUnpopulateResponse

)

data class TransactionUnpopulateResponse(
    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("total")
    val total: Int,

    @field:SerializedName("__v")
    val V: Int,

    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("listOfItems")
    val listOfItems: List<String>,

    @field:SerializedName("userId")
    val userId: String
)
