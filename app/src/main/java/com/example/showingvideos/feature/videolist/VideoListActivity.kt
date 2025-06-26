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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.showingvideos.R
import com.example.showingvideos.feature.videolist.screen.EmptyState
import com.example.showingvideos.feature.videolist.screen.PixelDisclaimer
import com.example.showingvideos.feature.videolist.screen.PlayingQualityBottomSheetContent
import com.example.showingvideos.feature.videolist.screen.SearchBar
import com.example.showingvideos.feature.videolist.screen.VideoListScreen
import com.example.showingvideos.library.uicommon.ErrorScreen
import com.example.showingvideos.library.uicommon.ErrorSnackBar
import com.example.showingvideos.library.uicommon.LoadingScreen
import com.example.showingvideos.library.uicommon.SnackbarState
import com.example.showingvideos.ui.theme.ShowingVideosTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoListActivity : ComponentActivity() {
    private val viewModel: VideoListViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uriHandler = LocalUriHandler.current

            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

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

            var openBottomSheet by rememberSaveable { mutableStateOf(false) }
            val bottomSheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )

            ShowingVideosTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { openBottomSheet = true }) {
                            Icon(Icons.Filled.Settings, stringResource(R.string.settings_button))
                        }
                    }
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
                            is VideoListDisplayState.Loading -> LoadingScreen()
                            is VideoListDisplayState.ShowList -> {
                                if (currentState.videos.isNotEmpty()) {
                                    VideoListScreen(
                                        videos = currentState.videos,
                                        onRefresh = { viewModel.onEvent(VideoListEvent.Refresh) },
                                        onLoadMore = { viewModel.onEvent(VideoListEvent.LoadNextPage) },
                                        isRefreshing = isRefreshing,
                                        isLoadingMore = isLoadingMore,
                                        resetListView = currentState.resetListView,
                                        playingQuality = currentState.playingQuality,
                                    )
                                } else {
                                    EmptyState(message = stringResource(R.string.no_results))
                                }
                            }

                            is VideoListDisplayState.Empty -> EmptyState()
                        }
                    }
                }
                if (openBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { openBottomSheet = false },
                        sheetState = bottomSheetState,
                    ) {
                        PlayingQualityBottomSheetContent(
                            selectedQuality = state.playingQuality,
                            onQualitySelected = { quality ->
                                viewModel.onEvent(VideoListEvent.SetPlayerQuality(quality))
                                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                    if (!bottomSheetState.isVisible) {
                                        openBottomSheet = false
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

