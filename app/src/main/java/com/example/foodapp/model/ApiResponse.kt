package com.example.foodapp.model

data class ApiResponse(
    val success: Boolean,
    val message: String?,
    val user_id: Int?,
    val restaurant_id: Int?,
    val restaurant_name: String?,
    val is_saved: Boolean? = null,
    val saved_eateries: List<Eatery>?,
    val orders: List<com.example.foodapp.model.Order>?
)