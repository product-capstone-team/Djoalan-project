package com.dicoding.djoalanapplication.data.transaction

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun newOrder(item: List<Transaction>)

    @Query("SELECT * FROM `transaction`")
    suspend fun getAllOrders(): List<Transaction>

    @Query("DELETE FROM `transaction`")
    suspend fun deleteAllOrders()

    @androidx.room.Transaction
    @Query("SELECT * FROM `transaction`")
    fun getAllTransactionAndProduct(): LiveData<List<TransactionAndProduct>>

    @androidx.room.Transaction
    @Query("SELECT * FROM `transaction`")
    fun getAllTransactionAndPurchasedItems(): LiveData<List<TransactionAndPurchasedItems>>

}