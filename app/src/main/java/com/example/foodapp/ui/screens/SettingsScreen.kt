package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
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
import com.example.foodapp.network.RetrofitClient

@Composable
fun SettingsScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.apiService.getProfile(1)
                if (response.success) { userProfile = response.user }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") }, selected = false, onClick = { navController.navigate("home") })
                NavigationBarItem(icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Giỏ hàng") }, selected = false, onClick = { navController.navigate("bag") })
                NavigationBarItem(icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Đã lưu") }, selected = false, onClick = { navController.navigate("saved") })
                NavigationBarItem(icon = { Icon(Icons.Default.Person, contentDescription = "Hồ sơ") }, selected = true, onClick = { }, colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryOrange, indicatorColor = Color.White))
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(paddingValues)) {
            Box(modifier = Modifier.fillMaxWidth().height(220.dp)) {
                Image(painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1557683316-973673baf926?q=80&w=800"), contentDescription = "Background", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    // FIX: Hiển thị avatar thực từ database
                    val avatarPath = if (!userProfile?.avatar_url.isNullOrEmpty()) "${userProfile?.avatar_url}?t=${System.currentTimeMillis()}"
                    else "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=150"
                    Image(
                        painter = rememberAsyncImagePainter(model = avatarPath),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(80.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "${userProfile?.last_name ?: ""} ${userProfile?.first_name ?: ""}".trim(), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                }
            }
            Column(modifier = Modifier.background(Color.White)) {
                SettingsMenuItem(icon = Icons.Default.Person, title = "Hồ sơ & Địa chỉ", onClick = { navController.navigate("profile") })
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                SettingsMenuItem(icon = Icons.Default.Info, title = "Về chúng tôi", onClick = { navController.navigate("about_us") })
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                SettingsMenuItem(icon = Icons.AutoMirrored.Filled.ExitToApp, title = "Đăng xuất", titleColor = primaryOrange, iconColor = primaryOrange, showArrow = false, onClick = { navController.navigate("login") { popUpTo(0) } })
            }
        }
    }
}
// ... (Hàm SettingsMenuItem giữ nguyên) ...
@Composable
fun SettingsMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, titleColor: Color = Color.DarkGray, iconColor: Color = Color.DarkGray, showArrow: Boolean = true, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = title, tint = iconColor)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = titleColor, modifier = Modifier.weight(1f))
        if (showArrow) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Go", tint = Color(0xFFFF6D3F)) }
    }
}