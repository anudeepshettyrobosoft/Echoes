package com.example.echoes.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.echoes.R

/*sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Upload : Screen("upload", "Upload", Icons.Default.AddCircle)
    object Profile : Screen("profile", "Profile", Icons.Default.Face)

}*/

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object UploadedArticles : Screen("uploaded_articles", "Articles", Icons.Default.List)
    object Upload : Screen("upload", "Upload", Icons.Default.AddCircle)
    object Profile : Screen("profile", "Profile", Icons.Default.Face)
}

enum class ImageLoadingState {
    LOADING,
    SUCCESS,
    ERROR
}