package com.dicoding.djoalanapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.djoalanapplication.data.user.User
import com.dicoding.djoalanapplication.data.user.UserRepository
import com.dicoding.djoalanapplication.network.response.*
import com.dicoding.djoalanapplication.util.Result
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val userRepo: UserRepository): ViewModel() {

    val isLoading: LiveData<Boolean> = userRepo.getLoadingState

    fun saveUserInfo(user: User) = viewModelScope.launch { userRepo.saveUserInfo(user) }
    fun deleteUserInfo() = viewModelScope.launch { userRepo.deleteUserInfo() }

    fun login(email: String, password: String): LiveData<LoginResponse> {
        userRepo.login(email, password)
        return userRepo.loginResponse
    }

    fun register(name: String, email: String, password: String): LiveData<RegisterResponse> {
        userRepo.register(name, email, password)
        return userRepo.registerResponse
    }

    fun getUserInfoFromDatabase(): LiveData<User> {
        val user = MutableLiveData<User>()
        viewModelScope.launch {
            user.value = userRepo.getUserInfo()
        }
        return user
    }

    fun update(
        id: String,
        storeName: String,
        company: String,
        location: StoreLocation
    ): LiveData<UpdateAccResponse> = userRepo.updateAccountToBusiness(
        id, storeName, company, location
    )

    fun logout(): LiveData<LogoutResponse> = userRepo.logout()

    fun getStoreInfo(storeID: String): LiveData<Result<GetStoreInfoResponse>> = userRepo.getCompanyInfo(storeID)

}