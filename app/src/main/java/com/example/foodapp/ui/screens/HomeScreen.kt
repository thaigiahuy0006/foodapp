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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
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

// Import các model và API đã tạo
import com.example.foodapp.model.Category
import com.example.foodapp.model.Eatery
import com.example.foodapp.network.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()

    // Biến lưu trữ dữ liệu từ API
    var categoryList by remember { mutableStateOf<List<Category>>(emptyList()) }
    var eateryList by remember { mutableStateOf<List<Eatery>>(emptyList()) }

    // Gọi API lấy dữ liệu khi màn hình khởi tạo
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                categoryList = RetrofitClient.apiService.getCategories()
                eateryList = RetrofitClient.apiService.getPopularEateries()
            } catch (e: Exception) {
                // Xử lý lỗi nếu không gọi được API
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryOrange,
                        selectedTextColor = primaryOrange,
                        indicatorColor = Color.White
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Bag") },
                    selected = false,
                    onClick = { navController.navigate("bag") } // Chuyển sang Giỏ hàng
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite") },
                    selected = false,
                    onClick = { navController.navigate("saved") } // Chuyển sang Quán đã lưu
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    selected = false,
                    onClick = { /* Sẽ cập nhật sau khi có màn Profile */ }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Phú", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "128 Đ. Vành Đai, Đông Hoà, Dĩ An, Bình Dương",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            maxLines = 2
                        )
                    }
                    Image(
                        painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=150"),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp).clip(CircleShape)
                    )
                }
            }

            // Thanh tìm kiếm
            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Search for eateries") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("search") },
                    enabled = false,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = primaryOrange.copy(alpha = 0.5f),
                        disabledPlaceholderColor = Color.Gray,
                        disabledLeadingIconColor = Color.Gray
                    )
                )
            }

            // Banner quảng cáo
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1550547660-d9450f859349?q=80&w=800"),
                    contentDescription = "Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(16.dp))
                )
            }

            // Categories từ MySQL
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Categories", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(categoryList.size) { index ->
                        val cat = categoryList[index]
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { navController.navigate("category_list") }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(cat.icon_url),
                                    contentDescription = cat.name,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = cat.name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Header cho Popular Eateries
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Popular Eateries", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "View all",
                        fontSize = 14.sp,
                        color = primaryOrange,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { navController.navigate("popular_list") }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Danh sách Popular Eateries từ MySQL
            items(eateryList.size) { index ->
                val eatery = eateryList[index]
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(eatery.image_url),
                        contentDescription = eatery.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { navController.navigate("detail") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
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