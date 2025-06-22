package com.example.showingvideos.library.uimodels

import com.example.showingvideos.library.fetchingvideo.local.model.Duration
import com.example.showingvideos.library.fetchingvideo.local.model.Height
import com.example.showingvideos.library.fetchingvideo.local.model.Id
import com.example.showingvideos.library.fetchingvideo.local.model.Width

data class VideoUi(
    val id: Id,
    val height: Height,
    val width: Width,
    val duration: Duration,
    val image: String?,
    val videoFiles: Map<VideoQuality, String>,
    val shouldPlay: Boolean = false
)

enum class VideoQuality {
    SMALL, MEDIUM, LARGE, HLS
}

enum class SoundState {
    MUTED, UNMUTED
}