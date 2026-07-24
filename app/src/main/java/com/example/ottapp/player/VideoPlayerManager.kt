package com.example.ottapp.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/**
 * Owns a single [ExoPlayer] instance that is shared across the Explore feed,
 * so that only one video ever plays at a time.
 *
 * Whenever a new row becomes the "current" one (see the visibility check in
 * ExploreFragment), we detach the player from whichever [PlayerView] it was
 * previously attached to and re-attach it to the new one. Because it's the
 * same underlying player, this automatically pauses/stops the previous
 * video - there's nothing extra to pause manually.
 *
 * Call [release] when the Explore screen's view is destroyed so we don't
 * leak the player.
 */
class VideoPlayerManager(context: Context) {

    private val player: ExoPlayer = ExoPlayer.Builder(context.applicationContext).build().apply {
        // Muted by default - the volume icon in the UI is UI-only per the brief.
        volume = 0f
        repeatMode = ExoPlayer.REPEAT_MODE_ALL
    }

    private var attachedPlayerView: PlayerView? = null
    private var currentVideoUrl: String? = null

    /** Attaches the shared player to [playerView] and starts playing [videoUrl]. */
    fun play(playerView: PlayerView, videoUrl: String) {
        if (attachedPlayerView === playerView && currentVideoUrl == videoUrl) {
            // Already playing this exact row - nothing to do.
            return
        }

        // Detach from whichever row previously owned the player.
        attachedPlayerView?.player = null

        playerView.player = player
        attachedPlayerView = playerView
        currentVideoUrl = videoUrl

        player.setMediaItem(MediaItem.fromUri(videoUrl))
        player.prepare()
        player.playWhenReady = true
    }

    /** Pauses playback without releasing the player (e.g. when the tab isn't visible). */
    fun pause() {
        player.playWhenReady = false
    }

    /** Resumes playback of whatever is currently attached (e.g. when the tab becomes visible again). */
    fun resume() {
        player.playWhenReady = true
    }

    /** Detaches from the current view and releases the player. Call this from onDestroyView. */
    fun release() {
        attachedPlayerView?.player = null
        attachedPlayerView = null
        currentVideoUrl = null
        player.release()
    }
}
