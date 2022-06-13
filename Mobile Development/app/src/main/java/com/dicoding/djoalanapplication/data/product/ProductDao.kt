package com.dicoding.djoalanapplication.data.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(items: List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneProduct(item: Product)

    @Query("SELECT * FROM product")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("DELETE FROM product")
    suspend fun deleteAll()

    @Query("DELETE FROM product WHERE product_id = :productId")
    suspend fun deleteOneProduct(productId: String)

}