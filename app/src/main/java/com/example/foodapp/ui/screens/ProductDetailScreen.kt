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

@Composable
fun ProductDetailScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)

    // --- KHAI BÁO CÁC BIẾN CẦN THIẾT ---
    val coroutineScope = rememberCoroutineScope() // Dùng để chạy hàm suspend (gọi API)
    var showMessage by remember { mutableStateOf("") } // Hiển thị thông báo thành công/lỗi
    var quantity by remember { mutableStateOf(1) } // Số lượng món ăn

    val basePrice = 25000.0
    val totalPrice = basePrice * quantity

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF12151C)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            // Ảnh bìa món ăn và nút Back
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1546173159-315724a31696?q=80&w=800"),
                    contentDescription = "Cơm tấm",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(top = 24.dp)
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Tên món và Giá
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Cơm tấm", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${basePrice} VND", color = Color.Gray, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                // Thành phần (Ingredients)
                Text(text = "Ingredients", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Cơm, Sườn, Bì, Chả", color = Color.DarkGray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Khu vực chọn số lượng và thêm vào giỏ
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Bộ đếm số lượng: [ - ]  1  [ + ]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE0E0E0))
                            .clickable(enabled = quantity > 1) { quantity-- },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("-", fontSize = 24.sp, color = if (quantity > 1) Color.Black else Color.Gray)
                    }

                    Text(
                        text = "$quantity",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(primaryOrange)
                            .clickable { quantity++ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", fontSize = 24.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tổng tiền
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", fontSize = 16.sp, color = Color.Gray)
                    Text("${totalPrice} VND", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primaryOrange)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hiển thị thông báo khi có phản hồi từ API
                if (showMessage.isNotEmpty()) {
                    Text(
                        text = showMessage,
                        color = if(showMessage.contains("Lỗi")) Color.Red else Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Nút Thêm vào giỏ hàng
                Button(
                    onClick = {
                        if (quantity > 0) {
                            coroutineScope.launch {
                                try {
                                    val requestData = mapOf(
                                        "user_id" to "1",
                                        "product_id" to "1",
                                        "quantity" to quantity.toString()
                                    )
                                    val response = com.example.foodapp.network.RetrofitClient.apiService.addToCart(requestData)

                                    showMessage = response.message ?: "Thành công"

                                    delay(1000) // Đợi 1 giây để người dùng thấy thông báo
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    showMessage = "Lỗi kết nối!"
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (quantity > 0) primaryOrange else Color(0xFFE0E0E0),
                        contentColor = if (quantity > 0) Color.White else Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = quantity > 0
                ) {
                    Text("ADD TO BAG", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}