package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

// Import Model và Retrofit
import com.example.foodapp.model.CartItem
import com.example.foodapp.network.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BagScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()

    // Khai báo biến lưu dữ liệu giỏ hàng từ MySQL
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var subtotal by remember { mutableStateOf(0.0) }
    val deliveryFee = 15000.0 // Phí ship cố định
    val total = subtotal + if(cartItems.isNotEmpty()) deliveryFee else 0.0

    // Tự động lấy dữ liệu khi mở màn hình
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.apiService.getCart(userId = 1) // Lấy giỏ hàng của user ID = 1
                if (response.success) {
                    cartItems = response.cart_items ?: emptyList()
                    subtotal = response.subtotal ?: 0.0
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Bag", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                // Đổ dữ liệu thật từ biến cartItems
                items(cartItems.size) { index ->
                    val item = cartItems[index]
                    CartItemRow(item, primaryOrange)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (cartItems.isEmpty()) {
                    item {
                        Text("Giỏ hàng đang trống!", color = Color.Gray, modifier = Modifier.padding(top = 20.dp))
                    }
                }
            }

            // Tổng kết thanh toán
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal", color = Color.Gray)
                Text("${subtotal} VND", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Delivery Fee", color = Color.Gray)
                Text("${if(cartItems.isNotEmpty()) deliveryFee else 0.0} VND", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("${total} VND", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primaryOrange)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if(cartItems.isNotEmpty()) navController.navigate("checkout")
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(cartItems.isNotEmpty()) primaryOrange else Color.LightGray
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = cartItems.isNotEmpty()
            ) {
                Text("CHECKOUT", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

// Cập nhật lại Item truyền vào class CartItem
@Composable
fun CartItemRow(item: CartItem, primaryOrange: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.image_url),
            contentDescription = item.name,
            modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "${item.quantity} x ${item.price} VND", color = Color.Gray, fontSize = 14.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("-", modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold)
            Text("${item.quantity}")
            Text("+", modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold, color = primaryOrange)
        }
    }
}