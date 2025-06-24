package com.example.showingvideos.library.uicommon

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.showingvideos.R
import com.example.showingvideos.library.uimodels.SoundState
import com.example.showingvideos.library.uimodels.VideoUi

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    video: VideoUi,
    soundState: SoundState,
    onMuteClicked: (soundSate: SoundState) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
        }
    }
    val highestVideoUrl = video.getVideoUrlHighestQuality()
    val mediaSource = remember(highestVideoUrl) {
        MediaItem.fromUri(highestVideoUrl.orEmpty())
    }

    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    LaunchedEffect(soundState) {
        when (soundState) {
            SoundState.MUTED -> exoPlayer.volume = 0f
            SoundState.UNMUTED -> exoPlayer.volume = 1f
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    setShowFastForwardButton(false)
                    setShowRewindButton(false)
                }
            },
            modifier = Modifier.matchParentSize()
        )

        Icon(
            painter = when (soundState) {
                SoundState.MUTED -> painterResource(R.drawable.mute_icon)
                SoundState.UNMUTED -> painterResource(R.drawable.speaker_icon)
            },
            contentDescription = when (soundState) {
                SoundState.MUTED -> stringResource(R.string.speaker_icon_unmute_description)
                SoundState.UNMUTED -> stringResource(R.string.speaker_icon_mute_description)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(24.dp)
                .clickable { onMuteClicked(soundState) },
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}