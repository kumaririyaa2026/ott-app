package com.example.ottapp.data.repository

import android.content.Context
import com.example.ottapp.data.local.DummyData
import com.example.ottapp.data.model.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Single source of truth for video/series data used by the Explore screen.
 *
 * Today this simply returns the dummy JSON payload bundled with the app
 * (see DummyData / assets/dummy_response.json), but it is written as a
 * suspend function returning the same list shape a real network call
 * (e.g. Retrofit) would return, so switching to a live endpoint later only
 * requires changing the implementation of this one method - the ViewModel
 * and UI do not need to change.
 */
class VideoRepository(private val context: Context) {

    suspend fun getVideos(): List<VideoItem> = withContext(Dispatchers.IO) {
        // Simulated network latency so the loading state is visible.
        delay(400)
        DummyData.getVideoList(context)
    }
}
