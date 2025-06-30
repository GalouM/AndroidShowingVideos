package com.example.showingvideos.feature.videolist

import com.example.showingvideos.library.fetchingvideo.FetchVideoRepository
import com.example.showingvideos.library.fetchingvideo.local.model.Id
import com.example.showingvideos.library.fetchingvideo.local.model.Video
import com.example.showingvideos.library.uimodels.VideoUi
import com.example.showingvideos.library.uimodels.mapper.VideoMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

internal class FetchVideoListUseCase @Inject constructor(
    private val repository: FetchVideoRepository,
    private val videoMapper: VideoMapper
) {

    private val defaultState = FetchVideoState.Initialized

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
        lastRequest = lastRequest?.copy(
            page = STARTING_PAGE,
            type = RequestType.REFRESH
        )
    }

    fun loadNextPage() {
        lastRequest?.let { previousState ->
            lastRequest = previousState.copy(
                page = previousState.page + 1,
                type = RequestType.NEXT_PAGE
            )
        }
    }

    fun setQuery(query: String) {
        lastRequest = FetchVideoRequest(
            query = query,
            type = RequestType.FIRST_PAGE
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: Flow<FetchVideoState> = requestChannel.receiveAsFlow()
        .flatMapLatest { request ->
            flow {
                val loadingState = when (request.type) {
                    RequestType.FIRST_PAGE -> FetchVideoState.FirstLoading
                    RequestType.NEXT_PAGE -> FetchVideoState.LoadingMore
                    RequestType.REFRESH -> FetchVideoState.Refreshing
                }
                emit(loadingState)

                emit(fetchVideoList(request))
            }
        }
        .onStart { emit(defaultState) }

    private suspend fun fetchVideoList(request: FetchVideoRequest): FetchVideoState =
        when (val result = repository.getVideoList(request.query, PAGE_SIZE, request.page)) {
            is FetchVideoRepository.FetchVideoList.Success -> {
                val pageAlreadyFetched = checkIfPageAlreadyFetched(result.videos, request.resetResults)
                lastVideoFetchedId = result.videos.lastOrNull()?.id
                val newVideos = result.videos.takeUnless { pageAlreadyFetched } ?: emptyList()
                FetchVideoState.Success(
                    videos = videoMapper.map(newVideos),
                    canLoadMore = canLoadMore(result.videos, pageAlreadyFetched),
                    resultReset = request.resetResults
                )
            }

            is FetchVideoRepository.FetchVideoList.Error -> {
                val isRetryable = result.error is HttpException || result.error is UnknownHostException
                if (request.type == RequestType.FIRST_PAGE) {
                    FetchVideoState.FirstPageError(isRetryable)
                } else {
                    FetchVideoState.NextPageError(isRetryable)
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

    private fun checkIfPageAlreadyFetched(videos: List<Video>, resultReset: Boolean): Boolean {
        return !resultReset && videos.isNotEmpty() && videos.map { it.id }.contains(lastVideoFetchedId)
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val STARTING_PAGE = 1
    }

    sealed interface FetchVideoState {
        data class Success(
            val videos: List<VideoUi>,
            val canLoadMore: Boolean,
            val resultReset: Boolean
        ) : FetchVideoState
        data class FirstPageError(val isRetryableError: Boolean) : FetchVideoState
        data class NextPageError(val isRetryableError: Boolean) : FetchVideoState
        data object FirstLoading : FetchVideoState
        data object Refreshing : FetchVideoState
        data object LoadingMore : FetchVideoState
        data object Initialized : FetchVideoState
    }

    data class FetchVideoRequest(
        val query: String,
        val page: Int = STARTING_PAGE,
        val type: RequestType = RequestType.FIRST_PAGE
    ) {
        val resetResults: Boolean get() =
            type == RequestType.REFRESH || type == RequestType.FIRST_PAGE
    }
    enum class RequestType {
        FIRST_PAGE,
        NEXT_PAGE,
        REFRESH
    }
}