package com.dicoding.djoalanapplication.network.response

import com.google.gson.annotations.SerializedName

data class UpdateAccResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("checkPayload")
	val checkPayload: AccountData
)
