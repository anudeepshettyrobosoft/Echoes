package com.example.echoes.data.model

data class UploadNewsResponse(
    val success: Boolean,
    val message: String,
    val newsId: Int?
)