package com.example.foodapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.foodapp.network.RetrofitClient
import com.example.foodapp.model.Category // Đã thêm import Model Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {
    // Biến quản lý trạng thái Tab vai trò: 0 là Khách hàng, 1 là Nhà hàng
    var selectedRole by remember { mutableIntStateOf(0) }

    // Các trường dữ liệu chung và riêng
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // ========================================================
    // ĐÃ SỬA: BIẾN QUẢN LÝ DANH MỤC ĐỘNG TỪ API
    // ========================================================
    var expandedCategory by remember { mutableStateOf(false) }
    var categoryList by remember { mutableStateOf<List<Category>>(emptyList()) }
    var selectedCategoryName by remember { mutableStateOf("Đang tải danh mục...") }
    var selectedCategoryId by remember { mutableStateOf("1") } // Mặc định là 1

    val coroutineScope = rememberCoroutineScope()
    var showMessage by remember { mutableStateOf("") }

    val primaryOrange = Color(0xFFFF6D3F)

    // ========================================================
    // TỰ ĐỘNG GỌI API LẤY DANH SÁCH DANH MỤC KHI VÀO MÀN HÌNH NÀY
    // ========================================================
    LaunchedEffect(Unit) {
        try {
            val fetchedCategories = RetrofitClient.apiService.getCategories()
            if (fetchedCategories.isNotEmpty()) {
                categoryList = fetchedCategories
                // Hiển thị sẵn danh mục đầu tiên tìm thấy
                selectedCategoryName = fetchedCategories[0].name
                selectedCategoryId = fetchedCategories[0].id
            } else {
                selectedCategoryName = "Chưa có danh mục nào"
            }
        } catch (e: Exception) {
            selectedCategoryName = "Lỗi tải danh mục"
            e.printStackTrace()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF12151C)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clickable { navController.popBackStack() }
            )

            Text("Sign Up", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A5568))
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Already have an account? ", fontSize = 14.sp)
                Text(
                    text = "Sign in!",
                    color = primaryOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // THANH CHUYỂN ĐỔI VAI TRÒ ĐĂNG KÝ
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedRole = 0
                            showMessage = ""
                        }
                        .background(if (selectedRole == 0) Color.White else Color.Transparent, RoundedCornerShape(6.dp))
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Khách hàng", fontWeight = FontWeight.Bold, color = if (selectedRole == 0) primaryOrange else Color.Gray)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedRole = 1
                            showMessage = ""
                        }
                        .background(if (selectedRole == 1) Color.White else Color.Transparent, RoundedCornerShape(6.dp))
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nhà hàng", fontWeight = FontWeight.Bold, color = if (selectedRole == 1) primaryOrange else Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ĐIỀU KIỆN HIỂN THỊ CÁC Ô NHẬP LIỆU THEO VAI TRÒ
            if (selectedRole == 0) {
                // GIAO DIỆN ĐĂNG KÝ CHO KHÁCH HÀNG
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Họ khách hàng") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("Tên khách hàng") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            } else {
                // GIAO DIỆN ĐĂNG KÝ CHO NHÀ HÀNG
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Tên nhà hàng") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Địa chỉ chi tiết") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // ========================================================
                // ĐÃ SỬA: ĐỔ DỮ LIỆU TỰ ĐỘNG VÀO MENU THẢ XUỐNG
                // ========================================================
                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { if (categoryList.isNotEmpty()) expandedCategory = !expandedCategory },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategoryName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Danh mục kinh doanh") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        categoryList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item.name) },
                                onClick = {
                                    selectedCategoryName = item.name
                                    selectedCategoryId = item.id
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Các ô nhập chung của cả 2 vai trò
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email đăng nhập") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mật khẩu") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // NÚT ĐĂNG KÝ - GỌI API THEO VAI TRÒ
            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            if (selectedRole == 0) {
                                // 1. Xử lý luồng Đăng ký Khách hàng
                                if (lastName.isNotEmpty() && firstName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                                    val request = mapOf(
                                        "last_name" to lastName,
                                        "first_name" to firstName,
                                        "email" to email,
                                        "password" to password
                                    )
                                    val response = RetrofitClient.apiService.register(request)
                                    showMessage = response.message ?: ""

                                    if (response.success) {
                                        navController.popBackStack()
                                    }
                                } else {
                                    showMessage = "Vui lòng nhập đầy đủ Họ Tên, Email và Mật khẩu!"
                                }
                            } else {
                                // 2. Xử lý luồng Đăng ký Nhà hàng
                                if (firstName.isNotEmpty() && address.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {

                                    // ========================================================
                                    // ĐÃ SỬA: Lấy thẳng selectedCategoryId truyền lên API
                                    // ========================================================
                                    val request = mapOf(
                                        "restaurant_name" to firstName,
                                        "address" to address,
                                        "email" to email,
                                        "password" to password,
                                        "category_id" to selectedCategoryId.toString()
                                    )
                                    val response = RetrofitClient.apiService.adminRegister(request)
                                    showMessage = response.message ?: ""

                                    if (response.success) {
                                        navController.popBackStack()
                                    }
                                } else {
                                    showMessage = "Vui lòng điền đủ Tên, Địa chỉ, Email và Mật khẩu nhà hàng!"
                                }
                            }
                        } catch (e: Exception) {
                            showMessage = "Lỗi kết nối máy chủ: ${e.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("SIGN UP", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Hiển thị thông báo trạng thái
            if (showMessage.isNotEmpty()) {
                Text(
                    text = showMessage,
                    color = if (showMessage.contains("thành công")) Color(0xFF4CAF50) else Color.Red,
                    modifier = Modifier.padding(top = 12.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}