package com.example.showingvideos.library.fetchingvideo

import android.util.Log
import com.example.showingvideos.library.fetchingvideo.di.IoDispatcher
import com.example.showingvideos.library.fetchingvideo.local.VideoMapper
import com.example.showingvideos.library.fetchingvideo.local.model.Video
import com.example.showingvideos.library.fetchingvideo.remote.VideoListRemoteSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class FetchVideoRepository @Inject constructor(
    private val remoteSource: VideoListRemoteSource,
    private val mapper: VideoMapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) {

    suspend fun getVideoList(query: String, limit: Int, page: Int): FetchVideoList = withContext(dispatcher) {
        try {
            val remoteVideoList = remoteSource.getVideoList(query, limit, page)
            FetchVideoList.Success(mapper.apiModelToLocal(remoteVideoList))
        } catch (e: Exception) {
            if (e is HttpException) {
                Log.e("FetchVideoRepository", "Error fetching video list: ${e.response()}")
            }
            Log.e("FetchVideoRepository", "Error fetching video list", e)
            FetchVideoList.Error(e)
        }
    }

    sealed interface FetchVideoList {
        data class Success(val videos: List<Video>) : FetchVideoList
        data class Error(val error: Throwable) : FetchVideoList
    }
}