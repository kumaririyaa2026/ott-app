package com.example.ottapp.data.repository

import android.content.Context
import com.example.ottapp.data.local.DummyData
import com.example.ottapp.data.model.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class VideoRepository(private val context: Context) {

    suspend fun getVideos(): List<VideoItem> = withContext(Dispatchers.IO) {
        delay(400) // fake network delay so the loading spinner is actually visible
        DummyData.getVideoList(context)
    }
}
