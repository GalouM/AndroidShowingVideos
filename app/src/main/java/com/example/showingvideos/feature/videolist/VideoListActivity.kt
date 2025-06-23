package com.example.showingvideos.feature.videolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.showingvideos.R
import com.example.showingvideos.feature.videolist.screen.EmptyState
import com.example.showingvideos.library.uicommon.ErrorScreen
import com.example.showingvideos.library.uicommon.ErrorSnackBar
import com.example.showingvideos.library.uicommon.LoadingScreen
import com.example.showingvideos.feature.videolist.screen.PixelDisclaimer
import com.example.showingvideos.feature.videolist.screen.SearchBar
import com.example.showingvideos.feature.videolist.screen.VideoListScreen
import com.example.showingvideos.library.uicommon.SnackbarState
import com.example.showingvideos.ui.theme.ShowingVideosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: VideoListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uriHandler = LocalUriHandler.current
            //val context = LocalContext.current
            //val player = ExoPlayer.Builder(context).build()

            val snackbarHostState = remember { SnackbarHostState() }

            val state by viewModel.displayState.collectAsStateWithLifecycle()
            val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
            val isLoadingMore by viewModel.isLoadingMore.collectAsStateWithLifecycle()
            val snackbarState by viewModel.snackbarState.collectAsStateWithLifecycle()

            when (snackbarState) {
                is SnackbarState.Error -> {
                    ErrorSnackBar(
                        message = stringResource(R.string.default_error_message),
                        snackbarHostState = snackbarHostState
                    )
                }
                else -> {}
            }

            ShowingVideosTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)) {
                        SearchBar(
                            onSearchClicked = { query ->
                                viewModel.onEvent(VideoListEvent.SetQuery(query))
                            }
                        )
                        PixelDisclaimer(
                            onLinkClicked = { url -> uriHandler.openUri(url) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        )

                        when (val currentState = state) {
                            is VideoListDisplayState.Error -> {
                                ErrorScreen(
                                    showRetryButton = currentState.isRetryable,
                                    onRetryClicked = { viewModel.onEvent(VideoListEvent.Refresh) }
                                )
                            }
                            VideoListDisplayState.Loading -> LoadingScreen()
                            is VideoListDisplayState.ShowList -> {
                                if (currentState.videos.isNotEmpty()) {
                                    VideoListScreen(
                                        videos = currentState.videos,
                                        onRefresh = { viewModel.onEvent(VideoListEvent.Refresh) },
                                        onLoadMore = { viewModel.onEvent(VideoListEvent.LoadNextPage) },
                                        isRefreshing = isRefreshing,
                                        isLoadingMore = isLoadingMore,
                                        resetListView = currentState.resetListView,
                                    )
                                } else {
                                    EmptyState(message = stringResource(R.string.no_results))
                                }
                            }

                            VideoListDisplayState.Empty -> EmptyState()
                        }
                    }
                }
            }
        }
    }
}

