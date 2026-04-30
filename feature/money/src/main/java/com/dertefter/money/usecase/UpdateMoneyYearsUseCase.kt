package com.dertefter.money.usecase

import com.dertefter.data.repository.MoneyRepository
import javax.inject.Inject

class UpdateMoneyYearsUseCase @Inject constructor(
    private val moneyRepository: MoneyRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        return moneyRepository.updateYears()
    }
}
