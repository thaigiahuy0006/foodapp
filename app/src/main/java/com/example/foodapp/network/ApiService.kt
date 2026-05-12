package com.example.foodapp.network

import com.example.foodapp.model.ApiResponse
import com.example.foodapp.model.Category
import com.example.foodapp.model.Eatery
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.example.foodapp.model.CartResponse
import com.example.foodapp.model.EateryDetailResponse
import com.example.foodapp.model.Order
import com.example.foodapp.model.ProductDetailResponse
import com.example.foodapp.model.ProfileResponse
import retrofit2.http.Query
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.Part
import com.example.foodapp.model.UploadAvatarResponse


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
    @GET("get_eatery_details.php")
    suspend fun getEateryDetails(@Query("id") eateryId: String): EateryDetailResponse

    @GET("get_eateries_by_category.php")
    suspend fun getEateriesByCategory(@Query("category_id") categoryId: String): List<Eatery>
    @GET("get_product_details.php")
    suspend fun getProductDetails(@Query("id") productId: String): ProductDetailResponse
    @GET("search_eateries.php")
    suspend fun searchEateries(@Query("query") query: String): List<Eatery>
    @GET("get_profile.php")
    suspend fun getProfile(@Query("user_id") userId: Int): ProfileResponse

    @POST("update_profile.php")
    suspend fun updateProfile(@Body request: Map<String, String>): ApiResponse
    @Multipart
    @POST("upload_avatar.php")
    suspend fun uploadAvatar(
        @Part("user_id") userId: RequestBody, // user_id gửi dạng text
        @Part avatar: MultipartBody.Part      // file ảnh gửi dạng file
    ): UploadAvatarResponse
}