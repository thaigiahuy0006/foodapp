package com.example.foodapp.network

import com.example.foodapp.model.ApiResponse
import com.example.foodapp.model.Category
import com.example.foodapp.model.Eatery
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.example.foodapp.model.CartResponse
import com.example.foodapp.model.Order
import retrofit2.http.Query

interface ApiService {
    // API Đăng nhập / Đăng ký cũ
    @POST("register.php")
    suspend fun register(@Body request: Map<String, String>): ApiResponse

    @POST("login.php")
    suspend fun login(@Body request: Map<String, String>): ApiResponse

    // CÁC API MỚI THÊM
    @GET("get_categories.php")
    suspend fun getCategories(): List<Category>

    @GET("get_popular_eateries.php")
    suspend fun getPopularEateries(): List<Eatery>
    @POST("add_to_cart.php")
    suspend fun addToCart(@Body request: Map<String, String>): ApiResponse

    // API lấy giỏ hàng (truyền user_id lên URL)
    @GET("get_cart.php")
    suspend fun getCart(@Query("user_id") userId: Int): CartResponse
    // Trong interface ApiService
    @POST("place_order.php")
    suspend fun placeOrder(@Body request: Map<String, String>): ApiResponse

    @GET("get_orders.php")
    suspend fun getOrders(
        @Query("user_id") userId: Int,
        @Query("status") status: String
    ): List<Order>

}