package com.example.foodapp.model

data class Order(
    val id: Int,
    val user_id: Int,
    val total_price: Double,
    val status: String,
    val address: String,
    val phone: String,
    val created_at: String
)