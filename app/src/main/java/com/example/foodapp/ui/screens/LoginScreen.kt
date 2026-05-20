package com.example.foodapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Biến lưu trạng thái tab đang chọn: 0 là Khách hàng, 1 là Nhà hàng
    var selectedRole by remember { mutableIntStateOf(0) }

    // Biến dùng để xử lý tiến trình gọi API và hiển thị thông báo
    val coroutineScope = rememberCoroutineScope()
    var showMessage by remember { mutableStateOf("") }

    val primaryOrange = Color(0xFFFF6D3F)
    val googleBlue = Color(0xFF4285F4)

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF12151C)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clickable { navController.popBackStack() }
            )

            Text("Sign In", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A5568))
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Don't have an account? ", fontSize = 14.sp)
                Text(
                    text = "Sign up now!",
                    color = primaryOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("signup")
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // THANH CHUYỂN ĐỔI VAI TRÒ (Tab Chọn Khách hàng / Nhà hàng)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nút Khách Hàng
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedRole = 0 }
                        .background(
                            if (selectedRole == 0) Color.White else Color.Transparent,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Khách hàng",
                        fontWeight = FontWeight.Bold,
                        color = if (selectedRole == 0) primaryOrange else Color.Gray
                    )
                }

                // Nút Nhà Hàng
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedRole = 1 }
                        .background(
                            if (selectedRole == 1) Color.White else Color.Transparent,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nhà hàng",
                        fontWeight = FontWeight.Bold,
                        color = if (selectedRole == 1) primaryOrange else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(if (selectedRole == 0) "Email khách hàng" else "Email nhà hàng") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Forgot Password?",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { navController.navigate("forgot_password") },
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nút Đăng nhập có gọi API
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        coroutineScope.launch {
                            try {
                                val requestData = mapOf("email" to email, "password" to password)

                                // KIỂM TRA ĐANG Ở TAB NÀO ĐỂ GỌI API TƯƠNG ỨNG
                                if (selectedRole == 1) {
                                    // 1. LUỒNG NHÀ HÀNG (ADMIN)
                                    val response = com.example.foodapp.network.RetrofitClient.apiService.adminLogin(requestData)
                                    showMessage = response.message ?: ""

                                    if (response.success) {
                                        com.example.foodapp.model.UserSession.restaurantId = response.restaurant_id ?: 1
                                        com.example.foodapp.model.UserSession.restaurantName = response.restaurant_name ?: "Nhà Hàng"
                                        // Đăng nhập Nhà hàng thành công, chuyển hướng vào Dashboard
                                        navController.navigate("admin_dashboard") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                } else {
                                    // 2. LUỒNG KHÁCH HÀNG (USER BÌNH THƯỜNG)
                                    val response = com.example.foodapp.network.RetrofitClient.apiService.login(requestData)
                                    showMessage = response.message ?: ""

                                    if (response.success) {
                                        com.example.foodapp.model.UserSession.userId = response.user_id ?: 1
                                        // Đăng nhập thành công, chuyển hướng vào Home
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                showMessage = "Lỗi kết nối: ${e.message}"
                            }
                        }
                    } else {
                        showMessage = "Vui lòng nhập email và mật khẩu!"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("SIGN IN", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Hiển thị thông báo
            if (showMessage.isNotEmpty()) {
                Text(
                    text = showMessage,
                    color = if (showMessage.contains("thành công") || showMessage.contains("Chào mừng")) Color(0xFF4CAF50) else Color.Red,
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = 14.sp
                )
            }

            // Ẩn nút đăng nhập bằng Google nếu đang ở tab Nhà hàng (Vì Nhà hàng cấp tài khoản riêng)
            if (selectedRole == 0) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "OR", modifier = Modifier.align(Alignment.CenterHorizontally), color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* Handle Google Login */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = googleBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("CONTINUE WITH GOOGLE", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}