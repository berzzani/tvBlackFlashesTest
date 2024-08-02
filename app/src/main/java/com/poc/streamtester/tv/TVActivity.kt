package com.poc.streamtester.tv

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import com.poc.streamtester.tv.ui.screens.ExplorationScreen
import com.poc.streamtester.tv.ui.screens.ExplorationScreenViewModel

@UnstableApi
class TVActivity : ComponentActivity() {

    private var toast by mutableStateOf<Toast?>(null)
    private lateinit var viewModel: ExplorationScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            viewModel =  ExplorationScreenViewModel(application)
            val messenger = remember {
                viewModel.messenger
            }
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                messenger.collect { message ->
                    if (message == null) {
                        toast?.cancel()
                        toast = null
                        return@collect
                    } else {
                        toast?.cancel()
                        toast = null
                        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
                        Log.d(TAG, "Attempting to show toast: $message")
                        toast?.show()

                    }
                }
            }
            ExplorationScreen(
                viewModel = viewModel,
            )
        }
    }

    @OptIn(androidx.media3.common.util.UnstableApi::class)
    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action != KeyEvent.ACTION_DOWN) return super.dispatchKeyEvent(event)

        when (event.keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                viewModel.setChannelToFocus(
                    (viewModel.channelToFocusFlow.value - 1).coerceAtLeast(
                        0
                    )
                )
            }

            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                viewModel.setChannelToFocus(
                    (viewModel.channelToFocusFlow.value + 1).coerceAtMost(
                        4
                    )
                )
            }

            KeyEvent.KEYCODE_1 -> {
                viewModel.setChannelToFocus(0)
                return@dispatchKeyEvent true
            }

            KeyEvent.KEYCODE_2 -> {
                viewModel.setChannelToFocus(1)
                return@dispatchKeyEvent true
            }

            KeyEvent.KEYCODE_3 -> {
                viewModel.setChannelToFocus(2)
                return@dispatchKeyEvent true
            }

            KeyEvent.KEYCODE_4 -> {
                viewModel.setChannelToFocus(3)
                return@dispatchKeyEvent true
            }

            KeyEvent.KEYCODE_5 -> {
                viewModel.setChannelToFocus(4)
                return@dispatchKeyEvent true
            }
        }

        return super.dispatchKeyEvent(event)
    }

    companion object {
        private const val TAG = "TVActivity"
    }
}

