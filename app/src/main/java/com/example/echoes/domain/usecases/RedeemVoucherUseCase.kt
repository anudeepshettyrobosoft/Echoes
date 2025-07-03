package com.example.echoes.domain.usecases

import com.example.echoes.data.model.GenericResponse
import com.example.echoes.domain.model.ProfileData
import com.example.echoes.domain.repository.NewsRepository
import javax.inject.Inject

class RedeemVoucherUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(voucherId:String): Result<GenericResponse> {
        return repository.redeemVoucher(voucherId)
    }
}
