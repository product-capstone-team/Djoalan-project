package com.dicoding.djoalanapplication.data.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.djoalanapplication.network.ApiService
import com.dicoding.djoalanapplication.network.response.*
import com.dicoding.djoalanapplication.util.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository(
    private val dao: ProductDao,
    private val apiService: ApiService
) {

    private val _loadingState = MutableLiveData<Boolean>()
    val getLoadingState: LiveData<Boolean> = _loadingState

    suspend fun insertProducts(items: List<Product>) {
        dao.insertProducts(items)
    }

    suspend fun deleteAllProducts() {
        dao.deleteAll()
    }



    fun addItem(
        productId: RequestBody,
        name: RequestBody,
        brand: RequestBody,
        expiredDate: RequestBody,
        price: RequestBody,
        category: RequestBody,
        quantity: RequestBody,
        file: MultipartBody.Part
    ) : LiveData<AddItemResponse> {
        _loadingState.value = true
        val data = MutableLiveData<AddItemResponse>()
        val service = apiService.addItem(productId, name, brand, expiredDate, price, category, quantity, file)
        service.enqueue(object : Callback<AddItemResponse> {
            override fun onResponse(
                call: Call<AddItemResponse>,
                response: Response<AddItemResponse>
            ) {
                _loadingState.value = false
                if (response.isSuccessful && response.body() != null) {
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<AddItemResponse>, t: Throwable) {
                _loadingState.value = false
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }


        })

        return data
    }


    fun getItem(storeId: String, productId: String): LiveData<Result<List<Product>>> = liveData {
        emit(Result.Loading)
        try {

            val service = apiService.getItem(storeId, productId)
            val result = service.getItems?.get(0)
            if (result != null) {


                val product = result.let { data ->
                    Product(
                        data.productId,
                        data.name,
                        data.brand,
                        data.expiredDate,
                        data.price,
                        data.category,
                        data.quantity,
                        data.imageUrl
                    )
                }
                dao.deleteOneProduct(product.productId)
//                dao.deleteAll()
                dao.insertOneProduct(product)
            }


        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.d(TAG, "cause: ${e.message.toString()}")
        }

        val localData: LiveData<Result<List<Product>>> = dao.getAllProducts().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getAllStockProduct(): LiveData<Result<List<Product>>> = liveData {
        emit(Result.Loading)
        try {

            val service = apiService.getAllStock()
            val result = service.data

            if (result != null) {
                val data = result.map {
                    Product(
                        it.productId,
                        it.name,
                        it.brand,
                        it.expiredDate,
                        it.price,
                        it.category,
                        it.quantity,
                        it.imageUrl
                    )
                }
                dao.insertProducts(data)
            }

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.d(TAG, e.message.toString())
        }

        val localData: LiveData<Result<List<Product>>> = dao.getAllProducts().map { Result.Success(it) }
        emitSource(localData)

    }

    fun getBestPrice(): LiveData<BestPriceResponse> {
        _loadingState.value = true
        val data = MutableLiveData<BestPriceResponse>()
        val service = apiService.getBestPrice()
        service.enqueue(object : Callback<BestPriceResponse> {
            override fun onResponse(
                call: Call<BestPriceResponse>,
                response: Response<BestPriceResponse>
            ) {
                _loadingState.value = false
                if (response.isSuccessful && response.body() != null) {
                    data.value = response.body()
                } else {
                    Log.d(TAG, "response body null")
                }
            }

            override fun onFailure(call: Call<BestPriceResponse>, t: Throwable) {
                _loadingState.value = false
                Log.d(TAG, "onFailure Retrofit")
            }

        })

        return data
    }

    companion object {
        private const val TAG = "cek productRepository"
    }


}