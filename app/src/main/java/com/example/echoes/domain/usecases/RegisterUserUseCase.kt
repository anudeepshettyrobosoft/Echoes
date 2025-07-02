package com.example.echoes.domain.usecases

import com.example.echoes.data.model.RegisterUserRequest
import com.example.echoes.data.model.RegisterUserResponse
import com.example.echoes.domain.repository.NewsRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(newsRequest: RegisterUserRequest): Result<RegisterUserResponse> {
        return repository.registerUser(newsRequest)
    }
}
