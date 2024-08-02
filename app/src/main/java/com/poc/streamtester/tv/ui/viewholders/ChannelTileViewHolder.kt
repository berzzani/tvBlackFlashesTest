package com.poc.streamtester.tv.ui.viewholders

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.poc.streamtester.R
import com.poc.streamtester.tv.model.ChannelTileData

class ChannelTileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tileBg: ImageView = view.findViewById(R.id.tile_bg)
    val channelName: TextView = view.findViewById(R.id.channel_name)

    fun bindTile(
        channelTileData: ChannelTileData,
        onChannelFocused: (ChannelTileData) -> Unit,
    ) {
        tileBg.isFocusable = true
        tileBg.setBackgroundColor(Color.DKGRAY)
        tileBg.setOnFocusChangeListener { tile, hasFocus ->
            Log.d("ChannelTileViewHolder", "onFocusChange! hasFocus=$hasFocus")
            if (hasFocus) {
                onChannelFocused(channelTileData)
                tile.setBackgroundColor(Color.GRAY)
            } else {
                tile.setBackgroundColor(Color.DKGRAY)
            }
        }
        channelName.text = channelTileData.title
        channelName.setTextColor(Color.CYAN)
    }
}