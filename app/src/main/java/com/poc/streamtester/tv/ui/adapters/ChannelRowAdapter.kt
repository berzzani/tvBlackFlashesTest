package com.poc.streamtester.tv.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poc.streamtester.R
import com.poc.streamtester.tv.model.ChannelTileData
import com.poc.streamtester.tv.ui.viewholders.ChannelTileViewHolder

class ChannelRowAdapter(
    private val channelTileDataList: List<ChannelTileData>,
    private val onChannelFocused: (ChannelTileData) -> Unit,
) : RecyclerView.Adapter<ChannelTileViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelTileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
        return ChannelTileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelTileViewHolder, position: Int) {
        val channelTileData = channelTileDataList[position]
        holder.bindTile(
            channelTileData = channelTileData,
            onChannelFocused = onChannelFocused,
        )
    }

    override fun getItemCount() = channelTileDataList.size
}