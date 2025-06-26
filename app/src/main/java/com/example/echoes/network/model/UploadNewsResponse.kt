package com.example.echoes.network.model

data class UploadNewsResponse(
    val success: Boolean,
    val message: String,
    val newsId: Int?
)