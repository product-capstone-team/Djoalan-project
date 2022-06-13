package com.dicoding.djoalanapplication.network

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {

//    private var BASE_URL = "http://localhost:5000/"
    private var BASE_URL = "https://djoalan-api-service-zrq2bjm4oq-uc.a.run.app/"

    fun getApiService(context: Context) : ApiService {
        val interceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)

        val client = OkHttpClient.Builder()
            .addInterceptor(AddCookiesInterceptor(context))
            .addInterceptor(ReceiveCookiesInterceptor(context))
            .addInterceptor(interceptor)
            .cache(null)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

}

private val COOKIES_KEY = "cookie_key"

class AddCookiesInterceptor(private val context: Context): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(context)
            .getStringSet(COOKIES_KEY, HashSet()) as HashSet<String>

        var cookiestring: String = ""

        for (cookie in preferences) {
            val parser = cookie.split(";")
            cookiestring = cookiestring + parser[0] + "; "
            Log.d("cek cookies Add", cookie)
            builder.addHeader("Cookie", cookiestring)
        }

        return chain.proceed(builder.build())
    }

}

class ReceiveCookiesInterceptor(private val context: Context): Interceptor {

    @JvmField
    val setCookieHeader = "Set-Cookie"

    @SuppressLint("MutatingSharedPrefs")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        Log.d("cek intercept", originalResponse.request.method)
        Log.d("cek intercept", originalResponse.request.url.toString())
        Log.d("cek intercept", originalResponse.headers.names().toString())
        Log.d("cek intercept", originalResponse.headers("content-type").toString())

        if (originalResponse.headers(setCookieHeader).isNotEmpty()) {
            val cookies = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getStringSet(COOKIES_KEY, HashSet()) as HashSet<String>

            Log.d("cek headers", originalResponse.headers.names().toString())

            originalResponse.headers(setCookieHeader).forEach {
                cookies.add(it)
                Log.d("cek cookies Receive", it)
            }

            if (originalResponse.request.url.toString() == "https://djoalan-api-service-zrq2bjm4oq-uc.a.run.app/logout") {

                PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .clear()
                    .apply()

            } else {

                PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .putStringSet(COOKIES_KEY, cookies)
                    .apply()
            }

        }

        return originalResponse
    }

}