package com.example.echoes.usecases

import com.example.echoes.network.NewsRepository
import com.example.echoes.network.model.ProfileData
import javax.inject.Inject

class GetProfileDataUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Result<ProfileData> {
        return repository.getProfileData()
    }
}
