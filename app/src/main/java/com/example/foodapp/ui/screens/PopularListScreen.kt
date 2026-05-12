package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopularListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("POPULAR", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF12151C)) // Giữ nền tối viền ngoài theo ảnh chụp
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                PopularEateryItem(
                    navController = navController,
                    name = "Bánh khọt đối diện vinmart",
                    address = "Đối diện vinmart",
                    distance = "10459,32 km",
                    rating = "5.0",
                    imageUrl = "https://images.unsplash.com/photo-1564834724105-918b73d1b9e0?q=80&w=600"
                )
                Spacer(modifier = Modifier.height(16.dp))
                PopularEateryItem(
                    navController = navController,
                    name = "Cơm Ngô Quyền",
                    address = "VQFX+5PW, Đông Hoà, Dĩ An, Bình Dương, Vietnam",
                    distance = "1.48 km",
                    rating = "4.25",
                    imageUrl = "https://images.unsplash.com/photo-1544025162-848f657a80fa?q=80&w=600"
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PopularEateryItem(navController: NavController, name: String, address: String, distance: String, rating: String, imageUrl: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("detail") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = address, color = Color.Gray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = distance, color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                    Text(text = " $rating", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}