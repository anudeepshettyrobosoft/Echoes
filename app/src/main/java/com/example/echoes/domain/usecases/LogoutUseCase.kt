package com.example.echoes.domain.usecases

import com.example.echoes.data.model.LoginRequest
import com.example.echoes.data.model.LoginResponse
import com.example.echoes.data.model.LogoutResponse
import com.example.echoes.data.model.RegisterUserRequest
import com.example.echoes.data.model.RegisterUserResponse
import com.example.echoes.domain.repository.NewsRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Result<LogoutResponse> {
        return repository.logout()
    }
}
