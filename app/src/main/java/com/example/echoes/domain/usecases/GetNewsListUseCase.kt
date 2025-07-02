package com.example.echoes.domain.usecases

import android.content.Context
import com.example.echoes.domain.model.NewsItem
import com.example.echoes.domain.repository.NewsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetNewsListUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(userId: String): Result<List<NewsItem>> {
        return repository.getNewsList(userId)
    }
}