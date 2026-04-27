package com.dertefter.neticlient.widgets.near_schedule

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.dertefter.neticlient.widgets.week_header.WeekHeaderWidget
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface WidgetUpdater {
    suspend fun updateScheduleWidget()
    suspend fun updateWeekHeaderWidget()
}

@Singleton
class WidgetUpdaterImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : WidgetUpdater {
    override suspend fun updateScheduleWidget() {
        NearScheduleWidget().updateAll(context)
    }

    override suspend fun updateWeekHeaderWidget() {
        WeekHeaderWidget().updateAll(context)
    }
}
