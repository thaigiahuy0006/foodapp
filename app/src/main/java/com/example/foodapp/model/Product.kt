package com.example.foodapp.model

data class Product(
    val id: Int,
    val eatery_id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val image_url: String
)

data class EateryDetailResponse(
    val success: Boolean,
    val eatery: Eatery?,
    val products: List<Product>?
)
data class ProductDetailResponse(
    val success: Boolean,
    val product: Product?,
    val message: String?
)