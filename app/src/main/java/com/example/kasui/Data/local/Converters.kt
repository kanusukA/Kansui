package com.example.kasui.Data.local

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter

class UriConverter {
    @TypeConverter
    fun toUri(uri: String): Uri? {
        return Uri.parse(uri)
    }

    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }
}

class ColorConverter {
    @TypeConverter
    fun toColor(color: Int): Color {
        return Color(color)
    }

    @TypeConverter
    fun fromColor(color: Color): Int {
        return color.toArgb()
    }
}