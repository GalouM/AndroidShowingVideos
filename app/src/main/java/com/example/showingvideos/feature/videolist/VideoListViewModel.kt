package com.example.showingvideos.feature.videolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showingvideos.library.uicommon.SnackbarState
import com.example.showingvideos.library.uimodels.VideoUi
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
    private val snackbarMessage = MutableStateFlow<SnackbarState?>(null)

    private val videos: MutableList<VideoUi> = mutableListOf()

    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore
    val snackbarState: StateFlow<SnackbarState?> = snackbarMessage

    val displayState: StateFlow<VideoListDisplayState> =
        fetchVideoListUseCase.state.map { state ->
            _isRefreshing.value = false
            _isLoadingMore.value = false
            when (state) {
                is FetchVideoListUseCase.FetchVideoState.Success -> {
                    if (state.resultReset) videos.clear()
                    canLoadMore = state.canLoadMore
                    videos += state.videos
                    VideoListDisplayState.ShowList(
                        videos = videos,
                        resetListView = state.resultReset
                    )
                }
                is FetchVideoListUseCase.FetchVideoState.FirstPageError -> {
                    VideoListDisplayState.Error(isRetryable = state.isRetryableError)
                }
                is FetchVideoListUseCase.FetchVideoState.NextPageError -> {
                    snackbarMessage.value = SnackbarState.Error()
                    VideoListDisplayState.ShowList(
                        videos = videos,
                        resetListView = false
                    )
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