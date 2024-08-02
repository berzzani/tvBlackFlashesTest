package com.poc.streamtester.tv.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poc.streamtester.tv.model.ChannelTileData
import com.poc.streamtester.tv.ui.adapters.ChannelRowAdapter
import com.poc.streamtester.tv.ui.viewholders.ChannelTileViewHolder
import com.poc.streamtester.tv.utils.CODECS_STREAMS
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow


val channels = CODECS_STREAMS

@Composable
fun ChannelsBox(
    channelToFocusFlow: StateFlow<Int>,
    onChannelFocused: (ChannelTileData) -> Unit,
) {
    var recyclerView: RecyclerView? = remember {
        null
    }

    val channelToFocus by channelToFocusFlow.collectAsState()
    val entries = remember {
        channels
    }

    LaunchedEffect(recyclerView, channelToFocus) {
        while (recyclerView?.findViewHolderForAdapterPosition(channelToFocus) == null) {
            delay(500)
            Log.d("ChannelMatrixHybrid", "waiting for viewHolder")
        }
        if (recyclerView?.findViewHolderForAdapterPosition(channelToFocus) != null) {
            val viewHolder = recyclerView?.findViewHolderForAdapterPosition(channelToFocus)
            Log.d(
                "ChannelMatrixHybrid",
                "attempting focusRequest for viewHolder=${(viewHolder as ChannelTileViewHolder).channelName.text}"
            )
            viewHolder.tileBg.requestFocus()
        } else {
            Log.d("ChannelMatrixHybrid", "viewHolder is null")
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 100.dp)
            .fillMaxSize(),
    ) {
        AndroidView(
            factory = { ctx ->
                RecyclerView(ctx).apply {
                    layoutManager =
                        LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
                    adapter = ChannelRowAdapter(
                        channelTileDataList = entries,
                        onChannelFocused = onChannelFocused,
                    )
                    recycledViewPool.setMaxRecycledViews(0, 5)
                }
            },
            update = { view ->
                recyclerView = view
            }
        )
    }
}