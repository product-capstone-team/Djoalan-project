package com.dicoding.djoalanapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.djoalanapplication.data.transaction.TransactionRepository
import com.dicoding.djoalanapplication.network.response.AddTransactionResponse
import com.dicoding.djoalanapplication.network.response.PaymentResponse
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository): ViewModel() {

    fun getListOfTransaction(userId: String) = repository.getTransaction(userId)

    val isLoading: LiveData<Boolean> = repository.isLoading
    fun confirmPayment(total: Int, paymentMethod: String): LiveData<PaymentResponse> = repository.confirmPay(total, paymentMethod)
    fun addTransaction(items: List<String>): LiveData<AddTransactionResponse> = repository.addTransaction(items)

    fun deleteAllTransactionAndProducts() = viewModelScope.launch {
        repository.deleteAllTransaction()
        repository.deleteAllProducts()
        repository.deleteAllPurchasedItems()
    }
}