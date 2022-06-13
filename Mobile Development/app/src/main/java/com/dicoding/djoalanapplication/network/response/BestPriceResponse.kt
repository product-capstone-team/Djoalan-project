package com.dicoding.djoalanapplication.network.response

import com.google.gson.annotations.SerializedName

data class BestPriceResponse(

	@field:SerializedName("sortedPrice")
	val sortedPrice: List<SortedPriceItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class SortedPriceItem(

	@field:SerializedName("companyId")
	val companyId: CompanyId,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("_id")
	val id: String
)

data class CompanyId(

	@field:SerializedName("company")
	val company: String,

	@field:SerializedName("_id")
	val id: String
)


