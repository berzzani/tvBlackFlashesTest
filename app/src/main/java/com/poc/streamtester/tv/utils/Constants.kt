package com.poc.streamtester.tv.utils

import com.poc.streamtester.tv.model.ChannelTileData

const val CHANNEL_ZAP_DELAY_MS = 500L

val CODECS_STREAMS = listOf(
    ChannelTileData(
        idx = 0,
        url = "https://storage.googleapis.com/berzzani-sample-stream/h262/index.m3u8",
        title = "Channel 1\nH.262",
    ),
    ChannelTileData(
        idx = 1,
        url = "https://storage.googleapis.com/berzzani-sample-stream/h264/index.m3u8",
        title = "Channel 2\nH.264",
    ),
)