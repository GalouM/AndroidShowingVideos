package com.example.showingvideos.library.fetchingvideo.remote.apimodel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoListApi(
    @Json(name = "page")
    val page: Int,
    @Json(name = "per_page")
    val perPage: Int,
    @Json(name = "total_results")
    val totalResults: Int,
    @Json(name = "url")
    val url: String,
    @Json(name = "videos")
    val videos: List<VideoApi>
)