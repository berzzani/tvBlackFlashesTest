package com.poc.streamtester.tv.usecase

import com.poc.streamtester.tv.model.ChannelTileData
import com.poc.streamtester.tv.ui.components.channels
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ChannelExplorationUseCase {
    val lastChannelFocusedFlow: StateFlow<ChannelTileData>

    /** return true if new ChannelFocused false otherwise **/
    fun onChannelFocused(channelTileData: ChannelTileData): Boolean
    val focusableChannels: StateFlow<List<ChannelTileData>>
}

class ChannelExplorationUseCaseImpl :
    ChannelExplorationUseCase {

    private val _lastChannelFocused: MutableStateFlow<ChannelTileData> = MutableStateFlow(
        defaultChannel
    )
    override val lastChannelFocusedFlow: StateFlow<ChannelTileData> = _lastChannelFocused
    val _focusableChannels: MutableStateFlow<List<ChannelTileData>> = MutableStateFlow(emptyList())
    override val focusableChannels: StateFlow<List<ChannelTileData>> = _focusableChannels
    override fun onChannelFocused(channelTileData: ChannelTileData): Boolean {
        _lastChannelFocused.value = channelTileData
        return true
    }
}

val defaultChannel = channels[0]