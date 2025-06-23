package com.example.showingvideos.library.uicommon

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.example.showingvideos.R
import kotlinx.coroutines.launch

@Composable
fun ErrorSnackBar(message: String, snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(message) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
            )
        }
    }
}

sealed interface SnackbarState {
    data class Error(@StringRes val message: Int = R.string.default_error_message) : SnackbarState
    data class Info(@StringRes val message: Int) : SnackbarState
}