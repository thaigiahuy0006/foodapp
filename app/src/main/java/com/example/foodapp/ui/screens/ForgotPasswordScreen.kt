package com.example.foodapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController // Đã thêm Import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    val primaryOrange = Color(0xFFFF6D3F)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF12151C)
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
                text = "Forgot Password",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A5568)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter your email to reset password",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Enter your email here") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Giả lập gửi email xong quay lại trang đăng nhập
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("RESET PASSWORD", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}