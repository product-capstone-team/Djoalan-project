package com.dicoding.djoalanapplication.data.transaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.djoalanapplication.data.payment.PurchasedItems
import com.dicoding.djoalanapplication.data.payment.PurchasedItemsDao
import com.dicoding.djoalanapplication.data.product.Product
import com.dicoding.djoalanapplication.data.product.ProductDao
import com.dicoding.djoalanapplication.network.ApiService
import com.dicoding.djoalanapplication.network.response.AddTransactionResponse
import com.dicoding.djoalanapplication.network.response.PaymentResponse
import com.dicoding.djoalanapplication.network.response.RequestBodyPayment
import com.dicoding.djoalanapplication.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val apiService: ApiService,
    private val productDao: ProductDao,
    private val itemDao: PurchasedItemsDao
) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getTransaction(userId: String): LiveData<Result<List<TransactionAndPurchasedItems>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getTransaction(userId)
            if (!response.error && response.data?.userTransaction != null ) {
                if (response.data.userTransaction.isNotEmpty()) {

                    val listItems = response.data.userTransaction
                    val orderEntity = listItems.map { data ->

                        val listOfItems = ArrayList<PurchasedItems>()
                        data.listOfItems.forEach {
                            val items = PurchasedItems(
                                it.productId,
                                it.name,
                                it.brand,
                                it.expiredDate,
                                it.price,
                                it.category,
                                it.quantity,
                                it.imageUrl,
                                data.id
                            )
                            listOfItems.add(items)
                        }
                        itemDao.deleteAllItems()
                        itemDao.insertListOfItems(listOfItems)

                        Transaction(
                            data.id,
                            data.total,
                            data.date
                        )

                    } as List<Transaction>
                    transactionDao.deleteAllOrders()
                    transactionDao.newOrder(orderEntity)

                    val localData: LiveData<Result<List<TransactionAndPurchasedItems>>> = transactionDao.getAllTransactionAndPurchasedItems().map { Result.Success(it) }
                    emitSource(localData)

                } else {
                    emit(Result.Error("cause: no transaction found"))
                }

            } else {
                emit(Result.Error("cause: ${response.message}"))
            }

        } catch (e: Exception) {

            Log.d("cek repo", "getTransaction: ${e.message.toString()}")
            Log.d("cek repo", "getTransaction: ${e.localizedMessage}")
            Log.d("cek repo", "getTransaction: ${e.cause}")
            emit(Result.Error(e.message.toString()))

        }
    }



    fun confirmPay(total: Int, paymentMethod: String): LiveData<PaymentResponse> {

        _isLoading.value = true
        val responseData = MutableLiveData<PaymentResponse>()
        val data = RequestBodyPayment(
            total,
            "ID_$paymentMethod"
        )

        val service = apiService.confirmPayment(data)
        service.enqueue(object : Callback<PaymentResponse> {
            override fun onResponse(
                call: Call<PaymentResponse>,
                response: Response<PaymentResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    responseData.value = response.body()
                } else {
                    Log.d("cek confirm pay", "response error and or resp body null")
                }
            }

            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                Log.d("cek confirm pay", "response error and or resp body null")
            }

        })

        return responseData
    }

    fun addTransaction(cartProducts: List<String>): LiveData<AddTransactionResponse> {

        _isLoading.value = true
        val data = MutableLiveData<AddTransactionResponse>()

        val service = apiService.createNewTransaction(cartProducts)
        service.enqueue(object : Callback<AddTransactionResponse> {
            override fun onResponse(
                call: Call<AddTransactionResponse>,
                response: Response<AddTransactionResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {

                    data.value = response.body()
                } else {

                    Log.d("cek", "response body null")
                }

            }

            override fun onFailure(call: Call<AddTransactionResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("cek", "failed: ${t.message.toString()}")

            }

        })

        return data
    }

    suspend fun deleteAllTransaction() {
        transactionDao.deleteAllOrders()
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAll()
    }

    suspend fun deleteAllPurchasedItems() {
        itemDao.deleteAllItems()
    }



}