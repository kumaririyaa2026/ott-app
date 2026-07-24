package com.example.ottapp.data.local

import android.content.Context
import com.example.ottapp.data.model.SeriesListResponse
import com.example.ottapp.data.model.VideoItem
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

// reads the dummy json from assets and parses it with Gson
object DummyData {

    fun getVideoList(context: Context): List<VideoItem> {
        val json = readJsonFromAssets(context, "dummy_response.json")
        val response = Gson().fromJson(json, SeriesListResponse::class.java)
        return response.data.rows
    }

    private fun readJsonFromAssets(context: Context, fileName: String): String {
        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }
}
