package com.example.ottapp.data.model

// matches the shape of dummy_response.json:
// { success, message, data: { rows: [...] } }
data class SeriesListResponse(
    val success: Boolean,
    val message: String,
    val data: SeriesData
)

data class SeriesData(
    val rows: List<VideoItem>
)
