package com.dicoding.djoalanapplication.network.response

import android.os.Parcelable
import com.dicoding.djoalanapplication.data.payment.PurchasedItems
import com.dicoding.djoalanapplication.data.product.Product
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class GetOrdersResponse(

	@field:SerializedName("data")
	val data: OrderData? = null,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class OrderData(

	@field:SerializedName("userTransaction")
	val userTransaction: List<TransactionResponse>
)
@Parcelize
data class SafeArgsData(
	val id : String,
	val date: String,
	val total : Int,
	val listProduct: @RawValue List<PurchasedItems>
):Parcelable

