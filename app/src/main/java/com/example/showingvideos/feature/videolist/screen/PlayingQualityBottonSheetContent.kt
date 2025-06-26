package com.example.showingvideos.feature.videolist.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.showingvideos.R
import com.example.showingvideos.feature.videolist.PlayingQuality

@Composable
fun PlayingQualityBottomSheetContent(
    selectedQuality: PlayingQuality,
    onQualitySelected: (PlayingQuality) -> Unit,
    modifier: Modifier = Modifier
) {
    val playingQualities = PlayingQuality.entries
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.select_playing_quality),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn {
            items(playingQualities) { quality ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onQualitySelected(quality) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (quality == selectedQuality),
                        onClick = { onQualitySelected(quality) }
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(text = quality.name, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}