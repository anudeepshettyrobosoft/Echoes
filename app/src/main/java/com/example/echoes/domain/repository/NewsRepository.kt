package com.example.echoes.domain.repository

import android.content.ContentResolver
import android.content.Context
import com.example.echoes.data.model.UploadNewsRequest
import com.example.echoes.data.model.UploadNewsResponse
import com.example.echoes.domain.model.NewsItem
import com.example.echoes.domain.model.ProfileData

interface NewsRepository {
    suspend fun uploadNews(
        newsRequest: UploadNewsRequest,
        contentResolver: ContentResolver,
        context: Context
    ): Result<UploadNewsResponse>

    suspend fun getNewsList(): Result<List<NewsItem>>

    suspend fun getProfileData(): Result<ProfileData>
}

