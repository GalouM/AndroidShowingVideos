package com.example.showingvideos.library.fetchingvideo.remote.apimodel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoFileApi(
    @Json(name = "file_type")
    val fileType: String,
    @Json(name = "height")
    val height: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "link")
    val link: String,
    @Json(name = "quality")
    val quality: String,
    @Json(name = "width")
    val width: Int
)