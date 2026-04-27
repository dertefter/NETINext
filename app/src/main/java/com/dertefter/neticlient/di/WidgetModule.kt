package com.dertefter.neticlient.di

import com.dertefter.neticlient.widgets.near_schedule.WidgetUpdater
import com.dertefter.neticlient.widgets.near_schedule.WidgetUpdaterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WidgetModule {

    @Binds
    @Singleton
    abstract fun bindWidgetUpdater(
        widgetUpdaterImpl: WidgetUpdaterImpl
    ): WidgetUpdater
}
