package com.example.kasui.Data.structure.song

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.EditorialNotes
import com.example.kasui.Data.structure.Genre


@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey val id: Long,
    val albumName: String?,
    val artistName: String?,
    val artistUrl: String?,
    val attribution: String?, // (Classical music only) The name of the artist or composer to attribute the song with.
//    val audioVariants: List<String>?,
    val composerName: String?,
    val contentRating: String?,
    val discNumber: Int?, // Album Disc number
//    val editorialNotes: EditorialNotes?,
    val inFavorites: Boolean?,
    val releaseDate: String?,
    val trackNumber: Int?,
    val url: String?,

    //Required
    val name: String,
    val durationInMillis: Int,
//    val genreNames: List<Genre>,
    val hasLyrics: Boolean,
    val uri: Uri
)

class Song(
    val id: Long,
    val href: String, // Location on device
    val attributes: SongAttributes,
    val relationships: SongRelationships
) {}

data class SongAttributes(
    // Maybe
    val albumName: String?,
    val artistName: String?,
    val artistUrl: String?,
    val artwork: Artwork?,
    val attribution: String?, // (Classical music only) The name of the artist or composer to attribute the song with.
    val audioVariants: List<String>?,
    val composerName: String?,
    val contentRating: String?,
    val discNumber: Int?, // Album Disc number
    val editorialNotes: EditorialNotes?,
    val inFavorites: Boolean?,
    val releaseDate: String?,
    val trackNumber: Int?,
    val url: String?,

    //Required
    val name: String,
    val durationInMillis: Int,
    val genreNames: List<String>,
    val hasLyrics: Boolean,
    val uri: Uri

)

// Long contains ID
data class SongRelationships(
    val albums: List<Long>,
    val artists: List<Long>,
    val composers: List<Long>,
    val genres: List<Long>,
    val library: List<Long>,
    val station: List<Long>,

    )