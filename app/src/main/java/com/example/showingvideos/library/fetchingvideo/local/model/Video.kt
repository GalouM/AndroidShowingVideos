package com.example.showingvideos.library.fetchingvideo.local.model

data class Video(
    val duration: Duration,
    val height: Height,
    val width: Width,
    val id: Id,
    val image: String?,
    val url: String?,
    val user: User?,
    val videoFiles: List<VideoFile>,
    val videoPictures: List<VideoPicture>,
)

@JvmInline
value class Height(val value: Int)
@JvmInline
value class Width(val value: Int)
@JvmInline
value class Id(val value: Int)
@JvmInline
value class Duration(val value: Long)