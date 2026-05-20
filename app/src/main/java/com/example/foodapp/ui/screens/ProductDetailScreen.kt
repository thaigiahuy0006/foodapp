package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.example.foodapp.model.Product
import com.example.foodapp.network.RetrofitClient

@Composable
fun ProductDetailScreen(navController: NavController, productId: String = "1") {
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()

    var showMessage by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(1) }

    // Biến lưu dữ liệu món ăn từ DB
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(productId) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.apiService.getProductDetails(productId)
                if (response.success) {
                    product = response.product
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = primaryOrange)
        }
        return
    }

    val basePrice = product?.price ?: 0.0
    val totalPrice = basePrice * quantity

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF12151C)) {
        Column(modifier = Modifier.fillMaxSize().background(Color.White, RoundedCornerShape(16.dp))) {
            Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(product?.image_url ?: ""),
                    contentDescription = product?.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(16.dp).padding(top = 24.dp).background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) { Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = Color.White) }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = product?.name ?: "Không có tên", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "$basePrice VNĐ", color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Thành phần", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product?.description ?: "Đang cập nhật...", color = Color.DarkGray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE0E0E0))
                        .clickable(enabled = quantity > 1) { quantity-- }, contentAlignment = Alignment.Center) {
                        Text("-", fontSize = 24.sp, color = if (quantity > 1) Color.Black else Color.Gray)
                    }
                    Text(text = "$quantity", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp), textAlign = TextAlign.Center)
                    Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(primaryOrange)
                        .clickable { quantity++ }, contentAlignment = Alignment.Center) {
                        Text("+", fontSize = 24.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Tổng cộng", fontSize = 16.sp, color = Color.Gray)
                    Text("$totalPrice VNĐ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primaryOrange)
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (showMessage.isNotEmpty()) {
                    Text(text = showMessage, color = if(showMessage.contains("Lỗi")) Color.Red else Color(0xFF4CAF50), fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                }

                Button(
                    onClick = {
                        if (quantity > 0 && product != null) {
                            coroutineScope.launch {
                                try {
                                    // ĐÃ FIX: Truyền đúng productId vào CSDL
                                    val requestData = mapOf("user_id" to com.example.foodapp.model.UserSession.userId.toString(), "product_id" to product!!.id.toString(), "quantity" to quantity.toString())
                                    val response = RetrofitClient.apiService.addToCart(requestData)
                                    showMessage = "Thêm thành công!"
                                    delay(1000)
                                    navController.popBackStack()
                                } catch (e: Exception) { showMessage = "Lỗi kết nối!" }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (quantity > 0) primaryOrange else Color(0xFFE0E0E0)),
                    shape = RoundedCornerShape(8.dp), enabled = quantity > 0
                ) { Text("THÊM VÀO GIỎ HÀNG", fontWeight = FontWeight.Bold, color = Color.White) }
            }
        }
    }
}