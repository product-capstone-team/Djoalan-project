package com.dicoding.djoalanapplication.util

import android.content.Context
import com.dicoding.djoalanapplication.data.AppDatabase
import com.dicoding.djoalanapplication.data.product.ProductRepository
import com.dicoding.djoalanapplication.data.transaction.TransactionRepository
import com.dicoding.djoalanapplication.data.user.UserRepository
import com.dicoding.djoalanapplication.network.ApiConfig

object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val dao = AppDatabase.getInstance(context).userDao()
        val apiService = ApiConfig().getApiService(context = context)
        return UserRepository(dao, apiService)
    }

    fun provideProductRepository(context: Context): ProductRepository {
        val dao = AppDatabase.getInstance(context).productDao()
        val apiService = ApiConfig().getApiService(context = context)
        return ProductRepository(dao, apiService)
    }

    fun provideTransactionRepository(context: Context): TransactionRepository {
        val transactionDao = AppDatabase.getInstance(context).transactionDao()
        val productDao = AppDatabase.getInstance(context).productDao()
        val apiService = ApiConfig().getApiService(context)
        val itemDao = AppDatabase.getInstance(context).itemDao()
        return TransactionRepository(transactionDao, apiService, productDao, itemDao)
    }

}