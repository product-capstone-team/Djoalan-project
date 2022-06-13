package com.dicoding.djoalanapplication.network.response

import com.google.gson.annotations.SerializedName

data class NewOrderResponse(

	@field:SerializedName("newOrderData")
	val newOrderData: NewOrderData,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class NewOrderData(

	@field:SerializedName("newTransaction")
	val newTransaction: TransactionResponse
)

