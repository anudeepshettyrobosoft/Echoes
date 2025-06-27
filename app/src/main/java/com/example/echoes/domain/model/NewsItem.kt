package com.example.echoes.domain.model

import com.example.echoes.utils.AppManager

data class NewsItem(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val status: String,
    val submittedDate: String? = null,
    val reportedTime: String? = null,
    val comments: String? = null,
    val imageORVideoUrl: String? = null
) {
    fun getImageUrl(): String {
        return AppManager.NEWS_IMAGE_URL + id
    }
}

enum class NewsStatus(val statusName: String) {
    SUBMITTED("Submitted"),
    PENDING("Pending"),
    REVIEWED("Reviewed"),
    PUBLISHED("Published"),
    REJECTED("Rejected"),
}