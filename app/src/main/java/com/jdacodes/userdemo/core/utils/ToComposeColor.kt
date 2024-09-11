package com.jdacodes.userdemo.core.utils

import androidx.compose.ui.graphics.Color
typealias myColor = com.jdacodes.userdemo.dashboard.domain.model.Color
typealias androidColor = android.graphics.Color

// Extension function for your custom Color class
fun myColor.toComposeColor(): Color {
    return try {
        // Explicitly use the `color` property from your custom class
        Color(androidColor.parseColor(this.color))
    } catch (e: IllegalArgumentException) {
        Color.Gray // Fallback in case of invalid hex string
    }
}

