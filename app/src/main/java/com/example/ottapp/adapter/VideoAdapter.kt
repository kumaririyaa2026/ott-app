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

/**
 * Displays each series/video row: title, a 2-line description, view count,
 * a "Watch Now" button, and UI-only action icons (like / save / share /
 * volume) as required by the brief.
 *
 * This adapter does NOT own any ExoPlayer instances itself - only one video
 * should play at a time across the whole list, so a single shared player is
 * managed by ExploreFragment via VideoPlayerManager, which attaches itself
 * to whichever row's [PlayerView] is currently most visible. Keeping that
 * logic out of the adapter/ViewHolder keeps this class focused purely on
 * binding data, per MVVM.
 */
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

        /** Exposed so ExploreFragment can attach the shared player to whichever row is visible. */
        val playerView: PlayerView
            get() = binding.playerView

        fun bind(item: VideoItem) {
            binding.textTitle.text = item.title
            binding.textDescription.text = item.description
            binding.textViews.text = ViewCountFormatter.format(item.views)
            // Icons and the Watch Now button are UI-only per the brief -
            // no click behaviour is wired up.
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
