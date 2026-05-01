package com.dertefter.doc_detail.usecase

import com.dertefter.navigation.Navigator
import javax.inject.Inject

class HideBottomSheetUseCase @Inject constructor(
    private val navigator: Navigator
) {
    operator fun invoke() {
        navigator.hideBottomSheet()
    }
}
