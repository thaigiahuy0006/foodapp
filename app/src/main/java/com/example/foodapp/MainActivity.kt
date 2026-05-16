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
import androidx.navigation.NavType
import androidx.navigation.navArgument

// Import toàn bộ màn hình
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

    NavHost(navController = navController, startDestination = "login") {

        // 1. Cụm Đăng nhập / Đăng ký
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("forgot_password") { ForgotPasswordScreen(navController) }

        // 2. Cụm Trang chủ & Tìm kiếm
        composable("home") { HomeScreen(navController) }
        composable("search") { SearchScreen(navController) }

        // 3. Cụm Phân loại & Danh sách quán ăn
        composable("popular_list") { PopularListScreen(navController) }
        composable(
            route = "category_list/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "1"
            CategoryListScreen(navController, categoryId)
        }

        // 4. Cụm Chi tiết Quán & Món ăn (Truyền ID)
        composable(
            route = "detail/{eateryId}",
            arguments = listOf(navArgument("eateryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eateryId = backStackEntry.arguments?.getString("eateryId") ?: "1"
            EateryDetailScreen(navController, eateryId)
        }
        composable("eatery_info") { EateryInfoScreen(navController) }
        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: "1"
            ProductDetailScreen(navController, productId)
        }

        // 5. Cụm Bottom Nav & Cá nhân
        composable("saved") { SavedScreen(navController) }
        composable("orders") { OrderScreen(navController) }
        composable("bag") { BagScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("about_us") { AboutUsScreen(navController) }

        // 6. Cụm Thanh toán & Đơn hàng
        composable("checkout") { CheckoutScreen(navController) }
        composable("order_detail") { OrderDetailScreen(navController) }
    }
}