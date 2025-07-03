package com.example.echoes.domain.model

import com.example.echoes.utils.AppManager

data class ProfileData(
    val id: String? = null,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val doj: String = "",
    val rewardPoint: String? = null,
    val badges: List<BadgeItem>? = null
) {
    fun getImageUrl(): String {
        return AppManager.PROFILE_IMAGE_URL + id
    }
}






