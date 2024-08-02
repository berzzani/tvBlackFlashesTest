package com.poc.streamtester.tv.ui.components

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.flow.StateFlow

@OptIn(UnstableApi::class)
@Composable
fun ChannelPlayer(
    modifier: Modifier = Modifier,
    initPlayer: (context: Context) -> ExoPlayer,
    url: String,
    channelPlayerStateFlow: StateFlow<ChannelPlayerState>,
    updateChannelPlayerState: (ChannelPlayerState) -> Unit
) {
    val channelPlayerState by channelPlayerStateFlow.collectAsState()
    val context = LocalContext.current
    var currentUri: String = remember {
        url
    }
    val exoPlayer = remember(context) {
        initPlayer(context)
    }

    LaunchedEffect(Unit) {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    updateChannelPlayerState(ChannelPlayerState.PLAYING)
                }
                Log.d("ChannelPlayer", "PlaybackStateChanged! playbackState=$playbackState")
            }

            override fun onPlayerError(error: PlaybackException) {
                Log.e("onPlayerError", "errorCode=${error.errorCode} error=${error.message}")
            }

            override fun onTracksChanged(tracks: Tracks) {
                super.onTracksChanged(tracks)
                Log.d(
                    "ChannelPlayer",
                    "TracksChanged! Tracks.groups of type video =${tracks.groups.filter { it.type == C.TRACK_TYPE_VIDEO }.size}"
                )
                Log.d(
                    "ChannelPlayer",
                    "exoplayer content live (Depreciated): ${exoPlayer.isCurrentWindowLive}")
                Log.d(
                    "ChannelPlayer",
                    "exoplayer content live :${exoPlayer.isCurrentMediaItemLive}")
                val videoFormats = tracks.groups.filter { it.type == C.TRACK_TYPE_VIDEO }
                    .map { it.mediaTrackGroup.getFormat(0) }
                Log.d(
                    "ChannelPlayer",
                    "video formats = ${videoFormats.map { "codecs ${it.codecs} width ${it.width} height ${it.height}" }}"
                )
            }
        })
    }


    LaunchedEffect(url) {
        if (url != currentUri) {
            url.also { currentUri = it }
            Log.d("ChannelPlayer", "Playing new $url")
            val mediaItem = MediaItem.fromUri(url)
            val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
            val hlsMediaSource = HlsMediaSource
                .Factory(dataSourceFactory)
                .setAllowChunklessPreparation(false)
                .createMediaSource(mediaItem)
            exoPlayer.apply {
                stop()
                setMediaSource(hlsMediaSource)
                playWhenReady = true
                prepare()
            }
        } else {
            exoPlayer.stop()
        }
    }

    LaunchedEffect(channelPlayerState) {
        exoPlayer.apply {
            volume = 1f
        }
    }

    AnimatedVisibility(
        visible = channelPlayerState == ChannelPlayerState.PLAYING,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .focusable(enabled = false),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = exoPlayer
                }
            },
            update = { view ->
                view.useController = false
                view.controllerAutoShow = false
                view.hideController()
                view.onResume()
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}

enum class ChannelPlayerState {
    IDLE,
    PLAYING
}