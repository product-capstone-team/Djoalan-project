package com.dicoding.djoalanapplication.network

import com.dicoding.djoalanapplication.network.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface ApiService {

    // TODO: USER RELATED API

    @FormUrlEncoded
    @POST("sign-up")
    fun register(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<RegisterResponse>

    @FormUrlEncoded
    @POST("sign-in")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @GET("logout")
    fun logout(): Call<LogoutResponse>

    @PUT("users/{id}")
    fun changeToBusinessAccount(
        @Path("id") id: String,
        @Body body: UpdateAccRequest
    ) : Call<UpdateAccResponse>

    @GET("users/{id}")
    suspend fun getStoreInfo(
        @Path("id") id: String
    ): GetStoreInfoResponse




    // TODO: PRODUCT RELATED API

    @Multipart
    @POST("add-item")
    fun addItem(
        @Part("productId") productId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("expiredDate") expiredDate: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part imageUrl: MultipartBody.Part
    ) : Call<AddItemResponse>

    @FormUrlEncoded
    @POST("addToCart/{id}")
    suspend fun getItem(
        @Path("id") id: String,
        @Field("productId") barcodeId: String
    ) : GetProductResponse

    @GET("itemStock")
    suspend fun getAllStock(): GetStockResponse

    @GET("bestPrice")
    fun getBestPrice(): Call<BestPriceResponse>




    // TODO: TRANSACTION RELATED API

    @GET("transaction/{id}")
    suspend fun getTransaction(
        @Path("id") id: String
    ) : GetOrdersResponse

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("payment")
    fun confirmPayment(
        @Body body: RequestBodyPayment
    ) : Call<PaymentResponse>

    @FormUrlEncoded
    @POST("transaction")
    fun createNewTransaction(
        @Field("productId") productId: List<String>
    ) : Call<AddTransactionResponse>


}