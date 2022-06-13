package com.dicoding.djoalanapplication.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SessionViewModel @Inject constructor(private val pref: SessionManager): ViewModel() {

    fun getUserStateLogin(): LiveData<Boolean> {
        return pref.getUserLoginState().asLiveData()
    }

    fun getUserAccType(): LiveData<Boolean> {
        return pref.getUserAccType().asLiveData()
    }

    fun saveUserState(isLogin: Boolean, isBusinessAcc: Boolean) {
        viewModelScope.launch {
            pref.saveLoginState(isLogin, isBusinessAcc)
        }
    }

    fun deleteSession() {
        viewModelScope.launch {
            pref.deleteSession()
        }
    }

    fun getNavState(): LiveData<Boolean> {
        return pref.getNavigationState().asLiveData()
    }

    fun moveIntoTopNav() {
        viewModelScope.launch {
            pref.changeNavigationState(false)
        }
    }

    fun moveIntoBottomNav() {
        viewModelScope.launch {
            pref.changeNavigationState(true)
        }
    }

    fun saveStoreId(storeId: String) {
        viewModelScope.launch {
            pref.saveStoreId(storeId)
        }
    }

    fun getStoreId(): LiveData<String> {
        return pref.getStoreId().asLiveData()
    }
}