package com.example.showingvideos.library.fetchingvideo.remote

import com.example.showingvideos.library.fetchingvideo.remote.apimodel.VideoListApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoListRemoteSource {

    @GET("/videos/search")
    suspend fun getVideoList(
        @Query("query") query: String,
        @Query("per_page") limit: Int,
        @Query("page") page: Int,
    ): VideoListApi

}