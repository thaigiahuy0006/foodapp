package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.foodapp.network.RetrofitClient // Đảm bảo đã import đúng RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()
    var showMessage by remember { mutableStateOf("") }

    // Dữ liệu thanh toán (Bạn có thể truyền các giá trị này qua ViewModel hoặc NavArgs)
    val userId = "1"
    val totalPrice = "145000.0"
    val address = "VRJ6+8HH, Đông Hoà, Dĩ An, Bình Dương, Việt Nam"
    val phone = "035431..."

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Information", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Phần hiển thị địa chỉ giao hàng
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Deliver to", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
                    Text("Change", color = primaryOrange, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(address, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Phần hiển thị thông tin liên hệ
            item {
                Text("Contact", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Phú Quang, $phone", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tiêu đề tóm tắt đơn hàng
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Order summary", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
                    Text("Add more", color = primaryOrange, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Danh sách các món ăn trong đơn (Giả lập để hiển thị UI)
            items(2) { index ->
                val name = if (index == 0) "Nước mía" else "Cơm gà xối mỡ"
                val price = if (index == 0) "20000.0 VND" else "30000.0 VND"
                val image = if (index == 0) "https://images.unsplash.com/photo-1556679343-c7306c1976bc?q=80&w=200" else "https://images.unsplash.com/photo-1604908176997-125f25cc6f3d?q=80&w=200"

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(image),
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "x 2", color = Color.Gray, fontSize = 14.sp)
                    }
                    Text(text = price, color = primaryOrange, fontWeight = FontWeight.Bold)
                }
            }

            // Tổng tiền và nút đặt hàng
            item {
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Total", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("$totalPrice VND", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryOrange)
                }

                if (showMessage.isNotEmpty()) {
                    Text(text = showMessage, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val requestData = mapOf(
                                    "user_id" to userId,
                                    "total_price" to totalPrice,
                                    "address" to address,
                                    "phone" to phone
                                )
                                // Gọi API đặt hàng
                                val response = RetrofitClient.apiService.placeOrder(requestData)

                                if (response.success) {
                                    // Đặt hàng thành công, chuyển đến danh sách đơn hàng
                                    navController.navigate("orders") {
                                        popUpTo("home") // Xóa các trang trung gian khỏi stack
                                    }
                                } else {
                                    showMessage = response.message ?: "Đặt hàng thất bại"
                                }
                            } catch (e: Exception) {
                                showMessage = "Lỗi kết nối mạng!"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("PLACE ORDER", fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}