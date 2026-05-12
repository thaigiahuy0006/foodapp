package com.example.foodapp.ui.screens

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(3) { // Giả lập hiển thị 3 quán đã lưu
                SavedEateryItem(navController, primaryOrange)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SavedEateryItem(navController: NavController, primaryOrange: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("detail") }, // Bấm vào để xem chi tiết
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1544025162-848f657a80fa?q=80&w=200"),
                contentDescription = "Quán ăn",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Cơm Ngô Quyền", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "1.48 km", color = Color.Gray, fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Text(text = " 4.0", color = Color.Gray, fontSize = 14.sp)
                }
            }
            // Icon thả tim màu cam báo hiệu đã lưu
            Icon(
                Icons.Default.Favorite,
                contentDescription = "Saved",
                tint = primaryOrange,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}