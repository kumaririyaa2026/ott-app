package com.example.ottapp.player

import android.content.Context
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

// Only one video should play at a time, so instead of giving every row its
// own ExoPlayer, we keep a single player here and move it between rows as
// the user scrolls (see ExploreFragment).
class VideoPlayerManager(context: Context) {

    private val appContext = context.applicationContext

    private val player: ExoPlayer = run {
        // some devices' hardware decoder throws DECODER_INIT_FAILED on these
        // videos, this lets exoplayer fall back to another decoder instead
        // of just failing
        val renderersFactory = DefaultRenderersFactory(appContext)
            .setEnableDecoderFallback(true)

        ExoPlayer.Builder(appContext, renderersFactory).build().apply {
            volume = 0f // muted by default, volume icon is UI only
            repeatMode = ExoPlayer.REPEAT_MODE_ALL

            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    Toast.makeText(
                        appContext,
                        "Video playback error: ${error.errorCodeName}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }

    private var attachedPlayerView: PlayerView? = null
    private var currentVideoUrl: String? = null

    fun play(playerView: PlayerView, videoUrl: String) {
        if (attachedPlayerView === playerView && currentVideoUrl == videoUrl) {
            return
        }

        attachedPlayerView?.player = null

        playerView.player = player
        attachedPlayerView = playerView
        currentVideoUrl = videoUrl

        player.setMediaItem(MediaItem.fromUri(videoUrl))
        player.prepare()
        player.playWhenReady = true
    }

    fun pause() {
        player.playWhenReady = false
    }

    fun resume() {
        player.playWhenReady = true
    }

    fun release() {
        attachedPlayerView?.player = null
        attachedPlayerView = null
        currentVideoUrl = null
        player.release()
    }
}
