package com.dertefter.neticlient.widgets.near_schedule

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.PreviewSizeMode
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import com.dertefter.data.repository.GroupsRepository
import com.dertefter.data.repository.ScheduleRepository
import com.dertefter.neticlient.widgets.near_schedule.presentation.NearScheduleWidgetContent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun scheduleRepository(): ScheduleRepository
    fun groupsRepository(): GroupsRepository
}

class NearScheduleWidget : GlanceAppWidget() {

    override val previewSizeMode: PreviewSizeMode = SizeMode.Responsive(
        setOf(DpSize(180.dp, 110.dp))
    )

    override suspend fun providePreview(context: Context, widgetCategory: Int) {
        val entryPoint = EntryPointAccessors.fromApplication(context, WidgetEntryPoint::class.java)
        val groupsRepository = entryPoint.groupsRepository()
        val scheduleRepository = entryPoint.scheduleRepository()

        val group = groupsRepository.getCurrentGroup().first()
        val schedule = group?.let { scheduleRepository.getSchedule(it).first() } ?: emptyList()
        val sessiaSchedule = group?.let { scheduleRepository.getSessiaSchedule(it).first() } ?: emptyList()
        val list = schedule + sessiaSchedule

        provideContent {
            GlanceTheme {
                NearScheduleWidgetContent(group?.name, list)
            }
        }
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(context, WidgetEntryPoint::class.java)
        val groupsRepository = entryPoint.groupsRepository()
        val scheduleRepository = entryPoint.scheduleRepository()

        provideContent {
            val group by groupsRepository.getCurrentGroup().collectAsState(null)

            val schedule by remember(group) {
                group?.let { scheduleRepository.getSchedule(it) } ?: flowOf(emptyList())
            }.collectAsState(emptyList())

            val sessiaSchedule by remember(group) {
                group?.let { scheduleRepository.getSessiaSchedule(it) } ?: flowOf(emptyList())
            }.collectAsState(emptyList())

            val list = (schedule ?: emptyList()) + (sessiaSchedule ?: emptyList())

            GlanceTheme {
                NearScheduleWidgetContent(group?.name, list)
            }
        }
    }
}
