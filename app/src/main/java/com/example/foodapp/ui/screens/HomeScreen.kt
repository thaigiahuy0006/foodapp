package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.foodapp.model.UserProfile
import com.example.foodapp.model.Category
import com.example.foodapp.model.Eatery
import com.example.foodapp.network.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()

    var categoryList by remember { mutableStateOf<List<Category>>(emptyList()) }
    var eateryList by remember { mutableStateOf<List<Eatery>>(emptyList()) }
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }

    // Gọi API lấy dữ liệu khi màn hình khởi tạo
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                categoryList = RetrofitClient.apiService.getCategories()
                eateryList = RetrofitClient.apiService.getPopularEateries()
                val profileResponse = RetrofitClient.apiService.getProfile(1)
                if (profileResponse.success) {
                    userProfile = profileResponse.user
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
                    label = { Text("Trang chủ") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryOrange, indicatorColor = Color.White)
                )
                NavigationBarItem(icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Giỏ hàng") }, selected = false, onClick = { navController.navigate("bag") })
                NavigationBarItem(icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Đã lưu") }, selected = false, onClick = { navController.navigate("saved") })
                NavigationBarItem(icon = { Icon(Icons.Default.Person, contentDescription = "Hồ sơ") }, selected = false, onClick = { navController.navigate("settings") })
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(paddingValues).padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = userProfile?.first_name ?: "Người dùng", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(text = userProfile?.address ?: "Chưa cập nhật địa chỉ", fontSize = 12.sp, color = Color.Gray, maxLines = 2)
                    }
                    // FIX: Hiển thị avatar thực từ database
                    val avatarPath = if (!userProfile?.avatar_url.isNullOrEmpty()) "${userProfile?.avatar_url}?t=${System.currentTimeMillis()}"
                    else "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=150"
                    Image(
                        painter = rememberAsyncImagePainter(model = avatarPath),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp).clip(CircleShape).clickable { navController.navigate("settings") }
                    )
                }
            }
            // ... (Phần Danh mục và Quán ăn nổi bật giữ nguyên như code cũ của bạn) ...
            item { Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("Tìm kiếm món ăn, quán ăn...") }, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }, modifier = Modifier.fillMaxWidth().clickable { navController.navigate("search") }, enabled = false, shape = RoundedCornerShape(24.dp))
            }
            item { Spacer(modifier = Modifier.height(16.dp))
                Image(painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1550547660-d9450f859349?q=80&w=800"), contentDescription = "Banner", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(16.dp)))
            }
            item { Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Danh mục", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(categoryList.size) { index ->
                        val cat = categoryList[index]
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { navController.navigate("category_list/${cat.id}") }) {
                            Box(modifier = Modifier.size(60.dp).background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                Image(painter = rememberAsyncImagePainter(cat.icon_url), contentDescription = cat.name, modifier = Modifier.size(30.dp))
                            }
                            Text(text = cat.name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Quán ăn nổi bật", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(eateryList.size) { index ->
                val eatery = eateryList[index]
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Image(painter = rememberAsyncImagePainter(eatery.image_url), contentDescription = eatery.name, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(16.dp)).clickable { navController.navigate("detail/${eatery.id}") })
                    Text(text = eatery.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                        Text(text = " ${eatery.rating}", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}