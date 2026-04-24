package com.dertefter.data.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dertefter.data.datasource.local.room.converter.Converters
import com.dertefter.data.datasource.local.room.dao.AccountDao
import com.dertefter.data.datasource.local.room.dao.GlobalConfigDao
import com.dertefter.data.datasource.local.room.dao.MessageDao
import com.dertefter.data.datasource.local.room.dao.NewsDao
import com.dertefter.data.datasource.local.room.dao.NewsRemoteKeyDao
import com.dertefter.data.datasource.local.room.dao.PersonDao
import com.dertefter.data.datasource.local.room.dao.ScheduleDao
import com.dertefter.data.datasource.local.room.dao.MoneyDao
import com.dertefter.data.datasource.local.room.entity.AccountEntity
import com.dertefter.data.datasource.local.room.entity.GlobalConfigEntity
import com.dertefter.data.datasource.local.room.entity.MessageEntity
import com.dertefter.data.datasource.local.room.entity.MoneyEntity
import com.dertefter.data.datasource.local.room.entity.NewsEntity
import com.dertefter.data.datasource.local.room.entity.NewsRemoteKey
import com.dertefter.data.datasource.local.room.entity.PersonEntity
import com.dertefter.data.datasource.local.room.entity.ScheduleEntity

@Database(
    entities = [
        AccountEntity::class,
        MessageEntity::class,
        ScheduleEntity::class,
        GlobalConfigEntity::class,
        PersonEntity::class,
        NewsEntity::class,
        NewsRemoteKey::class,
        MoneyEntity::class
    ],
    version = 12,
    exportSchema = false
)


@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun messageDao(): MessageDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun globalConfigDao(): GlobalConfigDao
    abstract fun personDao(): PersonDao
    abstract fun newsDao(): NewsDao
    abstract fun newsRemoteKeyDao(): NewsRemoteKeyDao
    abstract fun moneyDao(): MoneyDao
}
