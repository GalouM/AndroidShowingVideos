package com.example.showingvideos.feature.videolist.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.showingvideos.R
import com.example.showingvideos.ui.theme.ShowingVideosTheme

@Composable
internal fun EmptyState(
    modifier: Modifier = Modifier,
    message: String = stringResource(id = R.string.empty_screen)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    ShowingVideosTheme {
        EmptyState()
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStateCustomMessagePreview() {
    ShowingVideosTheme {
        EmptyState(message = "No items found. Try a different search!")
    }
}