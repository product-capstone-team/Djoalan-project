package com.dicoding.djoalanapplication.network.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: RegisterData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class RegisterData(

	@field:SerializedName("payload")
	val payload: AccountData? = null
)



