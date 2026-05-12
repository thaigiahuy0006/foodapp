package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavController // Đã thêm Import
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("nước") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF12151C)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                // Nút Back
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable { navController.popBackStack() } // Quay lại
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(24.dp)
                )
            }

            Divider(color = Color.LightGray.copy(alpha = 0.5f))

            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    // Click vào quán ăn sẽ mở màn hình Detail
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("detail") } // Chuyển trang
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1544025162-848f657a80fa?q=80&w=200"),
                            contentDescription = "Quán",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Cơm Ngô Quyền", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Quán cơm ngon số 1 Làng...", color = Color.Gray, fontSize = 12.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("1.48 km", color = Color.Gray, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                                Text(" 4.25", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    SearchProductItem("Nước mía", "10000.0", "https://images.unsplash.com/photo-1556679343-c7306c1976bc?q=80&w=150")
                    Spacer(modifier = Modifier.height(12.dp))
                    SearchProductItem("Nước dừa", "10000.0", "https://images.unsplash.com/photo-1605807646983-377bc5a76493?q=80&w=150")
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@Composable
fun SearchProductItem(name: String, price: String, imageUrl: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 76.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(text = price, fontSize = 12.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp))
        )
    }
}