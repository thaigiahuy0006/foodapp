package com.example.foodapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodapp.ui.theme.FoodappTheme

// Import TOÀN BỘ 10+ màn hình chúng ta đã code
import com.example.foodapp.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FoodAppNavigation()
                }
            }
        }
    }
}

@Composable
fun FoodAppNavigation() {
    val navController = rememberNavController()

    // Bắt đầu ứng dụng tại màn hình Đăng nhập
    NavHost(navController = navController, startDestination = "login") {

        // 1. Cụm Đăng nhập / Đăng ký
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("forgot_password") { ForgotPasswordScreen(navController) }

        // 2. Cụm Trang chủ & Tìm kiếm
        composable("home") { HomeScreen(navController) }
        composable("search") { SearchScreen(navController) }

        // 3. Cụm Phân loại & Danh sách quán ăn
        composable("category_list") { CategoryListScreen(navController) }
        composable("popular_list") { PopularListScreen(navController) }

        // 4. Cụm Chi tiết Quán & Món ăn
        composable("detail") { EateryDetailScreen(navController) }
        composable("eatery_info") { EateryInfoScreen(navController) }
        composable("product_detail") { ProductDetailScreen(navController) }

        // 5. Cụm Thanh điều hướng dưới cùng (Bottom Nav)
        composable("saved") { SavedScreen(navController) }
        composable("orders") { OrderScreen(navController) }
        composable("bag") { BagScreen(navController) }

        composable("settings") { SettingsScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("about_us") { AboutUsScreen(navController) }

        composable("checkout") { CheckoutScreen(navController) }
        composable("order_detail") { OrderDetailScreen(navController) }
    }
}