package com.dicoding.djoalanapplication.data.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dicoding.djoalanapplication.network.ApiService
import com.dicoding.djoalanapplication.network.response.*
import com.dicoding.djoalanapplication.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UserRepository(
    private val dao: UserDao,
    private val apiService: ApiService
) {

    private val _loadingState = MutableLiveData<Boolean>()
    val getLoadingState: LiveData<Boolean> = _loadingState

    private val _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User> = _userInfo

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    fun login(email: String, password: String) {

        _loadingState.value = true
        val service = apiService.login(email, password)
        service.enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _loadingState.value = false
                if (response.isSuccessful && response.body() != null) {
                    _loginResponse.value = response.body()
                } else {
                    Log.d(TAG, "didn't get any loginData")
                    _loginResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loadingState.value = false
                Log.d(TAG, "API Service doesn't work")
            }

        })
    }

    fun register(nama: String, email: String, password: String) {
        _loadingState.value = true
        val service = apiService.register(nama, email, password)
        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _loadingState.value = false
                if (response.isSuccessful && response.body() != null) {
                    _registerResponse.value = response.body()
                } else {
                    Log.d(TAG, "didn't get any loginData")
                    _registerResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _loadingState.value = false
                Log.d(TAG, "API Service doesn't work")
            }

        })
    }

    suspend fun saveUserInfo(user: User) {
        withContext(Dispatchers.IO) {
            dao.insertUser(user)
        }
    }

    suspend fun deleteUserInfo() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }

    suspend fun getUserInfo(): User {
        return dao.getUser()
    }

    fun updateAccountToBusiness(
        id: String,
        storeName: String,
        company: String,
        location: StoreLocation): LiveData<UpdateAccResponse> {

        _loadingState.value = true
        val requestBody = UpdateAccRequest(
            storeName,
            company,
            storeLocation = location
        )
        val data = MutableLiveData<UpdateAccResponse>()

        val service = apiService.changeToBusinessAccount(
            id = id,
            requestBody
        )
        service.enqueue(object : Callback<UpdateAccResponse> {

            override fun onResponse(
                call: Call<UpdateAccResponse>,
                response: Response<UpdateAccResponse>
            ) {
                _loadingState.value = false
                if (response.isSuccessful && response.body() != null) {
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<UpdateAccResponse>, t: Throwable) {
                _loadingState.value = false
                Log.d("cek", "onFailure update change account")
            }

        })

        return data

    }

    fun logout(): LiveData<LogoutResponse> {
        val data = MutableLiveData<LogoutResponse>()
        _loadingState.value = true
        val service = apiService.logout()
        service.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>
            ) {
                _loadingState.value = false
                Log.d("cek logout", response.body()?.message ?: "no message")
                if (response.isSuccessful && response.body() != null) {
                    data.value = response.body()
                } else {
                    Log.d("cek logout", "fail response")
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Log.d("cek logout", "fail onfailure logout")
            }

        })

        return data
    }

    fun getCompanyInfo(companyID: String): LiveData<Result<GetStoreInfoResponse>> = liveData {

        emit(Result.Loading)
        try {

            val service = apiService.getStoreInfo(companyID)
            emit(Result.Success(service))
            val result = service.data?.user
//            val storeEntity = result?.let {
//
//                User(
//                    it.id,
//                    it.nama,
//                    it.email,
//                    it.password,
//                    it.isBusinessAcc,
//                    it.storeName,
//                    it.company,
//                    it.storeLocation?.lat.toString().toDoubleOrNull(),
//                    it.storeLocation?.lon.toString().toDoubleOrNull()
//
//                )
//            }
//            dao.insertUser(storeEntity as User)
//
//            emit(Result.Success(service))

        } catch (e: IOException) {

            emit(Result.Error(e.message.toString()))
            Log.d("cek getCompanyInfo", e.message.toString())
            Log.d("cek getCompanyInfo", "error: ${e.cause}")
        }


    }

    companion object {
        private const val TAG = "user_repository"
    }

}