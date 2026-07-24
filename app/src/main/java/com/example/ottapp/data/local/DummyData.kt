package com.example.ottapp.data.local

import android.content.Context
import com.example.ottapp.data.model.SeriesListResponse
import com.example.ottapp.data.model.VideoItem
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Loads the dummy "Series list fetched" response bundled in
 * assets/dummy_response.json and parses it with Gson.
 *
 * In a production app this would instead come from a network response
 * (e.g. via Retrofit), but the repository layer is written so that
 * swapping this source out later does not require any change to the
 * ViewModel or UI - it's the exact same response shape.
 */
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
