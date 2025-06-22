package com.example.showingvideos.feature.videolist.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.showingvideos.R
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

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.video_image_placeholder_description),
            contentScale = ContentScale.Crop,
            modifier = modifier.matchParentSize()
        )

        if (shouldPlay) {
            Icon(
                painter = when (soundState) {
                    SoundState.MUTED -> painterResource(R.drawable.mute_icon)
                    SoundState.UNMUTED -> painterResource(R.drawable.speaker_icon)
                },
                contentDescription = stringResource(R.string.speaker_icon_description),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clickable { onMuteClicked(soundState) },
                tint = MaterialTheme.colorScheme.onSurface,
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
            onMuteClicked = {}
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
            onMuteClicked = {}
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
            onMuteClicked = {}
        )
    }
}