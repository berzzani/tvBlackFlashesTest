package com.poc.streamtester.tv.usecase

import com.poc.streamtester.tv.model.ChannelTileData
import com.poc.streamtester.tv.ui.components.ChannelPlayerState
import com.poc.streamtester.tv.utils.CHANNEL_ZAP_DELAY_MS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface ChannelPlayerUseCase {

    val channelPlayerStateFlow: StateFlow<ChannelPlayerState>
    fun updateChannelPlayerState(channelPlayerState: ChannelPlayerState)
    val selectedChannelFlow: StateFlow<ChannelTileData?>
    fun setSelectedChannel(scope: CoroutineScope, channelTileData: ChannelTileData)
    fun cancelZapChannelJob()
    fun initZapChannelJob(scope: CoroutineScope, channelTileData: ChannelTileData)
}

class ChannelPlayerUseCaseImpl : ChannelPlayerUseCase {
    private val _channelPlayerStateFlow = MutableStateFlow(ChannelPlayerState.IDLE)
    override val channelPlayerStateFlow: StateFlow<ChannelPlayerState> = _channelPlayerStateFlow

    override fun updateChannelPlayerState(channelPlayerState: ChannelPlayerState) {
        _channelPlayerStateFlow.value = channelPlayerState
    }

    val _selectedChannel = MutableStateFlow<ChannelTileData?>(null)
    override val selectedChannelFlow: StateFlow<ChannelTileData?> = _selectedChannel
    private val zapChannelJob: MutableStateFlow<Job?> = MutableStateFlow(null)
    override fun cancelZapChannelJob() {
        updateChannelPlayerState(ChannelPlayerState.IDLE)
        zapChannelJob.value?.cancel()
        zapChannelJob.value = null
    }

    override fun initZapChannelJob(scope: CoroutineScope, channelTileData: ChannelTileData) {
        zapChannelJob.value = scope.launch(Dispatchers.IO) {
            delay(CHANNEL_ZAP_DELAY_MS)
            setSelectedChannel(scope, channelTileData)
        }
    }

    override fun setSelectedChannel(scope: CoroutineScope, channelTileData: ChannelTileData) {
        if (_selectedChannel.value == channelTileData) return
        cancelZapChannelJob()
        _selectedChannel.value = channelTileData
        updateChannelPlayerState(ChannelPlayerState.PLAYING)
    }
}