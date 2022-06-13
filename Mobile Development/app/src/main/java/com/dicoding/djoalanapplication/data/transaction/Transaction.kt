package com.dicoding.djoalanapplication.data.transaction

import androidx.room.*
import com.dicoding.djoalanapplication.data.payment.PurchasedItems
import com.dicoding.djoalanapplication.data.product.Product
import com.google.gson.annotations.SerializedName

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey
    @ColumnInfo(name = "order_id")
    val orderId: String,
    @ColumnInfo(name = "total_price")
    val totalPrice: Int,
    val date: String,

)

data class TransactionAndProduct(
    @Embedded
    val transaction: Transaction,

    @Relation(
        parentColumn = "order_id",
        entityColumn = "tId"
    )
    val products: List<Product>? = null
)

data class TransactionAndPurchasedItems(

    @Embedded
    val transaction: Transaction,

    @Relation(
        parentColumn = "order_id",
        entityColumn = "tId"
    )
    val purchasedItems: List<PurchasedItems>? = null

)
