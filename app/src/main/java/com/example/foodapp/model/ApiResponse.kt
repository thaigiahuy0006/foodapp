package com.example.foodapp.model

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val user_id: Int? = null
)