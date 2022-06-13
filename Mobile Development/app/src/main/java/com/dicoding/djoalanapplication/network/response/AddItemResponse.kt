package com.dicoding.djoalanapplication.network.response

import com.google.gson.annotations.SerializedName

data class AddItemResponse(

	@field:SerializedName("newItem")
	val newItem: ProductResponse? = null,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)



