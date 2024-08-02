package com.poc.streamtester.tv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.poc.streamtester.tv.ui.components.ChannelsBox
import com.poc.streamtester.tv.ui.components.ChannelPlayer

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun ExplorationScreen(
    viewModel: ExplorationScreenViewModel,
    modifier: Modifier = Modifier
) {
    val selectedChannelFlow = remember { viewModel.selectedChannelFlow }
    val channelPlayerStateFlow = remember { viewModel.channelPlayerStateFlow }
    val onChannelFocused = remember { viewModel::onChannelFocused }
    val selectedChannel by selectedChannelFlow.collectAsState()

    Column(
        modifier = modifier
            .background(color = Color.LightGray)
            .focusable(false),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(50.dp)
                .fillMaxSize(0.6f)
        ) {
            ChannelPlayer(
                modifier = Modifier
                    .fillMaxSize(),
                initPlayer = viewModel::initPlayer,
                url = selectedChannel?.url ?: "",
                channelPlayerStateFlow = channelPlayerStateFlow,
                updateChannelPlayerState = viewModel::updateChannelPlayerState
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            )
        }
        Box(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally)
                .background(Color.Transparent)
        ) {
            ChannelsBox(
                channelToFocusFlow = remember { viewModel.channelToFocusFlow },
                onChannelFocused = onChannelFocused,
            )
        }
    }
}

