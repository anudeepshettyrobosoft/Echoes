package com.example.echoes.domain.usecases

import com.example.echoes.domain.model.NewsItem
import com.example.echoes.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsListUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Result<List<NewsItem>> {
        return repository.getNewsList()
    }
}