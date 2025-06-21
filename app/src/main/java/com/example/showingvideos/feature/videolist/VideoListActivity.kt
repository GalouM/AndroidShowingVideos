package com.example.showingvideos.feature.videolist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            ShowingVideosTheme {
                val state by viewModel.displayState.collectAsStateWithLifecycle()
                Log.e("GAELLE", "state = $state")
                Column(modifier = Modifier.fillMaxSize()) {
                    Button(onClick = { viewModel.onEvent(VideoListEvent.SetQuery("cats")) }) {
                        Text(text = "cats")
                    }
                    Button(onClick = { viewModel.onEvent(VideoListEvent.SetQuery("dogs")) }) {
                        Text(text = "dogs")
                    }

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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShowingVideosTheme {
        Greeting("Android")
    }
}