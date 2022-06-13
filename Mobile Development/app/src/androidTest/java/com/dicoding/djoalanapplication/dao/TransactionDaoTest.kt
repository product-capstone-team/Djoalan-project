package com.dicoding.djoalanapplication.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dicoding.djoalanapplication.DummyData
import com.dicoding.djoalanapplication.data.AppDatabase
import com.dicoding.djoalanapplication.data.product.ProductDao
import com.dicoding.djoalanapplication.data.transaction.TransactionDao
import com.dicoding.djoalanapplication.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*


class TransactionDaoTest {

    @get:Rule
    var instantTaskExec = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var productDao: ProductDao

    // dummy loginData
    private val transactionEntities = DummyData.generateTransactionEntities()
    private val products = DummyData.generateProductEntities()

    @Before
    fun initDB() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java).build()
        transactionDao = database.transactionDao()
        productDao = database.productDao()
    }

    @After
    fun closeDB() = database.close()

    @Test
    fun checkRelational() = runBlocking {
        transactionDao.newOrder(transactionEntities)
        productDao.insertProducts(products)

        val actualData = transactionDao.getAllTransactionAndProduct().getOrAwaitValue()

        assertNotNull(actualData)
        assertEquals(transactionEntities.orderId, actualData[0].transaction.orderId)

    }

}