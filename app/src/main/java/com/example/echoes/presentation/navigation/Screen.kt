package com.example.echoes.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.echoes.R

sealed class Screen(val route: String, val label: String) {
    data class WithVector(val screenRoute: String, val screenLabel: String, val icon: ImageVector) :
        Screen(screenRoute, screenLabel)

    data class WithDrawable(val screenRoute: String, val screenLabel: String, @DrawableRes val icon: Int) :
        Screen(screenRoute, screenLabel)

    companion object {
        val UploadedArticles = WithVector("uploaded_articles", "Articles", Icons.Default.List)
        val Upload = WithVector("upload", "Upload", Icons.Default.AddCircle)
        val Rewards = WithDrawable("rewards", "Rewards", R.drawable.ic_gift)
        val Profile = WithVector("profile", "Profile", Icons.Default.Face)

        val bottomNavScreens = listOf(UploadedArticles, Upload, Rewards, Profile)
    }
}

