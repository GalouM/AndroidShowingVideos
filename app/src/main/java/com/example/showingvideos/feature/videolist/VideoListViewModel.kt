package com.example.showingvideos.feature.videolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showingvideos.library.uicommon.SnackbarState
import com.example.showingvideos.library.uimodels.VideoUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class VideoListViewModel @Inject constructor(
    private val fetchVideoListUseCase: FetchVideoListUseCase,
    private val setPlayerSettingsUseCase: SetPlayerSettingsUseCase,
) : ViewModel() {

    private var canLoadMore: Boolean = true

    private val _snackbarMessage = MutableStateFlow<SnackbarState?>(null)

    private val videos: MutableList<VideoUi> = mutableListOf()
    val snackbarState: StateFlow<SnackbarState?> = _snackbarMessage

    val displayState: StateFlow<VideoListDisplayState> =
        combine(fetchVideoListUseCase.state,
            setPlayerSettingsUseCase.state,
        ) { videosState, playerState->
            when (videosState) {
                is FetchVideoListUseCase.FetchVideoState.Success -> {
                    if (videosState.resultReset) videos.clear()
                    canLoadMore = videosState.canLoadMore
                    videos += videosState.videos
                    VideoListDisplayState.ShowList(
                        videos = videos,
                        resetListView = videosState.resultReset,
                        playingQuality = playerState.playingQuality,
                        isRefreshing = false,
                        isLoadingMore = false
                    )
                }
                is FetchVideoListUseCase.FetchVideoState.FirstPageError -> {
                    VideoListDisplayState.Error(
                        isRetryable = videosState.isRetryableError,
                        playingQuality = playerState.playingQuality,
                    )
                }
                is FetchVideoListUseCase.FetchVideoState.NextPageError -> {
                    _snackbarMessage.value = SnackbarState.Error()
                    VideoListDisplayState.ShowList(
                        videos = videos,
                        resetListView = false,
                        playingQuality = playerState.playingQuality,
                        isRefreshing = false,
                        isLoadingMore = false

                    )
                }

                FetchVideoListUseCase.FetchVideoState.FirstLoading ->
                    VideoListDisplayState.Loading(playerState.playingQuality)

                FetchVideoListUseCase.FetchVideoState.LoadingMore ->
                    VideoListDisplayState.ShowList(
                        videos = videos,
                        resetListView = false,
                        playingQuality = playerState.playingQuality,
                        isRefreshing = false,
                        isLoadingMore = true

                    )

                FetchVideoListUseCase.FetchVideoState.Refreshing ->
                    VideoListDisplayState.ShowList(
                        videos = videos,
                        resetListView = false,
                        playingQuality = playerState.playingQuality,
                        isRefreshing = true,
                        isLoadingMore = false

                    )

                FetchVideoListUseCase.FetchVideoState.Initialized ->
                    VideoListDisplayState.Empty(playingQuality = playerState.playingQuality)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = VideoListDisplayState.Empty(playingQuality = PlayingQuality.Lowest)
        )

    fun onEvent(event: VideoListEvent) {
        when (event) {
            is VideoListEvent.SetQuery -> fetchVideoListUseCase.setQuery(event.query)
            VideoListEvent.Refresh -> {
                fetchVideoListUseCase.refresh()
            }
            VideoListEvent.LoadNextPage -> {
                if (canLoadMore) {
                    fetchVideoListUseCase.loadNextPage()
                }
            }

            is VideoListEvent.SetPlayerQuality ->
                setPlayerSettingsUseCase.setVideoQuality(event.playingQuality)
        }
    }

}