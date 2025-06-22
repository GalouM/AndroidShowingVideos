package com.example.showingvideos.library.uicommon

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.reachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == layoutInfo.totalItemsCount - buffer
}