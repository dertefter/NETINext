package com.dertefter.neticlient.di

import com.dertefter.navigation.Navigator
import com.dertefter.neticlient.navigation.AppNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigator(
        appNavigator: AppNavigator
    ): Navigator
}
