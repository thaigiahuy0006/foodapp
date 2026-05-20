package com.example.foodapp.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.foodapp.network.RetrofitClient
import com.example.foodapp.model.UserSession
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val primaryOrange = Color(0xFFFF6D3F)
    val coroutineScope = rememberCoroutineScope()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(true) }
    var isUploadingAvatar by remember { mutableStateOf(false) }
    var isUpdatingProfile by remember { mutableStateOf(false) } // Biến theo dõi tiến trình cập nhật

    // BỘ CHỌN VÀ UPLOAD ẢNH ĐẠI DIỆN
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { selectedUri ->
                isUploadingAvatar = true
                coroutineScope.launch {
                    try {
                        val avatarPart = createMultipartBody(selectedUri, context)
                        if (avatarPart != null) {
                            // ĐÃ SỬA: Lấy đúng ID thực tế thay vì gán cứng số "1"
                            val userIdPart = UserSession.userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                            val response = RetrofitClient.apiService.uploadAvatar(userIdPart, avatarPart)

                            if (response.success && response.avatar_url != null) {
                                avatarUrl = response.avatar_url
                                Toast.makeText(context, "Cập nhật ảnh thành công!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) { e.printStackTrace() }
                    finally { isUploadingAvatar = false }
                }
            }
        }
    )

    // LẤY DỮ LIỆU BAN ĐẦU
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = RetrofitClient.apiService.getProfile(UserSession.userId)
                if (response.success && response.user != null) {
                    firstName = response.user.first_name ?: ""
                    lastName = response.user.last_name ?: ""
                    email = response.user.email
                    gender = response.user.gender ?: ""
                    phone = response.user.phone ?: ""
                    address = response.user.address ?: ""
                    avatarUrl = response.user.avatar_url ?: ""
                }
            } catch (e: Exception) { e.printStackTrace() }
            finally { isLoading = false }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hồ sơ cá nhân", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        // ---- ĐÃ THÊM LOGIC CHO NÚT CẬP NHẬT (TICK MÀU CAM) ----
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!isUpdatingProfile) {
                        isUpdatingProfile = true
                        coroutineScope.launch {
                            try {
                                val request = mapOf(
                                    "user_id" to UserSession.userId.toString(),
                                    "first_name" to firstName,
                                    "last_name" to lastName,
                                    "phone" to phone,
                                    "address" to address,
                                    "gender" to gender
                                )
                                val response = RetrofitClient.apiService.updateProfile(request)

                                if (response.success) {
                                    Toast.makeText(context, response.message ?: "Đã lưu thay đổi!", Toast.LENGTH_SHORT).show()
                                    // Tùy chọn: Tự động đóng màn hình sau khi lưu thành công
                                    // navController.popBackStack()
                                } else {
                                    Toast.makeText(context, "Lỗi: ${response.message}", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show()
                            } finally {
                                isUpdatingProfile = false
                            }
                        }
                    }
                },
                containerColor = primaryOrange,
                shape = CircleShape
            ) {
                if (isUpdatingProfile) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Check, contentDescription = "Lưu", tint = Color.White)
                }
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryOrange)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // HIỂN THỊ ẢNH ĐẠI DIỆN
                Box(contentAlignment = Alignment.BottomEnd) {
                    val displayUrl = if (avatarUrl.isNotEmpty()) "$avatarUrl?t=${System.currentTimeMillis()}"
                    else "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=150"

                    Image(
                        painter = rememberAsyncImagePainter(model = displayUrl),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                    IconButton(
                        onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                        modifier = Modifier
                            .size(32.dp)
                            .background(primaryOrange, CircleShape)
                            .padding(4.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Đổi ảnh", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                    if (isUploadingAvatar) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(100.dp).padding(30.dp), strokeWidth = 5.dp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text("Thông tin người dùng", color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Tên") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Họ") }, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = email, onValueChange = { }, label = { Text("Email (Không thể thay đổi)") }, modifier = Modifier.fillMaxWidth(), enabled = false)
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Giới tính") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Số điện thoại") }, modifier = Modifier.weight(1.5f))
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Địa chỉ giao hàng mặc định") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(100.dp)) // Tạo khoảng trống dưới cùng để không bị che bởi FAB
            }
        }
    }
}

// --- HÀM HELPER CHUYỂN URI ANDROID THÀNH FILE GỬI LÊN SERVER ---
private fun createMultipartBody(uri: Uri, context: android.content.Context): MultipartBody.Part? {
    try {
        val contentResolver = context.contentResolver
        var fileName = "temp_avatar"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) fileName = cursor.getString(nameIndex)
            }
        }

        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri))
        val tempFile = File(context.cacheDir, fileName)

        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        } ?: return null

        val mediaType = contentResolver.getType(uri)?.toMediaTypeOrNull()
        val requestBody = tempFile.asRequestBody(mediaType)
        return MultipartBody.Part.createFormData("avatar", tempFile.name, requestBody)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}