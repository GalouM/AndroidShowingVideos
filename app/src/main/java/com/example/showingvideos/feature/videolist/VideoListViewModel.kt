package com.example.showingvideos.feature.videolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showingvideos.library.fetchingvideo.local.model.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class VideoListViewModel @Inject constructor(
    private val fetchVideoListUseCase: FetchVideoListUseCase
) : ViewModel() {

    private var canLoadMore: Boolean = true
    private val _isRefreshing = MutableStateFlow(false)
    private val _isLoadingMore = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val videos: MutableList<Video> = mutableListOf()

    val displayState: StateFlow<VideoListDisplayState> =
        fetchVideoListUseCase.state.map { state ->
            _isRefreshing.value = false
            _isLoadingMore.value = false
            when (state) {
                is FetchVideoListUseCase.FetchVideoState.Success -> {
                    canLoadMore = state.canLoadMore
                    videos += state.videos
                    VideoListDisplayState.Success(videos)
                }
                is FetchVideoListUseCase.FetchVideoState.Error -> {
                    if (state.isFirstPage) {
                        VideoListDisplayState.Error(isRetryable = false)
                    } else {
                        VideoListDisplayState.NextPageFailed
                    }
                }
                is FetchVideoListUseCase.FetchVideoState.RetryableError -> {
                    if (state.isFirstPage) {
                        VideoListDisplayState.Error(isRetryable = true)
                    } else {
                        VideoListDisplayState.NextPageFailed
                    }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = VideoListDisplayState.Empty
        )

    fun onEvent(event: VideoListEvent) {
        when (event) {
            is VideoListEvent.SetQuery -> fetchVideoListUseCase.setQuery(event.query)
            VideoListEvent.Refresh -> {
                if (!_isRefreshing.value) {
                    _isRefreshing.value = true
                    fetchVideoListUseCase.refresh()
                }
            }
            VideoListEvent.LoadNextPage -> {
                if (canLoadMore && !_isLoadingMore.value) {
                    _isLoadingMore.value = true
                    fetchVideoListUseCase.loadNextPage()
                }
            }
        }
    }

}