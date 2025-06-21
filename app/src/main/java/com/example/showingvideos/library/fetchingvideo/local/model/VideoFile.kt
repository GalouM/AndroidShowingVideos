package com.example.showingvideos.library.fetchingvideo.local.model

data class VideoFile(
    val fileType: String,
    val height: Height,
    val width: Width,
    val id: Id,
    val link: String,
    val quality: String
)