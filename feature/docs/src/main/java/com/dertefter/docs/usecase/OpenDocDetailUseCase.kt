package com.dertefter.docs.usecase

import com.dertefter.data.dto.docs.DocsItemDto
import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class OpenDocDetailUseCase @Inject constructor(
    private val navigator: Navigator
) {
    operator fun invoke(item: DocsItemDto) {
        navigator.openAsBottomSheet(
            Routes.DocDetail(
                type = item.type,
                date = item.date,
                status = item.status,
                person = item.person,
                comment = item.comment,
                number = item.number
            )
        )
    }
}
