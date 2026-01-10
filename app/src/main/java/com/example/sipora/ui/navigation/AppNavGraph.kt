package com.example.sipora.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sipora.ui.admin.dashboard.AdminDashboardScreen
import com.example.sipora.ui.admin.pendaftar.PendaftarDetailScreen
import com.example.sipora.ui.admin.pendaftar.PendaftarListScreen
import com.example.sipora.ui.auth.LoginScreen
import com.example.sipora.ui.auth.RegisterScreen
import com.example.sipora.ui.main.DashboardScreen
import com.example.sipora.ui.profile.ProfileScreen
import com.example.sipora.ui.superadmin.admin.AdminFormScreen
import com.example.sipora.ui.superadmin.admin.AdminListScreen
import com.example.sipora.ui.superadmin.dashboard.SuperAdminDashboardScreen
import com.example.sipora.ui.superadmin.periode.PeriodeFormScreen
import com.example.sipora.ui.superadmin.periode.PeriodeListScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = { name, role ->
                    val route = when {
                        role.contains("SUPERADMIN", ignoreCase = true) -> Routes.SUPER_ADMIN_DASHBOARD
                        role.contains("ADMIN", ignoreCase = true) -> Routes.ADMIN_DASHBOARD
                        else -> Routes.DASHBOARD.replace("{name}", name)
                    }
                    navController.navigate(route) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Admin Routes
        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashboardScreen(
                onNavigateToPendaftar = { navController.navigate(Routes.ADMIN_PENDAFTAR_LIST) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) },
                onLogout = { navController.navigate(Routes.LOGIN) { popUpTo(0) } }
            )
        }
        composable(Routes.ADMIN_PENDAFTAR_LIST) {
            PendaftarListScreen(
                onNavigateToDetail = { pendaftarId ->
                    navController.navigate(Routes.ADMIN_PENDAFTAR_DETAIL.replace("{id}", pendaftarId.toString()))
                }
            )
        }
        composable(
            route = Routes.ADMIN_PENDAFTAR_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            PendaftarDetailScreen()
        }

        // Super Admin Routes
        composable(Routes.SUPER_ADMIN_DASHBOARD) {
            SuperAdminDashboardScreen(
                onLogout = { navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                onNavigateToAdmin = { navController.navigate(Routes.SUPER_ADMIN_ADMIN_LIST) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) },
                onNavigateToPeriode = { navController.navigate(Routes.SUPER_ADMIN_PERIODE_LIST) }
            )
        }

        // Other existing routes...
        composable(Routes.REGISTER) { /* ... */ }
        composable(Routes.DASHBOARD) { /* ... */ }
        composable(Routes.SUPER_ADMIN_ADMIN_LIST) { /* ... */ }
        composable(Routes.SUPER_ADMIN_ADMIN_FORM) { /* ... */ }
        composable(Routes.SUPER_ADMIN_PERIODE_LIST) { /* ... */ }
        composable(Routes.SUPER_ADMIN_PERIODE_FORM) { /* ... */ }
        composable(Routes.PROFILE) { /* ... */ }
    }
}
