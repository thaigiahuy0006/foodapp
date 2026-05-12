package com.example.foodapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

// Import Model và API
import com.example.foodapp.model.Order
import com.example.foodapp.network.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()

    // Quản lý trạng thái Tab
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Upcoming", "Ongoing", "History")

    // Ánh xạ tab index sang trạng thái trong Database
    val statusMap = listOf("Pending", "Shipping", "Completed")

    // Biến lưu danh sách đơn hàng
    var orderList by remember { mutableStateOf<List<Order>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // Gọi API mỗi khi selectedTabIndex thay đổi
    LaunchedEffect(selectedTabIndex) {
        coroutineScope.launch {
            isLoading = true
            try {
                // Tạm fix user_id = 1 để test. Trạng thái lấy từ statusMap
                val currentStatus = statusMap[selectedTabIndex]
                orderList = RetrofitClient.apiService.getOrders(userId = 1, status = currentStatus)
            } catch (e: Exception) {
                e.printStackTrace()
                orderList = emptyList() // Lỗi mạng thì hiện rỗng
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            // Thanh Tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = primaryOrange,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = primaryOrange
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTabIndex == index) primaryOrange else Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }

            // Nội dung danh sách đơn hàng
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryOrange)
                }
            } else if (orderList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Không có đơn hàng nào", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(orderList.size) { index ->
                        val order = orderList[index]
                        OrderItemUI(
                            navController = navController,
                            order = order,
                            primaryOrange = primaryOrange
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemUI(navController: NavController, order: Order, primaryOrange: Color) {
    // Đổi màu chữ theo trạng thái
    val statusColor = when (order.status) {
        "Completed" -> Color(0xFF4CAF50) // Xanh lá
        "Cancelled" -> Color.Red
        else -> primaryOrange
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Có thể truyền ID đơn hàng qua route để màn OrderDetail fetch chi tiết
                navController.navigate("order_detail")
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Đơn hàng #${order.id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = order.status, color = statusColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            // Tạm thời hiển thị địa chỉ giao hàng ở đây thay vì liệt kê món
            // (Muốn hiện món cần join bảng order_details)
            Text(text = "Giao đến: ${order.address}", color = Color.DarkGray, fontSize = 14.sp, maxLines = 1)
            Text(text = "SĐT: ${order.phone}", color = Color.DarkGray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = order.created_at, color = Color.Gray, fontSize = 12.sp)
                Text(text = "${order.total_price} VND", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}