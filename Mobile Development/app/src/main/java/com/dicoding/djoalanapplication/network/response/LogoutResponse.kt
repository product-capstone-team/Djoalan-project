package com.dicoding.djoalanapplication.network.response

import com.google.gson.annotations.SerializedName

data class LogoutResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
