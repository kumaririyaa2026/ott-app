package com.example.ottapp.utils

/**
 * Formats a raw view count (e.g. 24840) into a short display string
 * (e.g. "24.8K"), matching the view-count badge in the Figma reference.
 */
object ViewCountFormatter {

    fun format(views: Int): String = when {
        views >= 1_000_000 -> String.format("%.1fM", views / 1_000_000.0)
        views >= 1_000 -> String.format("%.1fK", views / 1_000.0)
        else -> views.toString()
    }
}
