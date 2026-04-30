package com.dertefter.money.usecase

import com.dertefter.data.repository.MoneyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoneyYearsUseCase @Inject constructor(
    private val moneyRepository: MoneyRepository
) {
    operator fun invoke(): Flow<List<String>> {
        return moneyRepository.getYears()
    }
}
