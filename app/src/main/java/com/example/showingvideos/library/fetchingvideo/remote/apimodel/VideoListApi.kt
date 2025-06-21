package com.example.showingvideos.library.fetchingvideo.remote.apimodel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoListApi(
    @Json(name = "videos")
    val videos: List<VideoApi>
)