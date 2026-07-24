package com.example.ottapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ottapp.data.model.VideoItem
import com.example.ottapp.databinding.ItemVideoBinding
import com.example.ottapp.utils.ViewCountFormatter

// Only binds data here. The actual video player is managed from
// ExploreFragment (VideoPlayerManager) since only one row should be
// playing at any time, not one player per item.
class VideoAdapter : ListAdapter<VideoItem, VideoAdapter.VideoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val playerView: PlayerView
            get() = binding.playerView

        fun bind(item: VideoItem) {
            binding.textTitle.text = item.title
            binding.textDescription.text = item.description
            binding.textViews.text = ViewCountFormatter.format(item.views)
            // like/save/share/volume icons and watch now button are UI only,
            // no click listeners needed
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoItem>() {
            override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem) =
                oldItem == newItem
        }
    }
}
