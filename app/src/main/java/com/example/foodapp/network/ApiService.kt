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

    @POST("admin_register.php")
    suspend fun adminRegister(@Body request: Map<String, String>): ApiResponse

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
    ): ApiResponse
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
    @POST("admin_login.php")
    suspend fun adminLogin(@Body request: Map<String, String>): ApiResponse


// API Quản lý món ăn
    @GET("admin_manage_products.php?action=get_all")
    suspend fun adminGetAllProducts(@Query("eatery_id") eateryId: Int): List<Map<String, String>>

    @POST("admin_manage_products.php?action=add")
    suspend fun adminAddProduct(@Body request: Map<String, String>): ApiResponse

    @GET("admin_manage_products.php?action=delete")
    suspend fun adminDeleteProduct(@Query("id") id: Int): ApiResponse

    @POST("admin_manage_products.php?action=toggle_status")
    suspend fun adminToggleProductStatus(@Body request: Map<String, String>): ApiResponse

    @POST("admin_manage_products.php?action=update")
    suspend fun adminUpdateProduct(@Body product: Map<String, String>): ApiResponse




    // Quản lý danh mục
    @POST("admin_manage_categories.php?action=add")
    suspend fun adminAddCategory(@Body category: Map<String, String>): ApiResponse

    // API Xóa sản phẩm khỏi giỏ hàng
    @POST("remove_from_cart.php")
    suspend fun removeFromCart(@Body request: Map<String, String>): ApiResponse

    @POST("toggle_saved.php")
    suspend fun toggleSavedEatery(@Body request: Map<String, String>): ApiResponse // ApiResponse có thể cần thêm trường is_saved: Boolean?

    // Lấy danh sách nhà hàng đã lưu
    @GET("get_saved_eateries.php")
    suspend fun getSavedEateries(@Query("user_id") userId: Int): ApiResponse

    // ==========================================
    // API QUẢN LÝ ĐƠN HÀNG (DÙNG FILE GỘP CHUNG)
    // ==========================================

    @GET("admin_manage_orders.php?action=get_all")
    suspend fun adminGetAllOrders(): List<Map<String, String>>

    @POST("admin_manage_orders.php?action=update_status")
    suspend fun adminUpdateOrderStatus(@Body request: Map<String, String>): ApiResponse

    @GET("admin_manage_orders.php?action=delete")
    suspend fun adminDeleteOrder(@Query("id") id: Int): ApiResponse

    // API Quản lý hồ sơ nhà hàng
    @GET("admin_profile.php?action=get")
    suspend fun adminGetProfile(@Query("id") id: Int): Map<String, Any>  // Hãy đảm bảo ApiResponse của bạn có trường "data: Map<String, String>?" hoặc tương tự

    @POST("admin_profile.php?action=toggle_open")
    suspend fun adminToggleOpenStatus(@Body request: Map<String, String>): ApiResponse

    @POST("admin_profile.php?action=update_info")
    suspend fun adminUpdateProfileInfo(@Body request: Map<String, String>): ApiResponse

    @GET("admin_profile.php?action=delete_account")
    suspend fun adminDeleteRestaurantAccount(@Query("id") id: Int): ApiResponse
    // API Lấy chi tiết các món trong đơn hàng
    @GET("get_order_details.php")
    suspend fun getOrderDetails(@Query("order_id") orderId: Int): Map<String, Any>

    // API Hủy đơn hàng
    @POST("cancel_order.php")
    suspend fun cancelOrder(@Body request: Map<String, String>): ApiResponse
    // API Khách hàng cập nhật trạng thái đơn hàng (Đã nhận hàng)
    @POST("update_order_status.php")
    suspend fun updateOrderStatus(@Body request: Map<String, String>): ApiResponse

}