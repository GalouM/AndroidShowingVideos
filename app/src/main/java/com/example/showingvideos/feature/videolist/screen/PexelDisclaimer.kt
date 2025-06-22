package com.example.showingvideos.feature.videolist.screen

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.showingvideos.R

@Composable
internal fun PixelDisclaimer(onLinkClicked: (String) -> Unit, modifier: Modifier = Modifier) {
    val disclaimerFormat = stringResource(R.string.pexel_disclaimer)
    val linkText = stringResource(R.string.pexel_website)
    val annotatedString = buildAnnotatedString {
        val startIndex = disclaimerFormat.indexOf("%s")

        append(disclaimerFormat.substring(0, startIndex))
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(linkText)
        }
    }
    Text(
        text = annotatedString,
        modifier = modifier.clickable { onLinkClicked(PEXELS_WEBSITE) },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall,
        fontStyle = FontStyle.Italic
    )
}

@Preview(showBackground = true)
@Composable
private fun PixelDisclaimerPreview() {
    PixelDisclaimer({})
}

const val PEXELS_WEBSITE = "http://pexels.com"