package com.example.showingvideos.library.fetchingvideo.remote.apimodel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoPictureApi(
    @Json(name = "id")
    val id: Int,
    @Json(name = "nr")
    val nr: Int,
    @Json(name = "picture")
    val picture: String
)