package com.example.foodapp.model

data class CartItem(
    val cart_id: Int,
    val quantity: Int,
    val product_id: Int,
    val name: String,
    val price: Double,
    val image_url: String
)

data class CartResponse(
    val success: Boolean,
    val message: String?,
    val cart_items: List<CartItem>?,
    val subtotal: Double?
)