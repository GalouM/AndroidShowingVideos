package com.example.showingvideos.library.uimodels.mapper

import com.example.showingvideos.library.fetchingvideo.local.model.Video
import com.example.showingvideos.library.uimodels.VideoQuality
import com.example.showingvideos.library.uimodels.VideoUi
import javax.inject.Inject

class VideoMapper @Inject constructor() {

    fun map(video: Video): VideoUi {
        val videoFiles = video.videoFiles.filter { it.link != null }.associate {
            when (it.quality) {
                "sd", "small" -> VideoQuality.SMALL to it.link.orEmpty()
                "hd", "medium" -> VideoQuality.MEDIUM to it.link.orEmpty()
                "uhd", "large" -> VideoQuality.LARGE to it.link.orEmpty()
                "hls" -> VideoQuality.HLS to it.link.orEmpty()
                else -> VideoQuality.SMALL to it.link.orEmpty()
            }
        }
        return VideoUi(
            id = video.id,
            height = video.height,
            width = video.width,
            duration = video.duration,
            image = video.image,
            videoFiles = videoFiles
        )
    }

    fun map(videos: List<Video>): List<VideoUi> {
        return videos.map { map(it) }
    }
}