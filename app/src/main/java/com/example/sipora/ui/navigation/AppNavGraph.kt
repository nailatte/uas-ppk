package com.example.sipora.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sipora.ui.admin.dashboard.AdminDashboardScreen
import com.example.sipora.ui.admin.administrasi.AdminAdministrasiDetailScreen
import com.example.sipora.ui.admin.administrasi.AdminAdministrasiListScreen
import com.example.sipora.ui.admin.wawancara.AdminWawancaraPenilaianDetailScreen
import com.example.sipora.ui.admin.wawancara.AdminWawancaraPenilaianListScreen
import com.example.sipora.ui.admin.wawancara.AdminWawancaraUploadDetailScreen
import com.example.sipora.ui.admin.wawancara.AdminWawancaraUploadListScreen
import com.example.sipora.ui.admin.profile.AdminProfileScreen
import com.example.sipora.ui.auth.LoginScreen
import com.example.sipora.ui.auth.RegisterScreen
import com.example.sipora.ui.main.DashboardScreen
import com.example.sipora.ui.profile.ProfileScreen
import com.example.sipora.ui.superadmin.admin.AdminFormScreen
import com.example.sipora.ui.superadmin.admin.AdminListScreen
import com.example.sipora.ui.superadmin.dashboard.SuperAdminDashboardScreen
import com.example.sipora.ui.superadmin.periode.PeriodeFormScreen
import com.example.sipora.ui.superadmin.periode.PeriodeListScreen
import com.example.sipora.ui.superadmin.wawancara.SuperAdminWawancaraScreen

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

        // Super Admin Routes
        composable(Routes.SUPER_ADMIN_DASHBOARD) {
            SuperAdminDashboardScreen(
                onLogout = { navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                onNavigateToAdmin = { navController.navigate(Routes.SUPER_ADMIN_ADMIN_LIST) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) },
                onNavigateToPeriode = { navController.navigate(Routes.SUPER_ADMIN_PERIODE_LIST) },
                onNavigateToWawancara = { navController.navigate(Routes.SUPER_ADMIN_WAWANCARA) }
            )
        }
        composable(Routes.SUPER_ADMIN_WAWANCARA) {
            SuperAdminWawancaraScreen()
        }

        // Other routes are assumed to be correct
        composable(Routes.REGISTER) { RegisterScreen( onNavigateToLogin = { navController.popBackStack() }, onRegisterSuccess = { name -> navController.navigate(Routes.DASHBOARD.replace("{name}", name)) { popUpTo(Routes.LOGIN) { inclusive = true } } } ) }
        composable( route = Routes.DASHBOARD, arguments = listOf(navArgument("name") { type = NavType.StringType; defaultValue = "" })) { backStackEntry -> val name = backStackEntry.arguments?.getString("name"); DashboardScreen( name = name, onLogout = { navController.navigate(Routes.LOGIN) { popUpTo(0) } }, onNavigateToProfile = { navController.navigate(Routes.PROFILE) } ) }
        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashboardScreen(
                onNavigateToAdministrasi = { navController.navigate(Routes.ADMIN_ADMINISTRASI_LIST) },
                onNavigateToWawancara = { navController.navigate(Routes.ADMIN_WAWANCARA_NILAI_LIST) },
                onNavigateToKelolaWawancara = { navController.navigate(Routes.ADMIN_WAWANCARA_UPLOAD_LIST) },
                onNavigateToProfile = { navController.navigate(Routes.ADMIN_PROFILE) },
                onLogout = { navController.navigate(Routes.LOGIN) { popUpTo(0) } }
            )
        }
        composable(Routes.ADMIN_ADMINISTRASI_LIST) {
            AdminAdministrasiListScreen(
                onNavigateToDetail = { pendaftarId ->
                    navController.navigate(Routes.ADMIN_ADMINISTRASI_DETAIL.replace("{id}", pendaftarId.toString()))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.ADMIN_ADMINISTRASI_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            AdminAdministrasiDetailScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.ADMIN_WAWANCARA_NILAI_LIST) {
            AdminWawancaraPenilaianListScreen(
                onNavigateToDetail = { pendaftarId ->
                    navController.navigate(Routes.ADMIN_WAWANCARA_NILAI_DETAIL.replace("{id}", pendaftarId.toString()))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.ADMIN_WAWANCARA_NILAI_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            AdminWawancaraPenilaianDetailScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.ADMIN_WAWANCARA_UPLOAD_LIST) {
            AdminWawancaraUploadListScreen(
                onNavigateToDetail = { pendaftarId ->
                    navController.navigate(Routes.ADMIN_WAWANCARA_UPLOAD_DETAIL.replace("{id}", pendaftarId.toString()))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.ADMIN_WAWANCARA_UPLOAD_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            AdminWawancaraUploadDetailScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.ADMIN_PROFILE) {
            AdminProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = { navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                onEditProfile = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.SUPER_ADMIN_ADMIN_LIST) { AdminListScreen( onNavigateToCreateAdmin = { navController.navigate(Routes.SUPER_ADMIN_ADMIN_FORM) } ) }
        composable(Routes.SUPER_ADMIN_ADMIN_FORM) { AdminFormScreen(onNavigateBack = { navController.popBackStack() }) }
        composable(Routes.SUPER_ADMIN_PERIODE_LIST) { PeriodeListScreen( onNavigateToForm = { id -> val route = Routes.SUPER_ADMIN_PERIODE_FORM.replace("{id}", id?.toString() ?: "-1"); navController.navigate(route) } ) }
        composable( route = Routes.SUPER_ADMIN_PERIODE_FORM, arguments = listOf(navArgument("id") { type = NavType.LongType; defaultValue = -1L })) { PeriodeFormScreen(onNavigateBack = { navController.popBackStack() }) }
        composable(Routes.PROFILE) { ProfileScreen(onNavigateBack = { navController.popBackStack() }) }
    }
}
