package com.example.echoes.network.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.echoes.R

data class BadgeItem(
    val badgeType: String,
    val count: Int = 0,

    ){
    fun getBadgeIcon():Int?{
        return when(badgeType) {
            "BRONZE" -> {
                R.drawable.badge_bronze // Set icon for bronze badge
            }

            "SILVER" -> {
                R.drawable.badge_silver // Set icon for silver badge
            }

            "GOLD" -> {
                R.drawable.badge_gold // Set icon for gold badge
            }

            else -> {
                null
            }
        }
    }
}
