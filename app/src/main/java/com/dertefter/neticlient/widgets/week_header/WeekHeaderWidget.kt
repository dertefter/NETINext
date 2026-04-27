package com.dertefter.neticlient.widgets.week_header

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import com.dertefter.data.repository.ScheduleRepository
import com.dertefter.neticlient.widgets.week_header.presentation.WeekHeaderWidgetContent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WeekHeaderWidgetEntryPoint {
    fun scheduleRepository(): ScheduleRepository
}

class WeekHeaderWidget : GlanceAppWidget() {

    override suspend fun providePreview(context: Context, widgetCategory: Int) {
        val entryPoint = EntryPointAccessors.fromApplication(context, WeekHeaderWidgetEntryPoint::class.java)
        val scheduleRepository = entryPoint.scheduleRepository()
        val weekHeader = scheduleRepository.getWeekHeader().first()

        provideContent {
            GlanceTheme {
                WeekHeaderWidgetContent(weekHeader)
            }
        }
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(context, WeekHeaderWidgetEntryPoint::class.java)
        val scheduleRepository = entryPoint.scheduleRepository()

        provideContent {
            val weekHeader by scheduleRepository.getWeekHeader().collectAsState(null)

            GlanceTheme {
                WeekHeaderWidgetContent(
                    weekHeader = weekHeader,
                    modifier = androidx.glance.GlanceModifier.clickable(actionRunCallback<UpdateWeekHeaderAction>())
                )
            }
        }
    }
}

class UpdateWeekHeaderAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val entryPoint = EntryPointAccessors.fromApplication(context, WeekHeaderWidgetEntryPoint::class.java)
        val scheduleRepository = entryPoint.scheduleRepository()
        scheduleRepository.updateWeekHeader()
        WeekHeaderWidget().updateAll(context)
    }
}
