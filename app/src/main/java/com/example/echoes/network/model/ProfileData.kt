package com.example.echoes.network.model

import com.example.echoes.ui.AppManager

data class ProfileData(
    val id: String? = null,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val doj: String = "",
    val badges: List<BadgeItem>? = null
){
    fun getImageUrl(): String {
        return AppManager.PROFILE_IMAGE_URL + id
    }
}






