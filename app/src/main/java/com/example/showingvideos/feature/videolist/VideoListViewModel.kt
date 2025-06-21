package com.example.showingvideos.feature.videolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showingvideos.library.fetchingvideo.FetchVideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val fetchVideoListUseCase: FetchVideoListUseCase
) : ViewModel() {

    val displayState: StateFlow<VideoListDisplayState> =
        fetchVideoListUseCase.state.map {
            when (it) {
                is FetchVideoListUseCase.FetchVideoState.Success -> VideoListDisplayState.Success(it.videos)
                is FetchVideoListUseCase.FetchVideoState.Error -> VideoListDisplayState.Error(isRetryable = false)
                FetchVideoListUseCase.FetchVideoState.RetryableError -> VideoListDisplayState.Error(isRetryable = true)
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = VideoListDisplayState.Loading
        )

    fun onEvent(event: VideoListEvent) {
        when (event) {
            is VideoListEvent.SetQuery -> fetchVideoListUseCase.setQuery(event.query)
            VideoListEvent.Refresh -> fetchVideoListUseCase.refresh()
            VideoListEvent.LoadNextPage -> fetchVideoListUseCase.loadNextPage()
        }
    }

}