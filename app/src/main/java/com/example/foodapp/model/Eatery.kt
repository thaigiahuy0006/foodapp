package com.example.foodapp.model

data class Eatery(
    val id: String,
    val name: String,
    val address: String?,
    val distance: String?,
    val rating: Double?,
    val image_url: String?,
    val is_open: Any? = null,
    val description: String?
)