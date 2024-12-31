package com.softmintindia.pgsdk.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // Base URL for the API
//    private const val BASE_URL = "https://m.easyswift.in/"
    private const val BASE_URL = "http://192.168.1.31:8088/softmint-upi/"

    // Initialize the Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Lazy initialization of the API interface
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}