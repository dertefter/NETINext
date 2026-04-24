package com.dertefter.data.datasource.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.local.room.entity.NewsEntity
import com.dertefter.data.datasource.local.room.entity.NewsRemoteKey

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val pageSize: Int
) : RemoteMediator<Int, NewsEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        return try {
            remoteDataSource.getNewsForPage(page).fold(
                onSuccess = { news ->
                    val endOfPaginationReached = news.isEmpty()
                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    
                    val keys = news.map {
                        NewsRemoteKey(newsId = it.id, prevKey = prevKey, nextKey = nextKey)
                    }

                    localDataSource.saveNewsPage(
                        news = news,
                        keys = keys,
                        isRefresh = loadType == LoadType.REFRESH,
                        page = page,
                        pageSize = pageSize
                    )

                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                },
                onFailure = { MediatorResult.Error(it) }
            )
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, NewsEntity>): NewsRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { news -> localDataSource.getRemoteKeyForNewsId(news.id) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, NewsEntity>
    ): NewsRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { newsId ->
                localDataSource.getRemoteKeyForNewsId(newsId)
            }
        }
    }
}
