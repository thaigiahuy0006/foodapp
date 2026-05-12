package com.example.foodapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Thay 10.0.2.2 bằng địa chỉ IP IPv4 của máy bạn nếu dùng điện thoại thật
    private const val BASE_URL = "http://10.0.2.2/foodapp/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}