package com.example.kasui.Data.local

import android.net.Uri
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