package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
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
fun EateryInfoScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eatery Info", fontWeight = FontWeight.Medium, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // Ảnh bản đồ minh họa
            Image(
                painter = rememberAsyncImagePainter("https://maps.googleapis.com/maps/api/staticmap?center=10.870,106.803&zoom=15&size=600x300&maptype=roadmap&markers=color:red%7C10.870,106.803&key=YOUR_API_KEY_HERE"), // Placeholder URL
                contentDescription = "Map Location",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tên Quán và Rating
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = "Cơm Ngô Quyền", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "1,48 km", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Text(text = " 4.0", color = Color.Gray, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(24.dp))

                // Phần Contact Information
                Text(text = "Contact Information", fontSize = 18.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Address", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "VQFX+5PW, Đông Hoà, Dĩ An,\nBình Dương, Vietnam",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Phần Opening Hours
                Text(text = "Opening Hours", fontSize = 18.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = "Work Time", tint = Color.Gray)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Work Time", color = Color.Gray, fontSize = 14.sp)
                    }
                    Text(text = "7:00 - 19:00", color = Color.DarkGray, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}