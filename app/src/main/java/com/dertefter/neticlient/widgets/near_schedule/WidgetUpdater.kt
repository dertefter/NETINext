package com.dertefter.neticlient.widgets.near_schedule

import android.content.Context
import androidx.glance.appwidget.updateAll
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface WidgetUpdater {
    suspend fun updateScheduleWidget()
}

@Singleton
class WidgetUpdaterImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : WidgetUpdater {
    override suspend fun updateScheduleWidget() {
        NearScheduleWidget().updateAll(context)
    }
}
