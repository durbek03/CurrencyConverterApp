package com.example.a11task1.retrofit

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {

    @GET("uz/exchange-rates/json/")
    fun getCurrency(): Call<List<RetrofitCurrency>>

    @Headers("x-rapidapi-host: currency-exchange.p.rapidapi.com",
        "x-rapidapi-key: 37f60e0f28mshc0f871e06f7c3e4p1c9610jsn8767086cf81f")
    @GET("exchange?")
    suspend fun getMultiplier(@Query("from") from: String, @Query("to") to: String, @Query("q") q:String): Response<Double>
}