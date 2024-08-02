package com.poc.streamtester.tv.ui.screens

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.poc.streamtester.tv.model.ChannelTileData
import com.poc.streamtester.tv.ui.components.ChannelPlayerState
import com.poc.streamtester.tv.usecase.ChannelExplorationUseCase
import com.poc.streamtester.tv.usecase.ChannelExplorationUseCaseImpl
import com.poc.streamtester.tv.usecase.ChannelPlayerUseCase
import com.poc.streamtester.tv.usecase.ChannelPlayerUseCaseImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@UnstableApi
class ExplorationScreenViewModel(
    application: Application,
    private val channelPlayerUsecase: ChannelPlayerUseCase = ChannelPlayerUseCaseImpl(),
    private val channelExplorationUseCase: ChannelExplorationUseCase = ChannelExplorationUseCaseImpl(),
) : AndroidViewModel(application) {


    private val TAG = "ExplorationViewModel"

    val messenger = MutableSharedFlow<String?>()

    /**************************** ChannelPlayer section ***********************************/
    val channelPlayerStateFlow: StateFlow<ChannelPlayerState> =
        channelPlayerUsecase.channelPlayerStateFlow

    fun updateChannelPlayerState(channelPlayerState: ChannelPlayerState) =
        channelPlayerUsecase.updateChannelPlayerState(channelPlayerState)

    val selectedChannelFlow: StateFlow<ChannelTileData?> = channelPlayerUsecase.selectedChannelFlow

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var trackSelector: DefaultTrackSelector
    fun initPlayer(context: Context): ExoPlayer {
        trackSelector = DefaultTrackSelector(context).apply {
            setParameters(
                buildUponParameters().setAllowMultipleAdaptiveSelections(true).build()
            )
        }
        exoPlayer = ExoPlayer.Builder(context)
            .setTrackSelector(
                trackSelector
            )
            .build()
        return exoPlayer
    }

    /*************************** ChannelExploration section *******************************/
    private val _channelToFocusFlow = MutableStateFlow(0)
    val channelToFocusFlow: StateFlow<Int> = _channelToFocusFlow
    fun setChannelToFocus(channelToFocus: Int) {
        _channelToFocusFlow.value = channelToFocus
    }

    fun onChannelFocused(channelTileData: ChannelTileData) {
        if (channelExplorationUseCase.onChannelFocused(channelTileData)) {
            channelPlayerUsecase.cancelZapChannelJob()
            channelPlayerUsecase.initZapChannelJob(viewModelScope, channelTileData)
        }
    }
}