package com.example.kasui.Data.structure

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json


class Convertor {
    val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromArtwork(value: Artwork): String = json.encodeToString(value)

    @TypeConverter
    fun toArtwork(value: String): Artwork = json.decodeFromString(value)

    @TypeConverter
    fun fromStringList(value: List<String>): String = json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> = json.decodeFromString(value)

    @TypeConverter
    fun fromGenre(value: Genre): String = json.encodeToString(value)

    @TypeConverter
    fun toGenre(value: String): Genre = json.decodeFromString(value)

    @TypeConverter
    fun fromGenreList(value: List<Genre>): String = json.encodeToString(value)

    @TypeConverter
    fun toGenreList(value: String): List<Genre> = json.decodeFromString(value)
    

}