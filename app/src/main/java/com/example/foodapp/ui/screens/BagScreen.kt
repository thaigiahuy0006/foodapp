package com.example.foodapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

import com.example.foodapp.model.CartItem
import com.example.foodapp.model.UserSession
import com.example.foodapp.network.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BagScreen(navController: NavController) {
    val context = LocalContext.current
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()

    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var subtotal by remember { mutableStateOf(0.0) }
    val deliveryFee = 15000.0
    val total = subtotal + if (cartItems.isNotEmpty()) deliveryFee else 0.0

    // Hàm lấy lại dữ liệu giỏ hàng theo ID khách hàng thực tế
    fun fetchCart() {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.apiService.getCart(userId = UserSession.userId)
                if (response.success) {
                    cartItems = response.cart_items ?: emptyList()
                    subtotal = response.subtotal ?: 0.0
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    LaunchedEffect(Unit) { fetchCart() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Giỏ hàng của tôi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
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
                items(cartItems.size) { index ->
                    val item = cartItems[index]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(item.image_url),
                            contentDescription = item.name,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "${item.price} VNĐ", color = Color.Gray, fontSize = 14.sp)
                        }

                        // Cột chứa Nút Xóa và Nút Tăng/Giảm
                        Column(horizontalAlignment = Alignment.End) {
                            // NÚT XÓA MÓN ĂN
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        try {
                                            val request = mapOf(
                                                "user_id" to UserSession.userId.toString(),
                                                "product_id" to item.product_id.toString()
                                            )
                                            val response = RetrofitClient.apiService.removeFromCart(request)
                                            if (response.success) {
                                                Toast.makeText(context, "Đã xóa món ăn!", Toast.LENGTH_SHORT).show()
                                                fetchCart() // Load lại giỏ hàng để cập nhật danh sách và tổng tiền
                                            }
                                        } catch (e: Exception) { e.printStackTrace() }
                                    }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
                            }

                            // Nút Tăng/Giảm số lượng
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "-",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable {
                                            if (item.quantity > 1) {
                                                coroutineScope.launch {
                                                    RetrofitClient.apiService.addToCart(
                                                        mapOf("user_id" to UserSession.userId.toString(), "product_id" to item.product_id.toString(), "quantity" to "-1")
                                                    )
                                                    fetchCart()
                                                }
                                            }
                                        },
                                    fontWeight = FontWeight.Bold, fontSize = 20.sp
                                )
                                Text("${item.quantity}", fontSize = 16.sp)
                                Text(
                                    "+",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable {
                                            coroutineScope.launch {
                                                RetrofitClient.apiService.addToCart(
                                                    mapOf("user_id" to UserSession.userId.toString(), "product_id" to item.product_id.toString(), "quantity" to "1")
                                                )
                                                fetchCart()
                                            }
                                        },
                                    fontWeight = FontWeight.Bold, color = primaryOrange, fontSize = 20.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (cartItems.isEmpty()) {
                    item { Text("Giỏ hàng đang trống!", color = Color.Gray, modifier = Modifier.padding(top = 20.dp)) }
                }
            }

            // Tổng kết
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tạm tính", color = Color.Gray)
                Text("$subtotal VNĐ", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Phí giao hàng", color = Color.Gray)
                Text("${if(cartItems.isNotEmpty()) deliveryFee else 0.0} VNĐ", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tổng cộng", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("$total VNĐ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primaryOrange)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { if(cartItems.isNotEmpty()) navController.navigate("checkout") },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if(cartItems.isNotEmpty()) primaryOrange else Color.LightGray),
                shape = RoundedCornerShape(8.dp),
                enabled = cartItems.isNotEmpty()
            ) {
                Text("THANH TOÁN", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}