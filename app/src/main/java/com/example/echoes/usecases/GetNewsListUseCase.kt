package com.example.echoes.usecases

import com.example.echoes.network.NewsRepository
import com.example.echoes.network.model.NewsItem
import javax.inject.Inject

class GetNewsListUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Result<List<NewsItem>> {
        return repository.getNewsList()
    }
}