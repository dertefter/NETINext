package com.dertefter.money.usecase

import com.dertefter.data.dto.money.MoneyItemDto
import com.dertefter.data.repository.MoneyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoneyForYearUseCase @Inject constructor(
    private val moneyRepository: MoneyRepository
) {
    operator fun invoke(year: String): Flow<List<MoneyItemDto>> {
        return moneyRepository.getMoneyForYear(year)
    }
}
