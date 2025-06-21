package com.example.showingvideos.feature.videolist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.showingvideos.feature.videolist.screen.PixelDisclaimer
import com.example.showingvideos.feature.videolist.screen.SearchBar
import com.example.showingvideos.ui.theme.ShowingVideosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: VideoListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.onEvent(VideoListEvent.SetQuery("cats"))
        setContent {
            val uriHandler = LocalUriHandler.current
            ShowingVideosTheme {
                val state by viewModel.displayState.collectAsStateWithLifecycle()
                Log.e("GAELLE", "state = $state")

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
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

                        Button(onClick = { viewModel.onEvent(VideoListEvent.Refresh) }) {
                            Text(text = "refresh")
                        }

                        Button(onClick = { viewModel.onEvent(VideoListEvent.LoadNextPage) }) {
                            Text(text = "next page")
                        }
                    }
                }
            }
        }
    }
}

