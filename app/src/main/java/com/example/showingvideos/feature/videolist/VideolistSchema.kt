package com.example.showingvideos.feature.videolist

import com.example.showingvideos.library.uimodels.VideoUi

sealed interface VideoListDisplayState {
    val playingQuality: PlayingQuality
    data class Loading(override val playingQuality: PlayingQuality) : VideoListDisplayState
    data class ShowList(
        val videos: List<VideoUi>,
        val resetListView: Boolean,
        override val playingQuality: PlayingQuality,
        val isRefreshing: Boolean,
        val isLoadingMore: Boolean,
    ) : VideoListDisplayState
    data class Error(
        val isRetryable: Boolean,
        override val playingQuality: PlayingQuality,
        val errorMessage: String? = null
    ) : VideoListDisplayState
    data class Empty(override val playingQuality: PlayingQuality) : VideoListDisplayState
}

sealed interface VideoListEvent {
    data class SetQuery(val query: String) : VideoListEvent
    data object Refresh : VideoListEvent
    data object LoadNextPage : VideoListEvent
    data class SetPlayerQuality(val playingQuality: PlayingQuality) : VideoListEvent
}

enum class PlayingQuality {
    Lowest, Highest, HLS
}