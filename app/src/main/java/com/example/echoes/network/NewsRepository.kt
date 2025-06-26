package com.example.echoes.network

import android.content.ContentResolver
import android.content.Context
import com.example.echoes.network.model.NewsItem
import com.example.echoes.network.model.ProfileData
import com.example.echoes.network.model.UploadNewsRequest
import com.example.echoes.network.model.UploadNewsResponse

interface NewsRepository {
    suspend fun uploadNews(newsRequest: UploadNewsRequest,contentResolver: ContentResolver,context: Context): Result<UploadNewsResponse>

    suspend fun getNewsList(): Result<List<NewsItem>>

    suspend fun getProfileData(): Result<ProfileData>
}

