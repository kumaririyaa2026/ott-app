package com.example.ottapp.utils

// 24840 -> "24.8K", matches the view count badge in the design
object ViewCountFormatter {

    fun format(views: Int): String = when {
        views >= 1_000_000 -> String.format("%.1fM", views / 1_000_000.0)
        views >= 1_000 -> String.format("%.1fK", views / 1_000.0)
        else -> views.toString()
    }
}
