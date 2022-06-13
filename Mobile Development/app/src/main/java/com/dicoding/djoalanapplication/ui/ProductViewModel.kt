package com.dicoding.djoalanapplication.ui

import androidx.lifecycle.*
import com.dicoding.djoalanapplication.data.product.Product
import com.dicoding.djoalanapplication.data.product.ProductRepository
import com.dicoding.djoalanapplication.network.response.AddItemResponse
import com.dicoding.djoalanapplication.network.response.BestPriceResponse
import com.dicoding.djoalanapplication.network.response.GetOrdersResponse
import com.dicoding.djoalanapplication.network.response.GetProductResponse
import com.dicoding.djoalanapplication.util.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProductViewModel(private val repository: ProductRepository): ViewModel() {

    val isLoading: LiveData<Boolean> = repository.getLoadingState

    fun addItem(
        productId: RequestBody,
        name: RequestBody,
        brand: RequestBody,
        expiredDate: RequestBody,
        price: RequestBody,
        category: RequestBody,
        quantity: RequestBody,
        file: MultipartBody.Part
    ): LiveData<AddItemResponse> = repository.addItem(productId, name, brand, expiredDate, price, category, quantity, file)

    fun getItem(storeId: String, barcodeProduct: String): LiveData<Result<List<Product>>> =
        repository.getItem(storeId, barcodeProduct)

    fun getAllStock(): LiveData<Result<List<Product>>> = repository.getAllStockProduct()

    fun deleteAllProductsInDatabase() = viewModelScope.launch {
        repository.deleteAllProducts()
    }

    fun getBestPrice(): LiveData<BestPriceResponse> = repository.getBestPrice()
}