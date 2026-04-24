package com.dertefter.neticlient.foreground

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.dertefter.data.repository.GroupsRepository
import com.dertefter.data.repository.ScheduleRepository
import com.dertefter.neticlient.MainActivity
import com.dertefter.neticlient.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleService : Service() {

    @Inject lateinit var scheduleRepository: ScheduleRepository
    @Inject lateinit var groupsRepository: GroupsRepository
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val channelId = "my_foreground_channel"
    private val notificationId = 1000 - 7

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        val initialNotification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.loading))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        startForeground(notificationId, initialNotification)

        serviceScope.launch {
            while (isActive) {
                try {

                    val currentDate = LocalDate.now()
                    val currentTime = LocalTime.now()

                    val group = groupsRepository.getCurrentGroup().first()

                    if (group == null) {
                        updateNotification(getString(R.string.no_group), getString(R.string.select_group_in_app))
                    } else {
                        val schedule = scheduleRepository.getSchedule(group).first()
                        if (schedule.isNullOrEmpty()) {
                            updateNotification(getString(R.string.loading), getString(R.string.loading_schedule_for_group, group.name))
                            scheduleRepository.updateScheduleForGroup(group)
                        } else {
                            val grouped = schedule.groupBy { it.getDate() }
                            val todayLessons = grouped[currentDate]

                            val targetDate =
                                if (todayLessons != null && todayLessons.any { it.getEndTime().isAfter(currentTime) }) {
                                    currentDate
                                } else {
                                    grouped.keys.filter { it.isAfter(currentDate) }.minOrNull()
                                }

                            if (targetDate != null) {
                                val lessonsOnTargetDate = grouped[targetDate]?.sortedBy { it.getStartTime() }
                                val nextOrCurrentTimeSlot = lessonsOnTargetDate?.find { it.getEndTime().isAfter(currentTime) || targetDate.isAfter(currentDate) }

                                if (nextOrCurrentTimeSlot != null) {
                                    val lesson = nextOrCurrentTimeSlot.lessons.firstOrNull()
                                    val startTime = nextOrCurrentTimeSlot.getStartTime()
                                    val endTime = nextOrCurrentTimeSlot.getEndTime()

                                    val isCurrent = currentDate == targetDate && currentTime.isAfter(startTime) && currentTime.isBefore(endTime)

                                    val whenTime = if (isCurrent) {
                                        System.currentTimeMillis()
                                    } else {
                                        startTime.atDate(targetDate)
                                            .atZone(ZoneId.systemDefault())
                                            .toInstant()
                                            .toEpochMilli()
                                    }

                                    val progress = if (isCurrent) {
                                        calculateProgress(startTime, endTime, currentTime)
                                    } else -1

                                    updateNotification(
                                        title = lesson?.name ?: getString(R.string.lesson_placeholder),
                                        content = if (isCurrent) getString(R.string.going_now) else formatTargetDate(targetDate, nextOrCurrentTimeSlot.startTimeString),
                                        aud = lesson?.aud,
                                        type = lesson?.type,
                                        startTime = nextOrCurrentTimeSlot.startTimeString,
                                        endTime = nextOrCurrentTimeSlot.endTimeString,
                                        progress = progress,
                                        whenTime = whenTime
                                    )
                                } else {
                                    updateNotification(getString(R.string.no_lessons), getString(R.string.no_lessons_near_time))
                                }
                            } else {
                                updateNotification(getString(R.string.no_lessons), getString(R.string.no_lessons_near_time))
                            }
                        }
                    }


                } catch (_: Exception) {

                }

                delay(6000L)
            }
        }
    }

    private fun updateNotification(
        title: String,
        content: String = "",
        aud: String? = null,
        type: String? = null,
        startTime: String? = null,
        endTime: String? = null,
        progress: Int = -1,
        whenTime: Long? = null
    ) {
        val packageName = packageName
        val smallView = RemoteViews(packageName, R.layout.notification_lesson_small).apply {
            setTextViewText(R.id.notification_title, title)
        }

        val largeView = RemoteViews(packageName, R.layout.notification_lesson_large).apply {
            setTextViewText(R.id.notification_title, title)
            setTextViewText(R.id.notification_body, content)

            if (aud != null) {
                setViewVisibility(R.id.aud, View.VISIBLE)
                setTextViewText(R.id.aud, aud)
            } else {
                setViewVisibility(R.id.aud, View.GONE)
            }

            if (type != null) {
                setViewVisibility(R.id.type, View.VISIBLE)
                setTextViewText(R.id.type, type)
            } else {
                setViewVisibility(R.id.type, View.GONE)
            }

            if (startTime != null && endTime != null && progress >= 0) {
                setViewVisibility(R.id.on_going_container, View.VISIBLE)
                setTextViewText(R.id.timeStart, startTime)
                setTextViewText(R.id.timeEnd, endTime)
                setProgressBar(R.id.progress, 100, progress, false)
            } else {
                setViewVisibility(R.id.on_going_container, View.GONE)
            }
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(if (progress != -1) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setCustomContentView(smallView)
            .setCustomBigContentView(largeView)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .apply {
                if (whenTime != null) {
                    setWhen(whenTime)
                    setShowWhen(true)
                }
            }
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager?.notify(notificationId, notification)
    }
    

    private fun formatTargetDate(targetDate: LocalDate, startTime: String?): String {
        val nowDate = LocalDate.now()
        val diff = ChronoUnit.DAYS.between(nowDate, targetDate)
        val primaryLine: String? = when (diff) {
            0L -> getString(R.string.pretty_date_today)
            1L -> getString(R.string.pretty_date_tomorrow)
            2L -> getString(R.string.pretty_date_after_tomorrow)
            in 3L..6L -> resources.getQuantityString(R.plurals.pretty_date_days, diff.toInt(), diff.toInt())
            else -> null
        }

        val timeSuffix = if (startTime != null) " ${getString(R.string.in_what)} $startTime" else ""

        return if (primaryLine != null) {
            "$primaryLine$timeSuffix"
        } else {
            val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault())
            val secondaryLine = targetDate.format(formatter).replaceFirstChar { it.uppercase() }
            "$secondaryLine$timeSuffix"
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            getString(R.string.app_name),
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
    

    private fun calculateProgress(
        startTime: LocalTime?,
        endTime: LocalTime?,
        currentTime: LocalTime?
    ): Int {
        if (startTime == null || endTime == null || currentTime == null) return 0

        val totalMinutes = startTime.until(endTime, ChronoUnit.MINUTES)
        val elapsedMinutes = startTime.until(currentTime, ChronoUnit.MINUTES)

        return if (totalMinutes > 0) {
            ((elapsedMinutes.toDouble() / totalMinutes.toDouble()) * 100)
                .toInt().coerceIn(0, 100)
        } else 0
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}