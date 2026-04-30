package com.dertefter.news_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.news_detail.presentation.Event
import com.dertefter.news_detail.presentation.NewsState
import com.dertefter.data.common.toAppError
import com.dertefter.news_detail.usecase.FetchNewsDetailUseCase
import com.dertefter.news_detail.usecase.NavigateBackUseCase
import com.dertefter.news_detail.usecase.NavigateImageViewerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val fetchNewsDetailUseCase: FetchNewsDetailUseCase,
    private val navigateBackUseCase: NavigateBackUseCase,
    private val navigateImageViewerUseCase: NavigateImageViewerUseCase,
) : ViewModel() {

    private val _newsState = MutableStateFlow(NewsState())
    val newsState: StateFlow<NewsState> = _newsState.asStateFlow()

    var newsId: String? = null


    fun initWith(
        newsId: String,
    ){
        this.newsId = newsId
        loadNewsDetail(newsId)
    }


    fun onEvent(event: Event) {
        when (event) {
            is Event.RequestLoadingNewsDetail -> {
                newsId?.let{
                    loadNewsDetail(it)
                }

            }
            is Event.OnNavigateBack -> {
                navigateBackUseCase()
            }

            is Event.OnNavigateToImageViewer -> {
                navigateImageViewerUseCase(
                    imageUrls = event.imageUrls,
                    viewPosition = event.viewPosition
                )
            }

        }
    }

    private fun loadNewsDetail(newsId: String){
        viewModelScope.launch {
            _newsState.update { it.copy(isLoading = true, error = null) }
            fetchNewsDetailUseCase(newsId).onSuccess { newsDetailDto ->
                _newsState.update { it.copy(newsDetailDto = newsDetailDto, isLoading = false) }
            }.onFailure { error ->
                _newsState.update { it.copy(error = error.toAppError(), isLoading = false) }
            }
        }
    }
}
