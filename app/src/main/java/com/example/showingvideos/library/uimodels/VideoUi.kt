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
) {

    fun getVideoUrlLowestQuality(): String? {
        return videoFiles[VideoQuality.SMALL] ?:
        videoFiles[VideoQuality.MEDIUM] ?:
        videoFiles[VideoQuality.LARGE]
    }

    fun getVideoUrlHighestQuality(): String? {
        return videoFiles[VideoQuality.LARGE] ?:
        videoFiles[VideoQuality.MEDIUM] ?:
        videoFiles[VideoQuality.SMALL]
    }

    fun getVideoHlsUrl(): String? {
        return videoFiles[VideoQuality.HLS]
    }
}

enum class VideoQuality {
    SMALL, MEDIUM, LARGE, HLS
}

enum class SoundState {
    MUTED, UNMUTED
}