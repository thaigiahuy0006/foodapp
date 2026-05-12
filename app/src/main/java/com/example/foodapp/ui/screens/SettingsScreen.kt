package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@Composable
fun SettingsScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)

    Scaffold(
        bottomBar = {
            // Thanh Bottom Navigation giống HomeScreen
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, contentDescription = "Home") }, selected = false, onClick = { navController.navigate("home") })
                NavigationBarItem(icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Bag") }, selected = false, onClick = { navController.navigate("bag") })
                NavigationBarItem(icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite") }, selected = false, onClick = { navController.navigate("saved") })
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryOrange, indicatorColor = Color.White)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            // Header với background pattern
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1557683316-973673baf926?q=80&w=800"), // Ảnh nền cam gradient
                    contentDescription = "Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Nút chuông thông báo
                IconButton(
                    onClick = { },
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    BadgedBox(badge = { Badge(containerColor = primaryOrange) { Text("0", color = Color.White) } }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = primaryOrange)
                    }
                }

                // Avatar và Tên
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=150"),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(80.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Phú Quang", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                }
            }

            // Menu Items
            Column(modifier = Modifier.background(Color.White)) {
                SettingsMenuItem(
                    icon = Icons.Default.Person,
                    title = "Profile & Address",
                    onClick = { navController.navigate("profile") }
                )
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
                SettingsMenuItem(
                    icon = Icons.Default.Info,
                    title = "About us",
                    onClick = { navController.navigate("about_us") }
                )
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
                SettingsMenuItem(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = "Log Out",
                    titleColor = primaryOrange,
                    iconColor = primaryOrange,
                    showArrow = false,
                    onClick = {
                        navController.navigate("login") { popUpTo(0) } // Xóa lịch sử và về Login
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    titleColor: Color = Color.DarkGray,
    iconColor: Color = Color.DarkGray,
    showArrow: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = iconColor)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontWeight = FontWeight.Medium, fontSize = 16.sp, color = titleColor, modifier = Modifier.weight(1f))
        if (showArrow) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Go", tint = Color(0xFFFF6D3F))
        }
    }
}