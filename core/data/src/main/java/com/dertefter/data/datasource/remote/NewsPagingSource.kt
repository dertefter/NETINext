package com.dertefter.data.datasource.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dertefter.data.dto.news.NewsItem

class NewsPagingSource(
    private val remoteDataSource: RemoteDataSource
) : PagingSource<Int, NewsItem>() {

    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        val page = params.key ?: 1
        return try {
            val result = remoteDataSource.getNewsForPage(page)
            if (result.isSuccess) {
                val news = result.getOrNull() ?: emptyList()
                LoadResult.Page(
                    data = news,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (news.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
