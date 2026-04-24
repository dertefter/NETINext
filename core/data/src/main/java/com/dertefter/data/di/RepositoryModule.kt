package com.dertefter.data.di

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.repository.AuthRepository
import com.dertefter.data.repository.AuthRepositoryImpl
import com.dertefter.data.repository.ContactInfoRepository
import com.dertefter.data.repository.ContactInfoRepositoryImpl
import com.dertefter.data.repository.GroupsRepository
import com.dertefter.data.repository.GroupsRepositoryImpl
import com.dertefter.data.repository.MessagesRepository
import com.dertefter.data.repository.MessagesRepositoryImpl
import com.dertefter.data.repository.MoneyRepository
import com.dertefter.data.repository.MoneyRepositoryImpl
import com.dertefter.data.repository.NewsRepository
import com.dertefter.data.repository.NewsRepositoryImpl
import com.dertefter.data.repository.PersonsRepository
import com.dertefter.data.repository.PersonsRepositoryImpl
import com.dertefter.data.repository.ScheduleRepository
import com.dertefter.data.repository.ScheduleRepositoryImpl
import com.dertefter.data.repository.SessiaResultsRepository
import com.dertefter.data.repository.SessiaResultsRepositoryImpl
import com.dertefter.data.repository.SettingsRepository
import com.dertefter.data.repository.SettingsRepositoryImpl
import com.dertefter.data.repository.ShareScoreRepository
import com.dertefter.data.repository.ShareScoreRepositoryImpl
import com.dertefter.data.repository.UserRepository
import com.dertefter.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        @ApplicationScope externalScope: CoroutineScope
    ): AuthRepository = AuthRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
        externalScope = externalScope
    )


    @Provides
    @Singleton
    fun provideUserRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): UserRepository = UserRepositoryImpl(
        localDataSource = localDataSource, remoteDataSource = remoteDataSource
    )

    @Provides
    @Singleton
    fun provideContactInfoRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): ContactInfoRepository = ContactInfoRepositoryImpl(
        localDataSource = localDataSource, remoteDataSource = remoteDataSource
    )

    @Provides
    @Singleton
    fun provideNewsRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): NewsRepository = NewsRepositoryImpl(
        localDataSource = localDataSource, remoteDataSource = remoteDataSource
    )

    @Provides
    @Singleton
    fun provideScheduleRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): ScheduleRepository = ScheduleRepositoryImpl(
        localDataSource = localDataSource, remoteDataSource = remoteDataSource
    )

    @Provides
    @Singleton
    fun provideGroupsRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): GroupsRepository = GroupsRepositoryImpl(
        localDataSource = localDataSource, remoteDataSource = remoteDataSource
    )

    @Provides
    @Singleton
    fun provideMessagesRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): MessagesRepository = MessagesRepositoryImpl(
        localDataSource = localDataSource, remoteDataSource = remoteDataSource
    )

    @Provides
    @Singleton
    fun providePersonsRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): PersonsRepository = PersonsRepositoryImpl(
        localDataSource = localDataSource, remoteDataSource = remoteDataSource
    )

    @Provides
    @Singleton
    fun provideSessiaResultsRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): SessiaResultsRepository = SessiaResultsRepositoryImpl(
        localDataSource = localDataSource, remoteDataSource = remoteDataSource
    )


    @Provides
    @Singleton
    fun provideShareScoreRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): ShareScoreRepository = ShareScoreRepositoryImpl(
        localDataSource = localDataSource, remoteDataSource = remoteDataSource
    )

    @Provides
    @Singleton
    fun provideSettingsRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): SettingsRepository = SettingsRepositoryImpl(
        localDataSource = localDataSource, remoteDataSource = remoteDataSource
    )

    @Provides
    @Singleton
    fun provideMoneyRepository(
        localDataSource: LocalDataSource, remoteDataSource: RemoteDataSource
    ): MoneyRepository = MoneyRepositoryImpl(
        localDataSource = localDataSource, remoteDataSource = remoteDataSource
    )


}
