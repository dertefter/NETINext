package com.dertefter.data.di

import android.content.Context
import androidx.room.Room
import com.dertefter.data.datasource.local.DatastoreManager
import com.dertefter.data.datasource.local.EncryptedAuthStorage
import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.local.LocalDataSourceImpl
import com.dertefter.data.datasource.local.room.AppDatabase
import com.dertefter.data.datasource.local.room.dao.AccountDao
import com.dertefter.data.datasource.local.room.dao.MessageDao
import com.dertefter.data.datasource.local.room.dao.ScheduleDao
import com.dertefter.data.datasource.remote.CiuRemoteDataSource
import com.dertefter.data.datasource.remote.CiuRemoteDataSourceImpl
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.datasource.remote.RemoteDataSourceImpl
import com.dertefter.data.datasource.remote.SessionCookieJar
import com.dertefter.data.datasource.remote.YourNetiRemoteDataSource
import com.dertefter.data.datasource.remote.YourNetiRemoteDataSourceImpl
import com.dertefter.data.datasource.remote.api.BaseApiService
import com.dertefter.data.datasource.remote.api.CiuApiService
import com.dertefter.data.datasource.remote.api.Login2ApiService
import com.dertefter.data.datasource.remote.api.YourNetiApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "neti_database"
            ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    fun provideAccountDao(database: AppDatabase): AccountDao = database.accountDao()

    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao = database.messageDao()

    @Provides
    fun provideScheduleDao(database: AppDatabase): ScheduleDao = database.scheduleDao()

    @Provides
    @Singleton
    fun provideLocalDataSource(
        appDatabase: AppDatabase,
        encryptedAuthStorage: EncryptedAuthStorage,
        datastoreManager: DatastoreManager
    ): LocalDataSource = LocalDataSourceImpl(
        appDatabase,
        encryptedAuthStorage,
        datastoreManager
    )

    @Singleton
    @Provides
    @Named("base")
    fun provideBaseOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }


    @Singleton
    @Provides
    @Named("ciu")
    fun provideCiuSessionCookieJar(): SessionCookieJar {
        return SessionCookieJar()
    }

    @Singleton
    @Provides
    @Named("your_neti")
    fun provideYourNetiSessionCookieJar(): SessionCookieJar {
        return SessionCookieJar()
    }

    @Singleton
    @Provides
    @Named("ciu")
    fun provideCiuOkHttpClient(
        @Named("ciu") sessionCookieJar: SessionCookieJar,
    ): OkHttpClient {
        return OkHttpClient.Builder().cookieJar(sessionCookieJar).build()
    }

    @Singleton
    @Provides
    @Named("login_2")
    fun provideLogin2OkHttpClient(
        @Named("ciu") sessionCookieJar: SessionCookieJar,
    ): OkHttpClient {
        return OkHttpClient.Builder().cookieJar(sessionCookieJar).build()
    }

    @Singleton
    @Provides
    @Named("your_neti")
    fun provideYourNetiOkHttpClient(
        @Named("your_neti") sessionCookieJar: SessionCookieJar,
    ): OkHttpClient {
        return OkHttpClient.Builder().cookieJar(sessionCookieJar).build()
    }


    @Provides
    @Singleton
    fun provideLogin2ApiService(@Named("login_2") client: OkHttpClient): Login2ApiService {
        val retrofit = Retrofit.Builder().baseUrl("https://login2.nstu.ru/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(Login2ApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideBaseApiService(@Named("base") client: OkHttpClient): BaseApiService {
        val retrofit = Retrofit.Builder().baseUrl("https://nstu.ru/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(BaseApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideYourNetiApiService(@Named("your_neti") client: OkHttpClient): YourNetiApiService {
        val retrofit = Retrofit.Builder().baseUrl("https://api.ciu.nstu.ru/v2.0/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(YourNetiApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCiuApiService(@Named("ciu") client: OkHttpClient): CiuApiService {
        val retrofit = Retrofit.Builder().baseUrl("https://ciu.nstu.ru/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(CiuApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideCiuRemoteDataSource(
        login2ApiService: Login2ApiService,
        ciuApiService: CiuApiService,
        baseApiService: BaseApiService,
        @Named("ciu") ciuSessionCookieJar: SessionCookieJar,
    ): CiuRemoteDataSource {
        return CiuRemoteDataSourceImpl (
            login2ApiService,
            ciuApiService,
            baseApiService,
            ciuSessionCookieJar
        )
    }

    @Provides
    @Singleton
    fun provideYourNetiRemoteDataSource(
        yourNetiApiService: YourNetiApiService,
        @Named("your_neti") yourNetiSessionCookieJar: SessionCookieJar,
    ): YourNetiRemoteDataSource {
        return YourNetiRemoteDataSourceImpl(
            yourNetiApiService,
            yourNetiSessionCookieJar
        )
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        ciuRemoteDataSource: CiuRemoteDataSource,
        yourNetiRemoteDataSource: YourNetiRemoteDataSource,
    ): RemoteDataSource {
        return RemoteDataSourceImpl(
            ciuRemoteDataSource,
            yourNetiRemoteDataSource
        )
    }
}
