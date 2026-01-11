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
    const val SUPER_ADMIN_WAWANCARA = "superadmin_wawancara"

    // Admin Routes
    const val ADMIN_DASHBOARD = "admin/dashboard"
    const val ADMIN_ADMINISTRASI_LIST = "admin/administrasi"
    const val ADMIN_ADMINISTRASI_DETAIL = "admin/administrasi/{id}"
    const val ADMIN_WAWANCARA_NILAI_LIST = "admin/wawancara/penilaian"
    const val ADMIN_WAWANCARA_NILAI_DETAIL = "admin/wawancara/penilaian/{id}"
    const val ADMIN_WAWANCARA_UPLOAD_LIST = "admin/wawancara/kelola"
    const val ADMIN_WAWANCARA_UPLOAD_DETAIL = "admin/wawancara/kelola/{id}"
    const val ADMIN_PROFILE = "admin/profile"

    // Profile Route
    const val PROFILE = "profile"
}
