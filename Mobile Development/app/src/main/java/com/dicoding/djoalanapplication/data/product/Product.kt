package com.dicoding.djoalanapplication.data.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val productId: String = "_id",

    @ColumnInfo(name = "product_name")
    val productName: String,
    val brand: String,

    @ColumnInfo(name = "expire_date")
    val expireDate: String,
    val price: Int,
    val category: String,

    val quantity: Int,

    @ColumnInfo(name = "image_url")
    val imageUrl: String = "image.jpg",

    @ColumnInfo(name = "tId", defaultValue = 0.toString())
    val tId: String = ""


)
