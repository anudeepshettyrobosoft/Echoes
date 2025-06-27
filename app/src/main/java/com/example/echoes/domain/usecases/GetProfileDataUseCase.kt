package com.example.echoes.domain.usecases

import com.example.echoes.domain.model.ProfileData
import com.example.echoes.domain.repository.NewsRepository
import javax.inject.Inject

class GetProfileDataUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Result<ProfileData> {
        return repository.getProfileData()
    }
}
