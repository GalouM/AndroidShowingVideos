package com.example.showingvideos.feature.videolist.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.showingvideos.R
import com.example.showingvideos.library.fetchingvideo.local.model.Video

@Composable
internal fun VideoItemView(
    video: Video,
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
        val imageUrl = video.videoPictures.firstOrNull()?.picture

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.video_image_placeholder_description),
            contentScale = ContentScale.Crop,
            modifier = modifier.matchParentSize()
        )

        Icon(
            painter = painterResource(R.drawable.mute_icon),
            contentDescription = stringResource(R.string.speaker_icon_description),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

private const val VIDEO_MAX_HEIGHT = 400