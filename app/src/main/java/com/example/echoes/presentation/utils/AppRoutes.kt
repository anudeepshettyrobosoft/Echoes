package com.example.echoes.presentation.utils

sealed class AppRoutes(val route: String) {
    object Main : AppRoutes("main")
    object Login : AppRoutes("login")
    object Register : AppRoutes("register")
}