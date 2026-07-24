package com.example.ottapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a single series/video row returned by the "Series list" API.
 * Field names mirror dummy_response.json; snake_case JSON keys are mapped
 * to camelCase Kotlin properties via @SerializedName so Gson can parse the
 * asset directly into this data class.
 */
data class VideoItem(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: String,
    @SerializedName("cover_video_raw")
    val coverVideoRaw: String,
    val likes: Int,
    val views: Int,
    @SerializedName("age_rating")
    val ageRating: String
)
