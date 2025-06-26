package com.example.showingvideos.feature.videolist

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class SetPlayerSettingsUseCase @Inject constructor() {

    private val defaultPlayerState = PlayerSettingsState(
        playingQuality = PlayingQuality.Lowest,
    )

    private var lastState: PlayerSettingsState = defaultPlayerState
        set(value) {
            field = value
            playerStateChannel.trySend(value)
        }

    private val playerStateChannel = Channel<PlayerSettingsState>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun setVideoQuality(playingQuality: PlayingQuality) {
        lastState = lastState.copy(playingQuality = playingQuality)
    }

    val state: Flow<PlayerSettingsState> = playerStateChannel
        .receiveAsFlow().onStart { emit(defaultPlayerState) }

    data class PlayerSettingsState(
        val playingQuality: PlayingQuality,
    )
}