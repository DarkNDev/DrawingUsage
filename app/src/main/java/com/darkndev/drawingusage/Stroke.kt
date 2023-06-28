package com.darkndev.drawingusage

import android.graphics.Path

data class Stroke(
    val color: Int,
    val strokeWidth: Float,
    val path: Path,
)
