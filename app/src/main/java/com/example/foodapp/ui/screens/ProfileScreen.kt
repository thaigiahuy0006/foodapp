package com.example.foodapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val primaryOrange = Color(0xFFFF6D3F)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.popBackStack() },
                containerColor = primaryOrange,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Avatar
            Box(modifier = Modifier.size(100.dp)) {
                Image(
                    painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=150"),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // User Information Section
            Box(modifier = Modifier.fillMaxWidth()) {
                Text("User Information", color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = "Phú", onValueChange = {}, label = { Text("First name") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = "Quang", onValueChange = {}, label = { Text("Last name") }, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = "phuquang14722@gmail.com", onValueChange = {}, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = "male", onValueChange = {}, label = { Text("Gender") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = "0354351...", onValueChange = {}, label = { Text("Phone number") }, modifier = Modifier.weight(1.5f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Address Section
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Address", color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("ADD MORE", color = primaryOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.5f))

            // Address Items
            AddressItem("A\n23\nEa M'nang, Cư M'gar District, Dak Lak, Vietn")
            Divider(color = Color.LightGray.copy(alpha = 0.5f))
            AddressItem("QUANG\n12323\n1600 Plymouth St, Mountain View, CA")

            Spacer(modifier = Modifier.height(80.dp)) // Chừa chỗ cho Floating Button
        }
    }
}

@Composable
fun AddressItem(addressText: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = Color(0xFFFF6D3F))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = addressText, color = Color.Gray, fontSize = 14.sp)
    }
}