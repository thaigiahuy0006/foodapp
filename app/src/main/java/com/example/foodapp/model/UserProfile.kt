package com.example.foodapp.model

data class UserProfile(
    val id: Int,
    val email: String,
    val first_name: String?,
    val last_name: String?,
    val gender: String?,
    val phone: String?,
    val address: String?,
    val avatar_url: String? // ĐÃ THÊM trường này
)

data class ProfileResponse(
    val success: Boolean,
    val user: UserProfile?,
    val message: String?
)

// data class hứng kết quả sau khi upload ảnh thành công
data class UploadAvatarResponse(
    val success: Boolean,
    val message: String,
    val avatar_url: String?
)
