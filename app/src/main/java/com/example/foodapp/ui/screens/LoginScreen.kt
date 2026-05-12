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
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                    .padding(bottom = 24.dp)
                    .clickable { navController.popBackStack() }
            )

            Text("Sign In", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A5568))
            Spacer(modifier = Modifier.height(16.dp))

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
                    if(email.isNotEmpty() && password.isNotEmpty()) {
                        coroutineScope.launch {
                            try {
                                // Gửi request lên API login.php
                                val requestData = mapOf("email" to email, "password" to password)
                                val response = com.example.foodapp.network.RetrofitClient.apiService.login(requestData)

                                showMessage = response.message
                                if(response.success) {
                                    // Đăng nhập thành công, chuyển hướng vào Home
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            } catch(e: Exception) {
                                showMessage = "Lỗi kết nối: ${e.message}"
                            }
                        }
                    } else {
                        showMessage = "Vui lòng nhập email và mật khẩu!"
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("SIGN IN", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Hiển thị thông báo
            if (showMessage.isNotEmpty()) {
                Text(
                    text = showMessage,
                    color = if (showMessage.contains("thành công")) Color(0xFF4CAF50) else Color.Red,
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "OR", modifier = Modifier.align(Alignment.CenterHorizontally), color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Handle Google Login */ },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = googleBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("CONTINUE WITH GOOGLE", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}