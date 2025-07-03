package com.example.echoes.domain.usecases

import com.example.echoes.domain.model.Voucher
import com.example.echoes.domain.repository.NewsRepository
import javax.inject.Inject

class GetVouchersListUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(): Result<List<Voucher>> {
        return repository.getVouchersList()
    }
}