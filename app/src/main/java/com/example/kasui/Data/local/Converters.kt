package com.example.kasui.Data.local

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter
import com.example.kasui.Data.request.FILETYPES
import com.example.kasui.Data.structure.Lyric

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

class FileTypeConvertor {

    @TypeConverter
    fun toFileType(filetype: Int): FILETYPES {
        return FILETYPES.entries[filetype]
    }

    @TypeConverter
    fun fromFileType(filetype: FILETYPES): Int {
        return filetype.ordinal
    }

}

class LyricConvertor {

    @TypeConverter
    fun toLyric(lyrics: String): List<Lyric> {
        return lyrics.split("^").mapNotNull {
            val timestamp = it.substringBefore("|").toLongOrNull()
            if (timestamp != null) {
                Lyric(timestamp, it.substringAfter("|"))
            } else {
                null
            }
        }
    }

    @TypeConverter
    fun fromLyrics(lyrics: List<Lyric>): String {
        return lyrics.map {
            it.timestamp.toString() + "|" + it.text
        }.joinToString("^")
    }

}