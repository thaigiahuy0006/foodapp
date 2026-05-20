package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.foodapp.network.RetrofitClient
import com.example.foodapp.model.CartItem
import com.example.foodapp.model.UserSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()
    var showMessage by remember { mutableStateOf("") }

    // State cho việc nhập liệu địa chỉ và SĐT
    var address by remember { mutableStateOf("Ký túc xá VKU, Đà Nẵng") }
    var phone by remember { mutableStateOf("0901234567") }

    // ---- MỚI: State cho phương thức thanh toán ----
    var selectedPaymentMethod by remember { mutableStateOf("Tiền mặt (COD)") }
    val paymentOptions = listOf("Tiền mặt (COD)", "Ví điện tử MoMo", "Thẻ Ngân Hàng")

    // State cho giỏ hàng lấy từ DB
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var subtotal by remember { mutableStateOf(0.0) }
    val deliveryFee = 15000.0
    val total = subtotal + if(cartItems.isNotEmpty()) deliveryFee else 0.0

    LaunchedEffect(Unit) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông tin đặt hàng", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(paddingValues).padding(16.dp)
        ) {
            // ---- KHỐI 1: THÔNG TIN GIAO HÀNG ----
            item {
                Text("Giao đến", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text("Số điện thoại liên hệ", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ---- KHỐI 2: PHƯƠNG THỨC THANH TOÁN (MỚI THÊM) ----
            item {
                Text("Phương thức thanh toán", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        paymentOptions.forEachIndexed { index, method ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedPaymentMethod = method }
                                    .padding(vertical = 8.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Đổi icon tương ứng với từng phương thức
                                val icon = when(method) {
                                    "Tiền mặt (COD)" -> Icons.Default.ShoppingCart
                                    "Ví điện tử MoMo" -> Icons.Default.Phone
                                    else -> Icons.Default.CheckCircle
                                }
                                Icon(imageVector = icon, contentDescription = method, tint = primaryOrange, modifier = Modifier.size(24.dp))

                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = method, fontSize = 16.sp, modifier = Modifier.weight(1f))

                                RadioButton(
                                    selected = (selectedPaymentMethod == method),
                                    onClick = { selectedPaymentMethod = method },
                                    colors = RadioButtonDefaults.colors(selectedColor = primaryOrange)
                                )
                            }
                            if (index < paymentOptions.size - 1) {
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ---- KHỐI 3: TÓM TẮT ĐƠN HÀNG ----
            item {
                Text("Tóm tắt đơn hàng", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(cartItems.size) { index ->
                val item = cartItems[index]
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(item.image_url),
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "Số lượng: ${item.quantity}", color = Color.Gray, fontSize = 14.sp)
                    }
                    Text(text = "${item.price * item.quantity} VNĐ", color = primaryOrange, fontWeight = FontWeight.Bold)
                }
            }

            // ---- KHỐI 4: TỔNG TIỀN VÀ NÚT ĐẶT HÀNG ----
            item {
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Tổng thanh toán", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("$total VNĐ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryOrange)
                }

                if (showMessage.isNotEmpty()) { Text(text = showMessage, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp)) }
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                // ---- ĐÃ CẬP NHẬT: Thêm payment_method vào request ----
                                val requestData = mapOf(
                                    "user_id" to UserSession.userId.toString(),
                                    "total_price" to total.toString(),
                                    "address" to address,
                                    "phone" to phone,
                                    "payment_method" to selectedPaymentMethod
                                )
                                val response = RetrofitClient.apiService.placeOrder(requestData)
                                if (response.success) {
                                    // Chuyển hướng sang màn hình lịch sử đặt hàng khi thành công
                                    navController.navigate("order_history") { popUpTo("home") }
                                } else {
                                    showMessage = "Đặt hàng thất bại: ${response.message}"
                                }
                            } catch (e: Exception) {
                                showMessage = "Lỗi kết nối mạng!"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("ĐẶT HÀNG NGAY", fontWeight = FontWeight.Bold, color = Color.White) }
            }
        }
    }
}