package com.example.ottapp.data.model

import com.google.gson.annotations.SerializedName

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
