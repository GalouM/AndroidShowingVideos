package com.example.showingvideos.feature.videolist

import com.example.showingvideos.library.fetchingvideo.FetchVideoRepository
import com.example.showingvideos.library.fetchingvideo.local.model.Id
import com.example.showingvideos.library.fetchingvideo.local.model.Video
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import retrofit2.HttpException
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

internal class FetchVideoListUseCase @Inject constructor(
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

    private var lastVideoFetchedId: Id? = null

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

    val state: Flow<FetchVideoState> = requestChannel.receiveAsFlow().map {
        fetchVideoList(it.page, it.query)
    }

    private suspend fun fetchVideoList(page: Int, query: String): FetchVideoState =
        when (val result = repository.getVideoList(query, PAGE_SIZE, page)) {
            is FetchVideoRepository.FetchVideoList.Success -> {
                val pageAlreadyFetched = checkIfPageAlreadyFetched(result.videos)
                lastVideoFetchedId = result.videos.lastOrNull()?.id
                FetchVideoState.Success(
                    videos = result.videos.takeUnless { pageAlreadyFetched } ?: emptyList(),
                    canLoadMore = canLoadMore(result.videos, pageAlreadyFetched)
                )
            }

            is FetchVideoRepository.FetchVideoList.Error -> {
                if (result.error is HttpException) {
                    FetchVideoState.RetryableError(page == STARTING_PAGE)
                } else {
                    FetchVideoState.Error(page == STARTING_PAGE)
                }
            }
    }

    /**
     * Currently the API used to fetch videos doesn't tell us if we're at the end of the list or not
     * so we have to check manually.
     */
    private fun canLoadMore(videos: List<Video>, pageAlreadyFetched: Boolean): Boolean {
        return videos.size == PAGE_SIZE || !pageAlreadyFetched
    }

    private fun checkIfPageAlreadyFetched(videos: List<Video>): Boolean {
        return videos.map { it.id }.contains(lastVideoFetchedId)
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val STARTING_PAGE = 1
    }

    sealed interface FetchVideoState {
        data class Success(val videos: List<Video>, val canLoadMore: Boolean) : FetchVideoState
        data class Error(val isFirstPage: Boolean) : FetchVideoState
        data class RetryableError(val isFirstPage: Boolean) : FetchVideoState
    }

    data class FetchVideoRequest(
        val query: String,
        val page: Int = STARTING_PAGE
    )
}