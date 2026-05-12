package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
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

import com.example.foodapp.model.Product
import com.example.foodapp.model.Eatery
import com.example.foodapp.network.RetrofitClient

@Composable
fun EateryDetailScreen(navController: NavController, eateryId: String = "1") {
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()

    // Khai báo các biến lưu trữ dữ liệu tải về
    var eatery by remember { mutableStateOf<Eatery?>(null) }
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Gọi API lấy dữ liệu dựa theo eateryId
    LaunchedEffect(eateryId) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.apiService.getEateryDetails(eateryId)
                if (response.success) {
                    eatery = response.eatery
                    productList = response.products ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // Hiển thị vòng xoay đang tải
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = primaryOrange)
        }
        return
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF12151C)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        ) {
            // ---- ẢNH BÌA & NÚT BACK ----
            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(eatery?.image_url ?: ""),
                    contentDescription = eatery?.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(16.dp).padding(top = 24.dp).background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = Color.White)
                }
            }

            // ---- THÔNG TIN QUÁN & MENU ----
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = eatery?.name ?: "Đang tải...", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    // Nút Info chuyển sang màn thông tin chi tiết
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Thông tin",
                        tint = primaryOrange,
                        modifier = Modifier.size(28.dp).clickable { navController.navigate("eatery_info") }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Đánh giá", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Text(text = " ${eatery?.rating ?: 0.0}", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = eatery?.distance ?: "", fontSize = 14.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Thực đơn", fontSize = 18.sp, fontWeight = FontWeight.Bold) // ĐÃ VIỆT HÓA
                Spacer(modifier = Modifier.height(12.dp))

                // ---- HIỂN THỊ DANH SÁCH MÓN ĂN TỪ MYSQL ----
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(productList.size) { index ->
                        val product = productList[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .clickable {
                                    // QUAN TRỌNG: Truyền ID món ăn sang màn hình Chi tiết món!
                                    navController.navigate("product_detail/${product.id}")
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(product.image_url),
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = product.description ?: "", color = Color.Gray, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "${product.price} VNĐ", color = primaryOrange, fontWeight = FontWeight.Bold)
                            }
                        }
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}