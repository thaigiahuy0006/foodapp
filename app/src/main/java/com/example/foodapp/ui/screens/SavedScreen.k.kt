package com.example.foodapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
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
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

import com.example.foodapp.model.Eatery
import com.example.foodapp.model.UserSession
import com.example.foodapp.network.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Biến lưu danh sách thật
    var savedList by remember { mutableStateOf<List<Eatery>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Gọi API lấy dữ liệu khi vừa mở màn hình
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.apiService.getSavedEateries(UserSession.userId)
                if (response.success) {
                    savedList = response.saved_eateries ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Eateries", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = primaryOrange,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (savedList.isEmpty()) {
                Text(
                    "Bạn chưa lưu nhà hàng nào!",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(savedList.size) { index ->
                        val eatery = savedList[index]
                        SavedEateryItem(
                            eatery = eatery,
                            navController = navController,
                            primaryOrange = primaryOrange,
                            onRemove = {
                                // XỬ LÝ KHI BẤM BỎ LƯU (CÓ TRY-CATCH CHỐNG CRASH)
                                coroutineScope.launch {
                                    try {
                                        val request = mapOf(
                                            "user_id" to UserSession.userId.toString(),
                                            "restaurant_id" to eatery.id.toString()
                                        )
                                        val response = RetrofitClient.apiService.toggleSavedEatery(request)

                                        if (response.success) {
                                            Toast.makeText(context, "Đã bỏ lưu!", Toast.LENGTH_SHORT).show()
                                            // Xóa trực tiếp khỏi danh sách trên màn hình
                                            savedList = savedList.filter { it.id != eatery.id }
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Lỗi mạng!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SavedEateryItem(
    eatery: Eatery,
    navController: NavController,
    primaryOrange: Color,
    onRemove: () -> Unit // Hàm callback khi bấm xóa
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            // Chuyển hướng sang màn chi tiết với ID nhà hàng thật
            .clickable { navController.navigate("eatery_detail/${eatery.id}") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(eatery.image_url ?: "https://images.unsplash.com/photo-1544025162-848f657a80fa?q=80&w=200"),
                contentDescription = eatery.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = eatery.name ?: "Tên nhà hàng", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = eatery.distance ?: "1.0 km", color = Color.Gray, fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Text(text = " ${eatery.rating ?: 0.0}", color = Color.Gray, fontSize = 14.sp)
                }
            }

            // Icon thả tim được bọc trong IconButton
            IconButton(onClick = { onRemove() }) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Bỏ lưu",
                    tint = primaryOrange,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}