package com.example.ottapp.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ottapp.data.model.VideoItem
import com.example.ottapp.databinding.ItemVideoBinding

/**
 * Displays each series/video row with an inline Media3 (ExoPlayer) preview,
 * title, a 2-line description, a "Watch Now" button, and UI-only action
 * icons (like / save / share / volume) as required by the brief.
 *
 * Each visible item owns its own ExoPlayer instance. Players are released
 * when their view is recycled or detached from the window so we don't leak
 * players as the user scrolls through a long list.
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

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        holder.releasePlayer()
    }

    override fun onFailedToRecycleView(holder: VideoViewHolder): Boolean {
        holder.releasePlayer()
        return super.onFailedToRecycleView(holder)
    }

    class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var player: ExoPlayer? = null

        fun bind(item: VideoItem) {
            binding.textTitle.text = item.title
            binding.textDescription.text = item.description
            binding.textViews.text = formatViewCount(item.views)
            // Icons and the Watch Now button are UI-only per the brief -
            // no click behaviour is wired up.

            releasePlayer()
            val context = binding.root.context
            val exoPlayer = ExoPlayer.Builder(context).build().also { player = it }
            binding.playerView.player = exoPlayer

            val mediaItem = MediaItem.fromUri(item.coverVideoRaw)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.volume = 0f // muted by default; unmuting is UI-only per the brief
            exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }

        fun releasePlayer() {
            player?.release()
            player = null
            binding.playerView.player = null
        }

        /** Formats a raw view count like 24840 as "24.8K", matching the badge in the Figma reference. */
        private fun formatViewCount(views: Int): String = when {
            views >= 1_000_000 -> String.format("%.1fM", views / 1_000_000.0)
            views >= 1_000 -> String.format("%.1fK", views / 1_000.0)
            else -> views.toString()
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
