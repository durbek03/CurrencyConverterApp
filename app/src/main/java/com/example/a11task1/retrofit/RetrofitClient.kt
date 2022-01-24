package com.example.a11task1.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    var retrofit: Retrofit? = null

    fun getRetrofit(baseUrl: String): RetrofitService {
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(OkHttpClient())
            .build()

        return retrofit!!.create(RetrofitService::class.java)
    }
}