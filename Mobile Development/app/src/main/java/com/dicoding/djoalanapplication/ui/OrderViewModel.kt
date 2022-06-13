package com.dicoding.djoalanapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderViewModel: ViewModel() {

    private val _listProducts = MutableLiveData<List<String>>()
    val listProduct: LiveData<List<String>> = _listProducts

    private val _paymentMethod = MutableLiveData<String?>()
    val paymentMethod: LiveData<String?> = _paymentMethod

    private val _price = MutableLiveData<Int>()
    val price: LiveData<Int> = _price
    val formatPrice: LiveData<String> = Transformations.map(_price) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("IDR")
        format.format(it)
    }

    init {
        resetOrder()
    }

    fun setPaymentMethod(method: String?) {
        _paymentMethod.value = method
    }

    fun setListOfProducts(items: List<String>) {
        _listProducts.value = items
    }

    fun setPrice(amount: Int) {
        _price.value = amount
    }

    fun resetOrder() {
        _paymentMethod.value = ""
        _listProducts.value = ArrayList<String>()
        _price.value = 0
    }

}