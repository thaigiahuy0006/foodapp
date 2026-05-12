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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch // Dùng cho gọi API

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Biến dùng để xử lý tiến trình gọi API và hiển thị thông báo
    val coroutineScope = rememberCoroutineScope()
    var showMessage by remember { mutableStateOf("") }

    val primaryOrange = Color(0xFFFF6D3F)
    val googleBlue = Color(0xFF4285F4)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF12151C) // Màu nền tối bên ngoài viền ảnh
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            // Nút Back
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .clickable { navController.popBackStack() } // Quay lại màn hình Login
            )

            Text(
                text = "Sign Up",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A5568)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Chữ chuyển sang trang Đăng nhập
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Already have account? ", fontSize = 14.sp)
                Text(
                    text = "Sign in now!",
                    color = primaryOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("login") {
                            popUpTo("signup") { inclusive = true }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
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

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Nút SIGN UP gọi API
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                        coroutineScope.launch {
                            try {
                                val requestData = mapOf("email" to email, "password" to password)
                                val response = com.example.foodapp.network.RetrofitClient.apiService.register(requestData)

                                showMessage = response.message
                                if (response.success) {
                                    // Nếu đăng ký thành công, tự động chuyển về trang đăng nhập
                                    navController.navigate("login") {
                                        popUpTo("signup") { inclusive = true }
                                    }
                                }
                            } catch (e: Exception) {
                                showMessage = "Lỗi kết nối: ${e.message}"
                            }
                        }
                    } else if (password != confirmPassword) {
                        showMessage = "Mật khẩu xác nhận không khớp!"
                    } else {
                        showMessage = "Vui lòng nhập đầy đủ thông tin!"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("SIGN UP", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Hiển thị thông báo lỗi hoặc thành công
            if (showMessage.isNotEmpty()) {
                Text(
                    text = showMessage,
                    color = if (showMessage.contains("thành công")) Color(0xFF4CAF50) else Color.Red,
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "OR",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Handle Google Sign Up */ },
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