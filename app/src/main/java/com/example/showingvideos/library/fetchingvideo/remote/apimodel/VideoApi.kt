package com.example.showingvideos.library.fetchingvideo.remote.apimodel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoApi(
    @Json(name = "duration")
    val duration: Int,
    @Json(name = "height")
    val height: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "image")
    val image: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "user")
    val user: UserApi,
    @Json(name = "video_files")
    val videoFiles: List<VideoFileApi>,
    @Json(name = "video_pictures")
    val videoPictures: List<VideoPictureApi>,
    @Json(name = "width")
    val width: Int
)