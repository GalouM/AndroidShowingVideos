package com.example.showingvideos.feature.videolist

import com.example.showingvideos.library.fetchingvideo.FetchVideoRepository
import com.example.showingvideos.library.fetchingvideo.local.model.Video
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class FetchVideoListUseCase @Inject constructor(
    private val repository: FetchVideoRepository
) {
    private var lastRequest: FetchVideoRequest? = null
        set(value) {
            field = value
            if (value != null) {
                requestChannel.trySend(value)
            }
        }

    private val requestChannel = Channel<FetchVideoRequest>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    fun refresh() {
        lastRequest = lastRequest?.copy(page = STARTING_PAGE)
    }

    fun loadNextPage() {
        lastRequest?.let { previousState ->
            lastRequest = previousState.copy(page = previousState.page + 1)
        }
    }

    fun setQuery(query: String) {
        lastRequest = FetchVideoRequest(query)
    }

    val state: Flow<FetchVideoState> = requestChannel.consumeAsFlow().map {
        fetchVideoList(it.page, it.query)
    }

    private suspend fun fetchVideoList(page: Int, query: String): FetchVideoState =
        when (val result = repository.getVideoList(query, PAGE_SIZE, page)) {
            is FetchVideoRepository.FetchVideoList.Success -> FetchVideoState.Success(result.videos)
            is FetchVideoRepository.FetchVideoList.Error -> {
                if (result.error is HttpException) {
                    FetchVideoState.RetryableError
                } else {
                    FetchVideoState.Error
                }
            }
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val STARTING_PAGE = 1
    }

    sealed interface FetchVideoState {
        data class Success(val videos: List<Video>) : FetchVideoState
        data object Error : FetchVideoState
        data object RetryableError : FetchVideoState
    }

    data class FetchVideoRequest(
        val query: String,
        val page: Int = STARTING_PAGE
    )
}