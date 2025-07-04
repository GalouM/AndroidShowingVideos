package com.example.showingvideos.feature.videolist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.showingvideos.R
import com.example.showingvideos.feature.videolist.PlayingQuality
import com.example.showingvideos.library.uicommon.VideoPlayer
import com.example.showingvideos.library.uicommon.sampleTallVideo
import com.example.showingvideos.library.uicommon.sampleVideoZeroHeight
import com.example.showingvideos.library.uimodels.SoundState
import com.example.showingvideos.library.uimodels.VideoUi
import com.example.showingvideos.ui.theme.ShowingVideosTheme

@Composable
internal fun VideoItemView(
    video: VideoUi,
    shouldPlay: Boolean,
    soundState: SoundState,
    onMuteClicked: (soundSate: SoundState) -> Unit,
    playingQuality: PlayingQuality,
    modifier: Modifier = Modifier,
    maxHeight: Dp = VIDEO_MAX_HEIGHT.dp
) {
    val aspectRatio = video.width.value.toFloat() / video.height.value.toFloat()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)
            .aspectRatio(aspectRatio, matchHeightConstraintsFirst = true)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        val imageUrl = video.image

        if (shouldPlay) {
            VideoPlayer(
                video = video,
                soundState = soundState,
                onMuteClicked = onMuteClicked,
                modifier = Modifier.matchParentSize(),
                playingQuality = playingQuality
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.video_image_placeholder_description),
                contentScale = ContentScale.Crop,
                modifier = modifier.matchParentSize()
            )
        }
    }
}

private const val VIDEO_MAX_HEIGHT = 400

@Preview(name = "VideoItemView Tall Constrained", showBackground = true)
@Composable
private fun VideoItemViewTallConstrainedPreview() {
    ShowingVideosTheme {
        VideoItemView(
            video = sampleTallVideo,
            modifier = Modifier.padding(8.dp),
            maxHeight = 250.dp,
            shouldPlay = true,
            soundState = SoundState.UNMUTED,
            playingQuality = PlayingQuality.Lowest,
            onMuteClicked = {}
        )
    }
}

@Preview(name = "VideoItemView Muted", showBackground = true)
@Composable
private fun VideoItemViewMutedPreview() {
    ShowingVideosTheme {
        VideoItemView(
            video = sampleTallVideo,
            modifier = Modifier.padding(8.dp),
            maxHeight = 250.dp,
            shouldPlay = true,
            soundState = SoundState.MUTED,
            onMuteClicked = {},
            playingQuality = PlayingQuality.Lowest,
        )
    }
}

@Preview(name = "VideoItemView Not Playing", showBackground = true)
@Composable
private fun VideoItemViewNotPlayingPreview() {
    ShowingVideosTheme {
        VideoItemView(
            video = sampleTallVideo,
            modifier = Modifier.padding(8.dp),
            maxHeight = 250.dp,
            shouldPlay = false,
            soundState = SoundState.UNMUTED,
            onMuteClicked = {},
            playingQuality = PlayingQuality.Lowest,
        )
    }
}

@Preview(name = "VideoItemView Zero Height (Fallback)", showBackground = true)
@Composable
private fun VideoItemViewZeroHeightPreview() {
    ShowingVideosTheme {
        VideoItemView(
            video = sampleVideoZeroHeight,
            modifier = Modifier.padding(8.dp),
            shouldPlay = true,
            soundState = SoundState.UNMUTED,
            onMuteClicked = {},
            playingQuality = PlayingQuality.Lowest,
        )
    }
}