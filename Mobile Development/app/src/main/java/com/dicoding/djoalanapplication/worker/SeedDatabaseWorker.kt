package com.dicoding.djoalanapplication.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.dicoding.djoalanapplication.data.AppDatabase
import com.dicoding.djoalanapplication.data.product.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class SeedDatabaseWorker(context: Context, worker: WorkerParameters): CoroutineWorker(context, worker) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            applicationContext.assets.open("item.json").use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val productType = object : TypeToken<List<Product>>() {}.type
                    val listOfProducts: List<Product> = Gson().fromJson(jsonReader, productType)

                    val db = AppDatabase.getInstance(applicationContext)
                    db.productDao().insertProducts(listOfProducts)
                    Log.d(TAG, "success prepopulate")
                    Result.success()
                }
            }
        } catch (err: IOException) {
            Log.d(TAG, "Error Prepopulate LoginData", err)
            Result.failure()
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "debugSeedWorker"
    }
}