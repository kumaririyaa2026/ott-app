package com.example.ottapp.data.model

/**
 * Represents a single series/video row returned by the "Series list" API.
 * Mirrors the fields present in the dummy JSON response.
 */
data class VideoItem(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: String,
    val coverVideoRaw: String,
    val likes: Int,
    val views: Int,
    val ageRating: String
)
