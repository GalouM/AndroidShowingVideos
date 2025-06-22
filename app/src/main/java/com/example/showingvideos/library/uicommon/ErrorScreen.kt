package com.example.showingvideos.library.uicommon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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

@Composable
fun ErrorScreen(
    showRetryButton: Boolean,
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier,
    message: String = stringResource(id = R.string.default_error_message),
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
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )

        if (showRetryButton) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetryClicked) {
                Text(text = stringResource(id = R.string.retry_button_text))
            }
        }
    }
}

@Preview(showBackground = true, name = "Error Screen Without Retry")
@Composable
private fun ErrorScreenWithoutRetryPreview() {
    ErrorScreen(
        message = "Oops! Could not load videos.",
        showRetryButton = false,
        onRetryClicked = { /* noop */ }
    )
}

@Preview(showBackground = true, name = "Error Screen With Retry")
@Composable
private fun ErrorScreenWithRetryPreview() {
    ErrorScreen(
        message = "Network connection failed.",
        showRetryButton = true,
        onRetryClicked = { /* noop */ }
    )
}