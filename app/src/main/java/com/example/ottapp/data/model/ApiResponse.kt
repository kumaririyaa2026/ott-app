package com.example.ottapp.data.model

/**
 * Mirrors the top-level shape of the dummy JSON response:
 * { "success": ..., "message": ..., "data": { "rows": [ ... ] } }
 *
 * We are not hitting a live network endpoint for this assignment, so these
 * classes exist mainly to document the contract and to make it trivial to
 * swap DummyData for a real Retrofit call later (same shape, same repository
 * interface).
 */
data class SeriesListResponse(
    val success: Boolean,
    val message: String,
    val data: SeriesData
)

data class SeriesData(
    val rows: List<VideoItem>
)
