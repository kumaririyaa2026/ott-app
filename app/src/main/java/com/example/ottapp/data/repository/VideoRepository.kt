package com.example.ottapp.data.repository

import com.example.ottapp.data.local.DummyData
import com.example.ottapp.data.model.VideoItem
import kotlinx.coroutines.delay

/**
 * Single source of truth for video/series data used by the Explore screen.
 *
 * Today this simply returns the dummy JSON payload from the assignment brief,
 * but it is written as a suspend function returning the same list shape a
 * real network call (e.g. Retrofit) would return, so switching to a live
 * endpoint later only requires changing the implementation of this one
 * method - the ViewModel and UI do not need to change.
 */
class VideoRepository {

    suspend fun getVideos(): List<VideoItem> {
        // Simulated network latency so the loading state is visible.
        delay(400)
        return DummyData.getVideoList()
    }
}
