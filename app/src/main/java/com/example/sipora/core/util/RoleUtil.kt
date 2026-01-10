package com.example.sipora.core.util

object RoleUtil {
    fun ormawaIdFromRole(role: String): Long {
        return when {
            role.contains("bem", ignoreCase = true) -> 1L
            role.contains("dpm", ignoreCase = true) -> 2L
            else -> 0L // Default or error case
        }
    }
}
