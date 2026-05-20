package com.example.foodapp.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.foodapp.network.RetrofitClient
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Home
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Đơn Hàng", "Thực Đơn", "Tôi")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = com.example.foodapp.model.UserSession.restaurantName.ifEmpty { "Quản lý nhà hàng" }, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFF5F5F5))) {
            TabRow(selectedTabIndex = selectedTab, containerColor = Color.White, contentColor = primaryOrange) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            // Điều hướng Tab chính
            when (selectedTab) {
                0 -> AdminOrderTab(primaryOrange)
                1 -> AdminMenuTab(primaryOrange)
                2 -> AdminProfileTab(primaryOrange, navController)
            }
        }
    }
}

// ========================================================
// 1. PHÂN HỆ ĐƠN HÀNG (HIỆN TẠI & LỊCH SỬ)
// ========================================================
@Composable
fun AdminOrderTab(themeColor: Color) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    val subTabs = listOf("Hiện tại", "Lịch sử")

    val coroutineScope = rememberCoroutineScope()
    var ordersList by remember { mutableStateOf<List<Map<String, String>>>(emptyList()) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(refreshTrigger) {
        coroutineScope.launch {
            try {
                ordersList = RetrofitClient.apiService.adminGetAllOrders()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = Color(0xFFEEEEEE),
            contentColor = themeColor,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                    color = themeColor
                )
            }
        ) {
            subTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSubTab == index,
                    onClick = { selectedSubTab = index },
                    text = { Text(title, fontWeight = FontWeight.Medium, color = if (selectedSubTab == index) themeColor else Color.Gray) }
                )
            }
        }

        when (selectedSubTab) {
            0 -> CurrentOrdersScreen(ordersList, themeColor) { refreshTrigger++ }
            1 -> HistoryOrdersScreen(ordersList, themeColor)
        }
    }
}

@Composable
fun CurrentOrdersScreen(ordersList: List<Map<String, String>>, themeColor: Color, onRefresh: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val currentOrders = ordersList.filter { it["status"] != "Completed" && it["status"] != "Hoàn thành" }

    if (currentOrders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không có đơn hàng nào cần xử lý", color = Color.Gray)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(currentOrders.size) { index ->
                val order = currentOrders[index]
                var expanded by remember { mutableStateOf(false) }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Đơn hàng #${order["id"]}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(order["status"] ?: "Pending", color = themeColor, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Khách hàng: ${order["email"] ?: "Không rõ"}", color = Color.Gray, fontSize = 14.sp)
                        Text("Tổng tiền: ${order["total_price"]} VNĐ", fontWeight = FontWeight.Medium, fontSize = 15.sp)

                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.padding(top = 12.dp).fillMaxWidth()) {
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Địa chỉ: ${order["address"] ?: "Không có thông tin"}", fontSize = 14.sp)
                                Text("SĐT: ${order["phone"] ?: "Không có thông tin"}", fontSize = 14.sp)
                                Text("Thanh toán: ${order["payment_method"] ?: "Tiền mặt"}", fontSize = 14.sp)

                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            try {
                                                val response = RetrofitClient.apiService.adminUpdateOrderStatus(mapOf("id" to order["id"]!!, "status" to "Completed"))
                                                if(response.success) {
                                                    Toast.makeText(context, "Đã hoàn thành đơn hàng!", Toast.LENGTH_SHORT).show()
                                                    onRefresh()
                                                }
                                            } catch (e: Exception) { Toast.makeText(context, "Lỗi mạng", Toast.LENGTH_SHORT).show() }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("XÁC NHẬN HOÀN THÀNH", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryOrdersScreen(ordersList: List<Map<String, String>>, themeColor: Color) {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val selectedDateString = dateFormatter.format(Date(selectedDateMillis))
    val displayDateString = displayFormatter.format(Date(selectedDateMillis))

    val historyOrders = ordersList.filter {
        (it["status"] == "Completed" || it["status"] == "Hoàn thành") &&
                (it["created_at"]?.startsWith(selectedDateString) == true)
    }

    val totalOrdersCompleted = historyOrders.size
    val totalRevenue = historyOrders.sumOf { it["total_price"]?.toDoubleOrNull() ?: 0.0 }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.DateRange, contentDescription = "Chọn ngày", tint = themeColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ngày: $displayDateString", color = Color.DarkGray, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = themeColor)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Đơn hoàn tất", color = Color.White, fontSize = 14.sp)
                    Text("$totalOrdersCompleted", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }
            }
            Card(modifier = Modifier.weight(1.5f), colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Doanh thu", color = Color.White, fontSize = 14.sp)
                    Text(String.format("%,.0f VNĐ", totalRevenue), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(16.dp))

        if (historyOrders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text("Không có đơn hàng nào hoàn thành trong ngày này.", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(historyOrders.size) { index ->
                    val order = historyOrders[index]
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Đơn #${order["id"]}", fontWeight = FontWeight.Bold)
                                Text("${order["total_price"]} VNĐ", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                            }
                            Text("Thời gian: ${order["created_at"] ?: ""}", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedDateMillis = it }
                    showDatePicker = false
                }) { Text("Chọn", color = themeColor) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Hủy", color = Color.Gray) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// ========================================================
// 2. PHÂN HỆ QUẢN LÝ THỰC ĐƠN (ON/OFF & THIẾT LẬP CRUD)
// ========================================================
@Composable
fun AdminMenuTab(themeColor: Color) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    val subTabs = listOf("Trạng thái món", "Thiết lập thực đơn")

    val coroutineScope = rememberCoroutineScope()
    var productsList by remember { mutableStateOf<List<Map<String, String>>>(emptyList()) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(refreshTrigger) {
        coroutineScope.launch {
            try {
                val eateryId = com.example.foodapp.model.UserSession.restaurantId.toString().toIntOrNull() ?: 1
                productsList = RetrofitClient.apiService.adminGetAllProducts(eateryId)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = Color(0xFFEEEEEE),
            contentColor = themeColor,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                    color = themeColor
                )
            }
        ) {
            subTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSubTab == index,
                    onClick = { selectedSubTab = index },
                    text = { Text(title, fontWeight = FontWeight.Medium, color = if (selectedSubTab == index) themeColor else Color.Gray) }
                )
            }
        }

        when (selectedSubTab) {
            0 -> MenuToggleScreen(productsList, themeColor) { refreshTrigger++ }
            1 -> MenuSettingsScreen(productsList, themeColor) { refreshTrigger++ }
        }
    }
}

@Composable
fun MenuToggleScreen(productsList: List<Map<String, String>>, themeColor: Color, onRefresh: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    if (productsList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Chưa có món ăn nào", color = Color.Gray) }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(productsList.size) { index ->
                val product = productsList[index]
                val isAvailable = product["is_available"] == "1"
                var switchState by remember { mutableStateOf(isAvailable) }

                Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(product["name"] ?: "", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("${product["price"]} VNĐ", color = themeColor, fontWeight = FontWeight.Medium)
                            Text(if (switchState) "Đang bán" else "Đã hết hàng", color = if (switchState) Color(0xFF4CAF50) else Color.Red, fontSize = 12.sp)
                        }

                        Switch(
                            checked = switchState,
                            onCheckedChange = { isChecked ->
                                switchState = isChecked
                                coroutineScope.launch {
                                    try {
                                        val newStatus = if (isChecked) "1" else "0"
                                        val productId = product["id"] ?: "0"

                                        val request = mapOf(
                                            "id" to productId,
                                            "is_available" to newStatus
                                        )
                                        RetrofitClient.apiService.adminToggleProductStatus(request)
                                        onRefresh()
                                    } catch (e: Exception) {
                                        switchState = !isChecked
                                        e.printStackTrace()
                                    }
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = themeColor,
                                checkedTrackColor = themeColor.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuSettingsScreen(productsList: List<Map<String, String>>, themeColor: Color, onRefresh: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var currentProductId by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (isEditMode) "Chỉnh sửa món ăn" else "Thêm món ăn mới", fontWeight = FontWeight.Bold, color = themeColor) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên món") })
                    OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Giá (VNĐ)") })
                    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Miêu tả") })
                    OutlinedTextField(value = image, onValueChange = { image = it }, label = { Text("Link ảnh (URL)") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val request = mutableMapOf(
                                "name" to name,
                                "price" to price,
                                "description" to desc,
                                "image_url" to image
                            )

                            val response = if (isEditMode) {
                                request["id"] = currentProductId
                                RetrofitClient.apiService.adminUpdateProduct(request)
                            } else {
                                request["eatery_id"] = com.example.foodapp.model.UserSession.restaurantId.toString()
                                RetrofitClient.apiService.adminAddProduct(request)
                            }

                            if (response.success) {
                                Toast.makeText(context, "Thành công!", Toast.LENGTH_SHORT).show()
                                showDialog = false
                                onRefresh()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = themeColor)
                ) { Text("Xác nhận") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Hủy", color = Color.Gray) }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = {
                isEditMode = false; name = ""; price = ""; desc = ""; image = ""
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = themeColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("+ THÊM MÓN VÀO THỰC ĐƠN", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(productsList.size) { index ->
                val product = productsList[index]
                Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(product["name"] ?: "", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("${product["price"]} VNĐ", color = themeColor, fontWeight = FontWeight.Medium)
                        }

                        IconButton(onClick = {
                            isEditMode = true
                            currentProductId = product["id"] ?: ""
                            name = product["name"] ?: ""
                            price = product["price"] ?: ""
                            desc = product["description"] ?: ""
                            image = product["image_url"] ?: ""
                            showDialog = true
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Sửa", tint = Color(0xFF2196F3))
                        }

                        IconButton(onClick = {
                            coroutineScope.launch {
                                val response = RetrofitClient.apiService.adminDeleteProduct(product["id"]?.toInt() ?: 0)
                                if (response.success) {
                                    Toast.makeText(context, "Đã xóa!", Toast.LENGTH_SHORT).show()
                                    onRefresh()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

// ========================================================
// 3. PHÂN HỆ TÔI (HỒ SƠ / CÀI ĐẶT NHÀ HÀNG)
// ========================================================
@Composable
fun AdminProfileTab(themeColor: Color, navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val restaurantId = com.example.foodapp.model.UserSession.restaurantId

    var resName by remember { mutableStateOf("") }
    var resAddress by remember { mutableStateOf("") }
    var resPhone by remember { mutableStateOf("") }
    var resAvatar by remember { mutableStateOf("") } // Không khởi tạo cứng URL nữa
    var isOpen by remember { mutableStateOf(true) }

    var isSettingExpanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDetailDialog by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(refreshTrigger) {
        coroutineScope.launch {
            try {
                // Gọi API lấy profile theo đúng ID của quán đang đăng nhập
                val response = RetrofitClient.apiService.adminGetProfile(restaurantId)

                if (response["success"] == true) {
                    // Ép kiểu cụm dữ liệu "data" thành một Map để lấy từng trường
                    val data = response["data"] as? Map<*, *>
                    if (data != null) {
                        resName = data["name"]?.toString() ?: ""
                        resAddress = data["address"]?.toString() ?: ""
                        resPhone = data["phone"]?.toString() ?: ""
                        resAvatar = data["avatar_url"]?.toString() ?: ""

                        // Cập nhật luôn trạng thái đóng/mở cửa từ DB (1 là true, 0 là false)
                        val isOpenStatus = data["is_open"]?.toString() ?: "1"
                        isOpen = isOpenStatus == "1"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ĐÃ THÊM: verticalScroll để giải quyết vấn đề không cuộn được
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- KHỐI ĐẦU GIAO DIỆN: ẢNH VÀ TÊN NHÀ HÀNG ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logic xử lý Avatar
                if (resAvatar.isNotBlank()) {
                    androidx.compose.foundation.Image(
                        painter = rememberAsyncImagePainter(resAvatar),
                        contentDescription = "Avatar nhà hàng",
                        modifier = Modifier.size(90.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Trạng thái trống (Default Avatar)
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "No Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(45.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = resName, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { showDetailDialog = true }) {
                    Text("Xem chi tiết", color = themeColor, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- CHỮ CÀI ĐẶT NHỎ ---
        Text(
            text = "Cài đặt",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 8.dp)
        )

        // --- MỤC TÌNH TRẠNG QUÁN (ON/OFF) ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Tình trạng quán", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        text = if (isOpen) "Đang mở cửa" else "Đang đóng cửa",
                        color = if (isOpen) Color(0xFF4CAF50) else Color.Red,
                        fontSize = 13.sp
                    )
                }
                Switch(
                    checked = isOpen,
                    onCheckedChange = { isChecked ->
                        isOpen = isChecked
                        coroutineScope.launch {
                            try {
                                val request = mapOf("id" to restaurantId.toString(), "is_open" to if (isChecked) "1" else "0")
                                RetrofitClient.apiService.adminToggleOpenStatus(request)
                            } catch (e: Exception) { e.printStackTrace() }
                        }
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = themeColor, checkedTrackColor = themeColor.copy(alpha = 0.5f))
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- MỤC CÀI ĐẶT CỬA HÀNG (MỞ RỘNG) ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { isSettingExpanded = !isSettingExpanded }.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Cài đặt cửa hàng", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Icon(
                        imageVector = if (isSettingExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand"
                    )
                }

                AnimatedVisibility(visible = isSettingExpanded) {
                    Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFFAFAFA)).padding(horizontal = 16.dp)) {
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

                        Text(
                            text = "Chỉnh sửa thông tin",
                            modifier = Modifier.fillMaxWidth().clickable { showEditDialog = true }.padding(vertical = 14.dp),
                            color = Color.DarkGray,
                            fontSize = 15.sp
                        )
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

                        Text(
                            text = "Xóa tài khoản quán",
                            modifier = Modifier.fillMaxWidth().clickable {
                                coroutineScope.launch {
                                    try {
                                        val response = RetrofitClient.apiService.adminDeleteRestaurantAccount(restaurantId)
                                        if (response.success) {
                                            Toast.makeText(context, "Đã xóa tài khoản!", Toast.LENGTH_SHORT).show()
                                            navController.navigate("login") { popUpTo(0) }
                                        }
                                    } catch (e: Exception) { e.printStackTrace() }
                                }
                            }.padding(vertical = 14.dp),
                            color = Color.Red,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // ĐÃ XÓA Spacer(weight(1f)) làm sập layout khi cuộn, thay bằng Spacer tĩnh.
        Spacer(modifier = Modifier.height(40.dp))

        // --- NÚT ĐĂNG XUẤT ---
        Button(
            onClick = { navController.navigate("login") { popUpTo(0) } },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ĐĂNG XUẤT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showDetailDialog) {
        AlertDialog(
            onDismissRequest = { showDetailDialog = false },
            title = { Text("Thông tin nhà hàng", fontWeight = FontWeight.Bold, color = themeColor) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Tên quán: $resName", fontWeight = FontWeight.Medium)
                    Text("Địa chỉ: $resAddress")
                    Text("Số điện thoại: $resPhone")
                }
            },
            confirmButton = {
                TextButton(onClick = { showDetailDialog = false }) { Text("Đóng", color = themeColor) }
            }
        )
    }

    if (showEditDialog) {
        var editName by remember { mutableStateOf(resName) }
        var editAddress by remember { mutableStateOf(resAddress) }
        var editPhone by remember { mutableStateOf(resPhone) }
        var editAvatar by remember { mutableStateOf(resAvatar) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Sửa thông tin quán", fontWeight = FontWeight.Bold, color = themeColor) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Tên quán") })
                    OutlinedTextField(value = editAddress, onValueChange = { editAddress = it }, label = { Text("Địa chỉ") })
                    OutlinedTextField(value = editPhone, onValueChange = { editPhone = it }, label = { Text("Số điện thoại") })
                    OutlinedTextField(value = editAvatar, onValueChange = { editAvatar = it }, label = { Text("Link ảnh quán (URL)") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val request = mapOf(
                                    "id" to restaurantId.toString(),
                                    "name" to editName,
                                    "address" to editAddress,
                                    "phone" to editPhone,
                                    "avatar_url" to editAvatar
                                )
                                val response = RetrofitClient.apiService.adminUpdateProfileInfo(request)
                                if (response.success) {
                                    Toast.makeText(context, "Đã cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show()
                                    showEditDialog = false
                                    refreshTrigger++
                                }
                            } catch (e: Exception) { e.printStackTrace() }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = themeColor)
                ) { Text("Lưu thay đổi") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Hủy", color = Color.Gray) }
            }
        )
    }
}