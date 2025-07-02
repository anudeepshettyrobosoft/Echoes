package com.example.echoes.domain.usecases

import android.app.Application
import android.content.ContentResolver
import com.example.echoes.data.model.UploadNewsRequest
import com.example.echoes.data.model.UploadNewsResponse
import com.example.echoes.domain.repository.NewsRepository
import javax.inject.Inject

class UploadNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
    private val contentResolver: ContentResolver,
    private val application: Application,
) {
    suspend operator fun invoke(userId:String,newsRequest: UploadNewsRequest): Result<UploadNewsResponse> {
        return repository.uploadNews(userId,newsRequest, contentResolver, application.applicationContext)
    }
}
