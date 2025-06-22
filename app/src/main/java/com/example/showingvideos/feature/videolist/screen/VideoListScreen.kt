package com.example.showingvideos.feature.videolist.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.showingvideos.library.uicommon.fakeVideoUiList
import com.example.showingvideos.library.uicommon.reachedBottom
import com.example.showingvideos.library.uicommon.firstVisibleItem
import com.example.showingvideos.library.uimodels.SoundState
import com.example.showingvideos.library.uimodels.VideoUi
import com.example.showingvideos.ui.theme.ShowingVideosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun VideoListScreen(
    videos: List<VideoUi>,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val lazyListState = rememberLazyListState()

    val reachedBottom: Boolean by remember {
        derivedStateOf { lazyListState.reachedBottom(buffer = 3) }
    }

    var soundState by remember { mutableStateOf(SoundState.UNMUTED) }

    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            onLoadMore()
        }
    }

    val centeredItemIndex by remember {
        derivedStateOf { lazyListState.firstVisibleItem() }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = pullToRefreshState
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(
                items = videos,
                key = { _, video -> video.id.value }
            ) { index, video ->
                VideoItemView(
                    video = video,
                    shouldPlay = index == centeredItemIndex,
                    soundState = soundState,
                    onMuteClicked = { currentSoundState ->
                        soundState = when (currentSoundState) {
                            SoundState.MUTED -> SoundState.UNMUTED
                            SoundState.UNMUTED -> SoundState.MUTED
                        }

                    }
                )
            }

            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VideoListScreenPreviewWithFakeData() {
    ShowingVideosTheme {
        VideoListScreen(
            videos = fakeVideoUiList,
            onRefresh = {},
            onLoadMore = {},
            isRefreshing = false,
            isLoadingMore = false
        )
    }
}