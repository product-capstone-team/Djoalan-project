package com.dicoding.djoalanapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dicoding.djoalanapplication.data.payment.PurchasedItems
import com.dicoding.djoalanapplication.data.payment.PurchasedItemsDao
import com.dicoding.djoalanapplication.data.product.Product
import com.dicoding.djoalanapplication.data.product.ProductDao
import com.dicoding.djoalanapplication.data.transaction.Transaction
import com.dicoding.djoalanapplication.data.transaction.TransactionDao
import com.dicoding.djoalanapplication.data.user.User
import com.dicoding.djoalanapplication.data.user.UserDao
import com.dicoding.djoalanapplication.util.InitialDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [Product::class, User::class, Transaction::class, PurchasedItems::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun itemDao(): PurchasedItemsDao

    companion object {

        private val applicationScope = CoroutineScope(SupervisorJob())

        private const val DATABASE_NAME = "djoalan-database"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()

//                .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    // add loginData
//                    instance?.let { database ->
//                        applicationScope.launch {
//                            val dao = database.productDao()
//                            dao.insertProducts(InitialDataSource.getDummyProducts())
//                        }
//                    }
//                }
//            }).fallbackToDestructiveMigration().build()
        }
    }
}