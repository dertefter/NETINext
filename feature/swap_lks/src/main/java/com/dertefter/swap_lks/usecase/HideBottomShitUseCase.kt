package com.dertefter.swap_lks.usecase

import com.dertefter.navigation.Navigator
import javax.inject.Inject

class HideBottomShitUseCase @Inject constructor( // piece of bottom shit :)))))))

    private val navigator: Navigator,
) {
    operator fun invoke(){
        return navigator.hideBottomSheet()
    }
}