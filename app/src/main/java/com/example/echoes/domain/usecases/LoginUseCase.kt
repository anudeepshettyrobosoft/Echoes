package com.example.echoes.domain.usecases

import com.example.echoes.data.model.LoginRequest
import com.example.echoes.data.model.LoginResponse
import com.example.echoes.data.model.RegisterUserRequest
import com.example.echoes.data.model.RegisterUserResponse
import com.example.echoes.domain.repository.NewsRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(loginRequest: LoginRequest): Result<LoginResponse> {
        return repository.login(loginRequest)
    }
}
