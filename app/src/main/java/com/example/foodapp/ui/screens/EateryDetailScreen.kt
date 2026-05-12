package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
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
import androidx.navigation.NavController // Đã thêm Import
import coil.compose.rememberAsyncImagePainter

@Composable
fun EateryDetailScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                Image(
                    painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1544025162-848f657a80fa?q=80&w=800"),
                    contentDescription = "Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(top = 24.dp), // Thêm khoảng cách cho thanh status bar
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Nút Back
                    IconButton(
                        onClick = { navController.popBackStack() }, // Quay lại
                        modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    IconButton(
                        onClick = { },
                        modifier = Modifier.background(Color.White, CircleShape)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorite", tint = primaryOrange)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Cơm Ngô Quyền", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("1.48 km", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                        Text(" 4.0", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }
        }

        item {
            Text(
                text = "Product",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        items(2) { index ->
            ProductItemRow(
                name = if (index == 0) "Nước mía" else "Nước dừa",
                desc = if (index == 0) "Mía" else "Dừa trái",
                price = "10000.0 VND",
                primaryOrange = primaryOrange,
                navController = navController // TRUYỀN THÊM BIẾN NÀY VÀO
            )
        }
    }
}

@Composable
fun ProductItemRow(
    name: String,
    desc: String,
    price: String,
    primaryOrange: Color,
    navController: NavController // THÊM THAM SỐ NÀY
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable { navController.navigate("product_detail") } // BẤM VÀO ĐÂY ĐỂ CHUYỂN TRANG
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1546173159-315724a31696?q=80&w=200"),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(70.dp).clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = desc, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = price, color = primaryOrange, fontWeight = FontWeight.Bold)
        }
    }
}
