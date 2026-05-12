package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun OrderDetailScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)
    val successGreen = Color(0xFF4CAF50)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF12151C) // Nền tối
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).padding(top = 40.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Order Id", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                            Text("he11KBUX0z4etvPPW6pQ", color = Color.Gray, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Order Status", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                            Text("Completed", color = successGreen, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Order time", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                            Text("14 thg 6, 2022 14:37:10", color = Color.Gray, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Text("Deliver to", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("VRJ6+8HH, Đông Hoà, Dĩ An, Bình Dương, Việt Nam", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Text("Contact", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Phú Quang, 035431...", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Text("Order summary", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Món 1
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1546173159-315724a31696?q=80&w=200"),
                                contentDescription = "Cơm tấm",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Cơm tấm", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(text = "x 2", color = Color.Gray, fontSize = 14.sp)
                            }
                            Text(text = "50000.0 VND", color = primaryOrange, fontWeight = FontWeight.Bold)
                        }

                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Total", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text("105000.0 VND", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryOrange)
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { navController.popBackStack() }, // Đóng thẻ
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("CLOSE", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}