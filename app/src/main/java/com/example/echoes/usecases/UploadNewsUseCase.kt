package com.example.echoes.usecases

import android.app.Application
import android.content.ContentResolver
import com.example.echoes.network.NewsRepository
import com.example.echoes.network.model.UploadNewsRequest
import com.example.echoes.network.model.UploadNewsResponse
import javax.inject.Inject

class UploadNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
    private val contentResolver: ContentResolver,
    private val application: Application,
) {
    suspend operator fun invoke(newsRequest: UploadNewsRequest): Result<UploadNewsResponse> {
        return repository.uploadNews(newsRequest,contentResolver,application.applicationContext)
    }
}
