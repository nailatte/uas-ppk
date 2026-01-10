package com.example.sipora.ui.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val DASHBOARD = "dashboard?name={name}" // For Mahasiswa

    // Super Admin Routes
    const val SUPER_ADMIN_DASHBOARD = "super_admin_dashboard"
    const val SUPER_ADMIN_ADMIN_LIST = "super_admin_admin_list"
    const val SUPER_ADMIN_ADMIN_FORM = "super_admin_admin_form"
    const val SUPER_ADMIN_PERIODE_LIST = "superadmin_periode_list"
    const val SUPER_ADMIN_PERIODE_FORM = "superadmin_periode_form?id={id}"

    // Admin Routes
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_PENDAFTAR_LIST = "admin_pendaftar_list"
    const val ADMIN_PENDAFTAR_DETAIL = "admin_pendaftar_detail/{id}"

    // Profile Route
    const val PROFILE = "profile"
}
