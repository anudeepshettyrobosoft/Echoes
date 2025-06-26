package com.example.echoes.network.model

import android.net.Uri

data class UploadNewsRequest(
    val title: String,
    val category: String,
    val description: String,
    val location: String? = null,
    val reportedDate: String?= null,
    val reportedTime: String?=null,
    val imageList: List<Uri>? = null
)