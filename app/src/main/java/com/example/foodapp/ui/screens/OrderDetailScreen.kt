package com.example.foodapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.example.foodapp.network.RetrofitClient

@Composable
fun OrderDetailScreen(navController: NavController, orderId: String) {
    val primaryOrange = Color(0xFFFF6D3F)
    val successGreen = Color(0xFF4CAF50)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var orderInfo by remember { mutableStateOf<Map<String, Any>?>(null) }
    var itemsList by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(orderId) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.apiService.getOrderDetails(orderId.toInt())
                if (response["success"] == true) {
                    orderInfo = response["order"] as? Map<String, Any>
                    itemsList = (response["items"] as? List<Map<String, Any>>) ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Lỗi mạng: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF12151C)) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryOrange)
            }
        } else if (orderInfo == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Không tải được chi tiết đơn hàng", color = Color.White)
            }
        } else {
            val status = orderInfo!!["status"]?.toString() ?: "Pending"
            val statusColor = when (status) {
                "Completed" -> successGreen
                "Cancelled" -> Color.Red
                "Shipping" -> Color(0xFF2196F3)
                else -> primaryOrange
            }

            Column(modifier = Modifier.fillMaxSize().padding(16.dp).padding(top = 40.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        item {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Mã đơn hàng", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                                Text("#$orderId", color = Color.Gray, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Trạng thái", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                                Text(status, color = statusColor, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Thời gian đặt", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                                Text(orderInfo!!["created_at"]?.toString() ?: "", color = Color.Gray, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            Text("Giao đến", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(orderInfo!!["address"]?.toString() ?: "", color = Color.Gray, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            Text("Liên hệ", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("SĐT: ${orderInfo!!["phone"]?.toString() ?: ""}", color = Color.Gray, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            Text("Tóm tắt món ăn", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        items(itemsList.size) { index ->
                            val product = itemsList[index]
                            val qty = (product["quantity"] as? Double)?.toInt() ?: product["quantity"].toString()

                            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = product["image_url"]?.toString() ?: "",
                                    contentDescription = "Hình món",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = product["name"]?.toString() ?: "", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(text = "x $qty", color = Color.Gray, fontSize = 14.sp)
                                }
                                Text(text = "${product["price"]} VND", color = primaryOrange, fontWeight = FontWeight.Bold)
                            }
                        }

                        item {
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Tổng cộng", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text("${orderInfo!!["total_price"]} VND", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryOrange)
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nút Đóng
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("ĐÓNG", fontWeight = FontWeight.Bold, color = Color.White)
                }

                // Nút Hủy Đơn Hàng (Chỉ hiện ra khi quán chưa xác nhận làm món)
                if (status == "Pending") {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val request = mapOf("order_id" to orderId)
                                    val response = RetrofitClient.apiService.cancelOrder(request)
                                    if (response.success) {
                                        Toast.makeText(context, "Đã hủy đơn hàng thành công!", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Lỗi mạng", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("HỦY ĐƠN HÀNG", fontWeight = FontWeight.Bold)
                    }
                }

                // Nút Đã Nhận Hàng (Chỉ hiện ra khi quán đã chuyển trạng thái sang Shipping)
                if (status == "Shipping") {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val request = mapOf("order_id" to orderId, "status" to "Completed")
                                    val response = RetrofitClient.apiService.updateOrderStatus(request)
                                    if (response.success) {
                                        Toast.makeText(context, "Cảm ơn bạn đã xác nhận!", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Lỗi mạng", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = successGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("ĐÃ NHẬN ĐƯỢC HÀNG", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}