package com.dicoding.djoalanapplication.network.response

import android.os.Parcelable
import com.dicoding.djoalanapplication.data.product.Product
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class ProductResponse(

    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("productId")
    val productId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("brand")
    val brand: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("expiredDate")
    val expiredDate: String,

    @field:SerializedName("quantity")
    val quantity: Int,

    @field:SerializedName("imageUrl")
    val imageUrl: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("companyId")
    val companyId: String

)

data class GetProductResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("getItems")
    val getItems: List<ProductResponse>? = null

)

data class GetStockResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<ProductResponse>? = null

)

@Parcelize
data class SafeArgsItems(
    val listProduct: @RawValue Product
):Parcelable
