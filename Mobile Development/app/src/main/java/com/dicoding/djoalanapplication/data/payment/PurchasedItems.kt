package com.dicoding.djoalanapplication.data.payment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PurchasedItems(
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val productId: String = "barcode_id",

    @ColumnInfo(name = "product_name")
    val productName: String,
    val brand: String,
    val expireDate: String,
    val price: Int,
    val category: String,
    val quantity: Int,
    val imageUrl: String = "image.jpg",


    @ColumnInfo(name = "tId", defaultValue = 0.toString())
    val tId: String = ""
)
