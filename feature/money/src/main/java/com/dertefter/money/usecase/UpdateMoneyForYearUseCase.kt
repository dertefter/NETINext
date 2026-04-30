package com.dertefter.money.usecase

import com.dertefter.data.dto.money.MoneyItemDto
import com.dertefter.data.repository.MoneyRepository
import javax.inject.Inject

class UpdateMoneyForYearUseCase @Inject constructor(
    private val moneyRepository: MoneyRepository
) {
    suspend operator fun invoke(year: String): Result<List<MoneyItemDto>> {
        return moneyRepository.updateMoneyForYear(year)
    }
}
