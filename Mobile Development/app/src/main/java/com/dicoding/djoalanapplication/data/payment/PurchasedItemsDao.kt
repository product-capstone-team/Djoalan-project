package com.dicoding.djoalanapplication.data.payment

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PurchasedItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfItems(items: List<PurchasedItems>)

    @Query("SELECT * FROM purchaseditems")
    fun getAllItems(): LiveData<List<PurchasedItems>>

    @Query("DELETE FROM purchaseditems")
    suspend fun deleteAllItems()

}