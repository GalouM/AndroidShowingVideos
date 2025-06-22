package com.example.showingvideos.feature.videolist

import com.example.showingvideos.library.uimodels.SoundState
import com.example.showingvideos.library.uimodels.VideoUi

sealed interface VideoListDisplayState {
    data object Loading : VideoListDisplayState
    data class Success(val videos: List<VideoUi>) : VideoListDisplayState
    data class Error(val isRetryable: Boolean, val errorMessage: String? = null) : VideoListDisplayState
    data object NextPageFailed : VideoListDisplayState
    data object Empty : VideoListDisplayState
}

sealed interface VideoListEvent {
    data class SetQuery(val query: String) : VideoListEvent
    data object Refresh : VideoListEvent
    data object LoadNextPage : VideoListEvent

}