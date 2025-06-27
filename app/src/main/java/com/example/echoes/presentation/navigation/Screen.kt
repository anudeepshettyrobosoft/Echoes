package com.example.echoes.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object UploadedArticles : Screen("uploaded_articles", "Articles", Icons.Default.List)
    data object Upload : Screen("upload", "Upload", Icons.Default.AddCircle)
    data object Profile : Screen("profile", "Profile", Icons.Default.Face)
}

