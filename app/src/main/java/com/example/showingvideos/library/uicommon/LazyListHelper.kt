package com.example.showingvideos.library.uicommon

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.reachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == layoutInfo.totalItemsCount - buffer
}

fun LazyListState.firstVisibleItem(minimumVisiblePercentage: Float = MINIMUM_VISIBILITY_PERCENTAGE): Int {
    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    return when (visibleItemsInfo.size) {
        0 -> -1
        1 -> visibleItemsInfo.first().index
        else -> {
            val firstItem = visibleItemsInfo.first()
            val firstItemVisiblePercentage = firstItem.calculateVisiblePercentage(layoutInfo)
            if (firstItemVisiblePercentage > minimumVisiblePercentage) {
                firstItem.index
            } else {
                visibleItemsInfo[1].index
            }
        }
    }
}

private fun LazyListItemInfo.calculateVisiblePercentage(
    layoutInfo: LazyListLayoutInfo
): Float {
    if (size == 0) return 0f

    val itemTop = offset
    val itemBottom = offset + size

    val viewportTop = layoutInfo.viewportStartOffset
    val viewportBottom = layoutInfo.viewportEndOffset

    val visibleHeight = maxOf(0, minOf(itemBottom, viewportBottom) - maxOf(itemTop, viewportTop))

    return visibleHeight.toFloat() / size
}

private const val MINIMUM_VISIBILITY_PERCENTAGE = 0.6f