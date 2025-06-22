package com.example.showingvideos.feature.videolist

import com.example.showingvideos.library.fetchingvideo.local.model.Video

sealed interface VideoListDisplayState {
    data object Loading : VideoListDisplayState
    data class Success(val videos: List<Video>) : VideoListDisplayState
    data class Error(val isRetryable: Boolean, val errorMessage: String? = null) : VideoListDisplayState
    data object NextPageFailed : VideoListDisplayState
    data object Empty : VideoListDisplayState
}

sealed interface VideoListEvent {
    data class SetQuery(val query: String) : VideoListEvent
    data object Refresh : VideoListEvent
    data object LoadNextPage : VideoListEvent

}